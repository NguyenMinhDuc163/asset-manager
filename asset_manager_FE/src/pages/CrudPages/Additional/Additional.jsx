import React, { useState, useEffect } from "react";
import MultilineChartIcon from "@material-ui/icons/MultilineChart";
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
import AdditionalForm from "./AdditionalForm";
import PageHeader from "../../../oftadeh-layouts/layout/PageHeader";
import useTable from "../commons/useTable";
import Controls from "../commons/Controls";
import Popup from "../commons/Popup";
import Notification from "../commons/Notification";
import ConfirmDialog from "../commons/ConfirmDialog";
import * as AdditionalService from "../../../services/additionalService";
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
  { id: "time", label: "Thời gian thực hiện bổ sung" },
  { id: "userId", label: "Người bổ sung" },
  { id: "organizationId", label: "Tổ chức" },
  { id: "inProcess", label: "Trạng thái" },
  { id: "createdAt", label: "Ngày tạo dữ liệu" },
  { id: "updatedAt", label: "Ngày cập nhật" },
  { id: "actionss", label: "Chuyển trạng thái ", disableSorting: true },
  { id: "actions", label: "Actions", disableSorting: true },
];

export default function Additional(props) {
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
  const AdditionalHandled = (object) => {
    const objectConverted = object.map((item) => {
      item.time = utils.convertDateTime(item.time);
      item.createdAt = utils.convertDateTime(item.createdAt);
      item.updatedAt = utils.convertDateTime(item.updatedAt);
      const a = {
        userId: item.user.id,
        organizationId: item.organization.id,
      };
      return Object.assign(item, a);
    });
    return objectConverted;
  };

  const AdditionalHandledToInsert = (obj) => {
    const temp = {
      time: obj.time,
      embedded: {
        userId: obj.userId,
        organizationId: obj.organizationId,
      },
    };
    return temp;
  };

  const AdditionalHandledToUpdate = (obj) => {
    const temp = {
      id: obj.id,
      time:  moment(obj.time, 'DD-MM-YYYY hh:mm:ss A').toISOString(),
      embedded: {
        categoryId: obj.userId,
        calculationUnitId: obj.organizationId,
      },
    };
    return temp;
  };

  //=======================================   XỬ LÝ CALL API    ===========================================
  const getAdditionalAndUpdateToState = async () => {
    try {
      const { data: responseData } = await AdditionalService.getAllAdditional();
      const { data: Additional } = responseData;
      setRecords(AdditionalHandled(Additional));
    } catch (ex) {
      if ((ex.response && ex.response.status === 403) || (ex.response && ex.response.status === 401))
      {
        toast("Bạn không có quyền hạn truy cập trang này");
      }else {
        toast.error("Errors: Lỗi tải lên dữ liệu ");
      }
    }
  };

  const insertAdditional = async (Additional) => {
    try {
      await AdditionalService.insertAdditional(
        AdditionalHandledToInsert(Additional)
      );
      getAdditionalAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Thêm thành công",
        type: "success",
      });
    } catch (ex) {
      if (ex.response && ex.response.status === 500)
      {
        toast("Đợt bổ sung chưa hoàn tất nên không thể thêm mới");
      }else {
        toast.error("Errors: Lỗi thêm mới dữ liệu ");
      }
     
    }
  };

  const updateAdditional = async (Additional) => {
    try {
      await AdditionalService.updateAdditional(
        AdditionalHandledToUpdate(Additional)
      );
      getAdditionalAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Cập nhật thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi cập nhật dữ liệu ");
    }
  };

  const deleteAdditional = async (AdditionalId) => {
    const originalAdditionalRecord = records;
    const newAdditionalRecord = originalAdditionalRecord.filter(
      (x) => x.id !== AdditionalId
    );
    setRecords(newAdditionalRecord);
    try {
      await AdditionalService.deleteAdditional(AdditionalId);
      setNotify({
        isOpen: true,
        message: "Đã xoá thành công",
        type: "error",
      });
    } catch (ex) {
      if (ex.response && ex.response.status === 500)
      {
        toast("Đợt bổ sung này đã tồn tại trong hệ thống nên không thể xóa");
      }else {
        toast.error("Errors: Lỗi xóa dữ liệu ");
      }
      setRecords(originalAdditionalRecord);
    }
  };

  const changeStatusAdditional = async (additonal) => {

    const infoChange =  { id:additonal.id,
      status:!additonal.inProcess};
    try {
      await AdditionalService.changeStatusAdditional(
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
    getAdditionalAndUpdateToState();
  };

  //===================================================================================

  useEffect(getAdditionalAndUpdateToState, []);
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

  const  handleChangeStatus = (item) => {
    changeStatusAdditional(item);
  };
  
  const addOrEdit = (Additional, resetForm) => {
    if (Additional.id === 0) insertAdditional(Additional);
    else updateAdditional(Additional);
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
    deleteAdditional(id);
  };

  return (
    <>
       <OftadehLayout>
       <PageHeader
        history={history}
        title="Đợt bổ sung"
        subTitle="Tất cả các đợt bổ sung mà hệ thống hiện đang quản lý"
        icon={<MultilineChartIcon fontSize="large" />}
      />
      <Paper elevator={3} className={classes.pageContent}>
        <div className={classes.paper}>
          <Grid container spacing={3}>
            <Grid item sm={9}>
              <Controls.Input
                label="Tìm kiếm đợt bổ sung"
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
                <TableCell>{item.organization.name}</TableCell>
                <TableCell>{item.inProcess.toString() === "false" ? "Hoàn tất" : " Chưa"}</TableCell>
                <TableCell>{item.createdAt}</TableCell>
                <TableCell>{item.updatedAt}</TableCell>
                <TableCell>
                <Controls.AlertDialogSlide 
                value = {item.inProcess}
                disabled = {item.inProcess ? false : true}
                onChange = {() => handleChangeStatus(item)}
                description = {" Khi bạn đánh dấu đợt bổ sung này là hoàn tất, bạn sẽ không thể quay trở lại trạng thái ban đầu, hãy chắc chắn mọi thứ là hợp lệ trước khi thực hiện thao tác này!"}
                title = {"Xác nhận thao tác hoàn tất đợt bổ sung"} 
                toolTipOn = {"Chuyển sang  hoàn tất"}
                toolTipOff = {"Chuyến về chưa hoàn tất"}
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
                        title: "Bạn có chắc chắn xóa đợt bổ sung này không?",
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
        title="Biểu mẫu đợt bổ sung"
        openPopup={openPopup}
        setOpenPopup={setOpenPopup}
      >
        <AdditionalForm recordForEdit={recordForEdit} addOrEdit={addOrEdit} />
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
