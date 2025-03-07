import React, { useState, useEffect } from "react";
import ChildFriendlyIcon from "@material-ui/icons/ChildFriendly";
import { Search } from "@material-ui/icons";
import AddIcon from "@material-ui/icons/Add";
import EditOutlinedIcon from "@material-ui/icons/EditOutlined";
import CloseIcon from "@material-ui/icons/Close";
import Grid from "@material-ui/core/Grid";
import { toast } from "react-toastify";
import {
  Paper,
  makeStyles,
  withStyles,
  TableBody,
  TableRow,
  TableCell,
  InputAdornment,
} from "@material-ui/core";
import OftadehLayout from "../../../components/OftadehLayout/OftadehLayout";
import LiquidateForm from "./LiquidateForm";
import PageHeader from "../../../oftadeh-layouts/layout/PageHeader";
import useTable from "../commons/useTable";
import Controls from "../commons/Controls";
import Popup from "../commons/Popup";
import Notification from "../commons/Notification";
import ConfirmDialog from "../commons/ConfirmDialog";
import * as liquidateService from "../../../services/liquidateService";
import * as utils from "../../../utils/Utils.js";
import moment from "moment";
const useStyles = makeStyles((theme) => ({
  pageContent: {
    marginTop: theme.spacing(4),
    marginBottom: theme.spacing(5),
    padding: theme.spacing(3),
  },
  searchInput: {
    width: "75%",
  },
  newButton: {
    maxHeight: "50px",
    width: "75%",
  },
}));

const StyledTableRow = withStyles((theme) => ({
  root: {
    "&:nth-of-type(even)": {
      backgroundColor: theme.palette.action.selected,
    },
  },
}))(TableRow);

const headCells = [
  { id: "time", label: "Thời gian thanh lý" },
  { id: " user.fullName", label: "Người thanh lý" ,  disableSorting: true},
  { id: "done", label: "Trạng thái" },
  { id: "createdAt", label: "Ngày tạo dữ liệu" },
  { id: "updatedAt", label: "Ngày cập nhật" },
  { id: "actionss", label: "Chuyển trạng thái ", disableSorting: true },
  { id: "actions", label: "Actions", disableSorting: true },
];

export default function Liquidate(props) {
  const { history } = props;
  const classes = useStyles();
  const [recordForEdit, setRecordForEdit] = useState(null);
  const [records, setRecords] = useState([]);
  const [filterFn, setFilterFn] = useState({
    fn: (items) => {
      return items;
    },
  });
  const [openPopup, setOpenPopup] = useState(false);
  const [notify, setNotify] = useState({
    isOpen: false,
    message: "",
    type: "",
  });
  const [confirmDialog, setConfirmDialog] = useState({
    isOpen: false,
    title: "",
    subTitle: "",
  });

  // ======================================    XỬ LÝ DATA FROM SERVER ====================================
  const LiquidateHandledToShow = (obj) => {
    const objConverted = obj.map((item) => {
      item.createdAt = utils.convertDateTime(item.createdAt);
      item.updatedAt = utils.convertDateTime(item.updatedAt);
      item.time = utils.convertDateTime(item.time);
      const additionalProps = {
        userId: item.user.id,
      };
      return Object.assign(item, additionalProps);
    });
    return objConverted;
  };

  const LiquidateHandledToInsert = (obj) => {
    const temp = {
      time: obj.time,
      embedded: {
        userId: obj.userId,
      },
    };
    return temp;
  };

  const LiquidateHandledToUpdate = (obj) => {
    const temp = {
      id: obj.id,
      time: moment(obj.time, 'DD-MM-YYYY hh:mm:ss A').toISOString(),
      embedded: {
        userId: obj.userId,
      },
    };
    return temp;
  };
  // ======================================================================================================

  //=======================================   XỬ LÝ CALL API    ===========================================
  const getLiquidateAndUpdateToState = async () => {
    try {
      const { data: responseData } = await liquidateService.getAllLiquidate();
      const { data: Liquidate } = responseData;
      setRecords(LiquidateHandledToShow(Liquidate));
    } catch (ex) {
      if ((ex.response && ex.response.status === 403) || (ex.response && ex.response.status === 401))
      {
        toast("Bạn không có quyền hạn truy cập trang này");
      }else {
        toast.error("Errors: Lỗi tải lên dữ liệu ");
      }
    }
  };

  const insertLiquidate = async (Liquidate) => {
    try {
      await liquidateService.insertLiquidate(
        LiquidateHandledToInsert(Liquidate)
      );
      getLiquidateAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Thêm thành công",
        type: "success",
      });
    } catch (ex) {

        if (ex.response && ex.response.status === 500)
        {
          toast("Đợt thanh lý chưa hoàn tất nên không thể thêm mới");
        }else {
          toast.error("Errors: Lỗi thêm mới dữ liệu ");
        }
    }
  };

  const updateLiquidate = async (Liquidate) => {
    try {
      await liquidateService.updateLiquidate(
        LiquidateHandledToUpdate(Liquidate)
      );
      getLiquidateAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Cập nhật thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi cập nhật dữ liệu ");
    }
  };

  const deleteLiquidate = async (LiquidateId) => {
    const originalLiquidateRecord = records;
    const newLiquidateRecord = originalLiquidateRecord.filter(
      (x) => x.id !== LiquidateId
    );
    setRecords(newLiquidateRecord);
    try {
      await liquidateService.deleteLiquidate(LiquidateId);
      setNotify({
        isOpen: true,
        message: "Đã xoá thành công",
        type: "error",
      });
    } catch (ex) {
      if (ex.response && ex.response.status === 500)
      {
        toast("Đợt thanh lý này đã tồn tại trong hệ thống nên không thể xóa");
      }else {
        toast.error("Errors: Lỗi xóa dữ liệu ");
      }
    
      setRecords(originalLiquidateRecord);
    }
  };


  const changeStatusLiquidate = async (Liquidate) => {
    const infoChange =  { id:Liquidate.id,
      status:!Liquidate.done};
    try {
      await liquidateService.changeStatusLiquidate(
        infoChange
      );
   
      setNotify({
        isOpen: true,
        message: "Thay đổi trạng thái thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: Thay đổi trạng thái thất bại ");
    }
    getLiquidateAndUpdateToState();
  };
  //===================================================================================

  useEffect(getLiquidateAndUpdateToState, []);

  const {
    TblContainer,
    TblHead,
    TblPagination,
    recordsAfterPagingAndSorting,
  } = useTable(records, headCells, filterFn);

  const handleSearch = (e) => {
    let target = e.target;
    setFilterFn({
      fn: (items) => {
        if (target.value === "") return items;
        else
          return items.filter((x) =>
            x.time.toLowerCase().includes(target.value)
          );
      },
    });
  };

  const handleChangeStatus = (item) => {
    changeStatusLiquidate(item);
  };

  const addOrEdit = (Liquidate, resetForm) => {
    if (Liquidate.id === 0) insertLiquidate(Liquidate);
    else updateLiquidate(Liquidate);
    resetForm();
    setRecordForEdit(null);
    setOpenPopup(false);
  };

  const openInPopup = (item) => {
    setRecordForEdit(item);
    setOpenPopup(true);
  };

  const onDelete = (id) => {
    setConfirmDialog({
      ...confirmDialog,
      isOpen: false,
    });
    deleteLiquidate(id);
  };

  return (
    <>
     <OftadehLayout>
     <PageHeader
        history={history}
        title="Đợt thanh lý"
        subTitle="Tất cả các đợt thanh lý mà bạn hiện đang quản lý"
        icon={<ChildFriendlyIcon fontSize="large" />}
      />
      <Paper elevator={3} className={classes.pageContent}>
        <div className={classes.paper}>
          <Grid container spacing={3}>
            <Grid item sm={9}>
              <Controls.Input
                label="Tìm kiếm thời gian thanh lý"
                className={classes.searchInput}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Search />
                    </InputAdornment>
                  ),
                }}
                onChange={handleSearch}
              />
            </Grid>
            <Grid item sm={3}>
              <Controls.Button
                text="Thêm mới"
                variant="outlined"
                startIcon={<AddIcon />}
                className={classes.newButton}
                onClick={() => {
                  setOpenPopup(true);
                  setRecordForEdit(null);
                }}
              />
            </Grid>
          </Grid>
        </div>
        <TblContainer>
          <TblHead />
          <TableBody>
            {recordsAfterPagingAndSorting().map((item) => (
              <StyledTableRow key={item.id}>
                <TableCell>{item.time}</TableCell>
                <TableCell>{item.user.fullName}</TableCell>
                <TableCell>{item.done.toString() === "true" ? "Hoàn tất" : " Chưa"}</TableCell> 
                <TableCell>{item.createdAt}</TableCell>
                <TableCell>{item.updatedAt}</TableCell>
                <TableCell>
                <Controls.AlertDialogSlide 
                value = {item.done}
                disabled = {item.done ? true : false}
                onChange = {() => handleChangeStatus(item)}
                description = {"Khi bạn đánh dấu đợt thanh lý này là hoàn tất, bạn sẽ không thể quay trở lại trạng thái ban đầu, hãy chắc chắn mọi thứ là hợp lệ trước khi thực hiện thao tác này!"}
                title = {"Xác nhận thao tác thanh lý"} 
                toolTipOn = {"Chuyển về chưa hoàn tất"}
                toolTipOff = {"Chuyến sang hoàn tất"}
                />
                </TableCell>
                <TableCell>
                  <Controls.ActionButton
                    color="primary"
                    onClick={() => {
                      openInPopup(item);
                    }}
                  >
                    <EditOutlinedIcon fontSize="small" />
                  </Controls.ActionButton>
                  <Controls.ActionButton
                    color="secondary"
                    onClick={() => {
                      setConfirmDialog({
                        isOpen: true,
                        title: "Bạn có chắc chắn xóa đợt thanh lý này không?",
                        subTitle: "Bạn không thể hoàn tác thao tác này",
                        onConfirm: () => {
                          onDelete(item.id);
                        },
                      });
                    }}
                  >
                    <CloseIcon fontSize="small" />
                  </Controls.ActionButton>
                </TableCell>
               
              </StyledTableRow>
            ))}
          </TableBody>
        </TblContainer>
        <TblPagination />
      </Paper>
      <Popup
        title="Biểu mẫu đợt thanh lý"
        openPopup={openPopup}
        setOpenPopup={setOpenPopup}
      >
        <LiquidateForm recordForEdit={recordForEdit} addOrEdit={addOrEdit} />
      </Popup>
      <Notification notify={notify} setNotify={setNotify} />
      <ConfirmDialog
        confirmDialog={confirmDialog}
        setConfirmDialog={setConfirmDialog}
      />  
      </OftadehLayout>
    </>
  );
}
