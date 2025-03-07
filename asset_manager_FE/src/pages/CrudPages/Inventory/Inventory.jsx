import React, { useState, useEffect } from "react";
import EqualizerIcon from '@material-ui/icons/Equalizer';
import { Search } from "@material-ui/icons";
import AddIcon from "@material-ui/icons/Add";
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
import InventoryForm from "./InventoryForm";
import PageHeader from "../../../oftadeh-layouts/layout/PageHeader";
import useTable from "../commons/useTable";
import Controls from "../commons/Controls";
import Popup from "../commons/Popup";
import Notification from "../commons/Notification";
import ConfirmDialog from "../commons/ConfirmDialog";
import * as InventoryService from "../../../services/inventoryService";
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
  { id: "time", label: "Định kỳ kiểm kê" },
  { id: "startTime", label: "Thời Gian Bắt đầu" },
  { id: "endTime", label: "Thời Gian Kết Thúc" },
  { id: "inCheck", label: "Cho phép kiểm kê" },
  { id: "createdAt", label: "Ngày tạo dữ liệu" },
  { id: "updatedAt", label: "Ngày cập nhật" },
  { id: "actionss", label: "Chuyển trạng thái", disableSorting: true },
  { id: "actions", label: "Actions", disableSorting: true },
];

export default function Inventory(props) {
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
  const InventoryHandled = (object) => {
    const objectConverted = object.map((item) => {
      item.time = utils.convertDateTime(item.time);
      item.startTime = utils.convertDateTime(item.startTime);
      item.endTime = utils.convertDateTime(item.endTime);
      item.createdAt = utils.convertDateTime(item.createdAt);
      item.updatedAt = utils.convertDateTime(item.updatedAt);
      return item;
    });
    return objectConverted;
  };

  //=======================================   XỬ LÝ CALL API    ===========================================
  const getInventoryAndUpdateToState = async () => {
    try {
      const { data: responseData } = await InventoryService.getAllInventory();
      const { data: Inventory } = responseData;

      setRecords(InventoryHandled(Inventory));
    } catch (ex) {
      if ((ex.response && ex.response.status === 403) || (ex.response && ex.response.status === 401))
      {
        toast("Bạn không có quyền hạn truy cập trang này");
      }else {
        toast.error("Errors: Lỗi tải lên dữ liệu ");
      }
    }
  };

  const insertInventory = async (Inventory) => {
    try {
      await InventoryService.insertInventory(Inventory);
      getInventoryAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Thêm thành công",
        type: "success",
      });
    } catch (ex) {
      if (ex.response && ex.response.status === 500)
      {
        toast("Đợt kiêm kê chưa hoàn tất nên không thể thêm mới");
      }else {
        toast.error("Errors: Lỗi thêm mới dữ liệu ");
      }
    }
  };

  const updateInventory = async (Inventory) => {
 
    Inventory.time = moment(Inventory.time, 'DD-MM-YYYY hh:mm:ss A').toISOString();
    Inventory.startTime = moment(Inventory.startTime, 'DD-MM-YYYY hh:mm:ss A').toISOString() ;
    Inventory.endTime = moment(Inventory.endTime, 'DD-MM-YYYY hh:mm:ss A').toISOString();
 
    try {
      await InventoryService.updateInventory(Inventory);
      getInventoryAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Cập nhật thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi cập nhật dữ liệu ");
    }
  };

  const deleteInventory = async (InventoryId) => {
    const originalInventoryRecord = records;
    const newInventoryRecord = originalInventoryRecord.filter(
      (x) => x.id !== InventoryId
    );
    setRecords(newInventoryRecord);
    try {
      await InventoryService.deleteInventory(InventoryId);
      setNotify({
        isOpen: true,
        message: "Đã xoá thành công",
        type: "error",
      });
    } catch (ex) {
      if (ex.response && ex.response.status === 500)
      {
        toast("Đợt kiểm kê này đã tồn tại trong hệ thống nên không thể xóa");
      }else {
        toast.error("Errors: Lỗi xóa dữ liệu ");
      }
      setRecords(originalInventoryRecord);
    }
  };
  
  const changeStatusInventory = async (Inventory) => {

    const infoChange =  { id:Inventory.id,
      status:!Inventory.inCheck};
    try {
      await InventoryService.changeStatusInventory(
        infoChange
      );
      setNotify({
        isOpen: true,
        message: "Thay đổi trạng thái thành công",
        type: "success",
      });
    } catch (ex) {
      if (ex.response && ex.response.status === 500)
      {
        toast("Chỉ cho phép tồn tại một đợt kiểm kê chưa hoàn tất");
      }else {
        toast.error("Errors: Lỗi thay đổi trạng thái");
      }
    }
    getInventoryAndUpdateToState();
  };
  //===================================================================================

  useEffect(getInventoryAndUpdateToState, []);
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
    changeStatusInventory(item);
  };

  const addOrEdit = (Inventory, resetForm) => {
    if (Inventory.id === 0) insertInventory(Inventory);
    else updateInventory(Inventory);
    resetForm();
    setRecordForEdit(null);
    setOpenPopup(false);
  };

  const onDelete = (id) => {
    setConfirmDialog({
      ...confirmDialog,
      isOpen: false,
    });
    deleteInventory(id);
  };

  return (
    <>
     <OftadehLayout>
     <PageHeader
        history={history}
        title="Đợt kiểm kê"
        subTitle="Tất cả các đợt kiểm kê mà bạn hiện đang quản lý"
        icon={<EqualizerIcon fontSize="large" />}
      />
      <Paper elevator={3} className={classes.pageContent}>
        <div className={classes.paper}>
          <Grid container spacing={3}>
            <Grid item sm={9}>
              <Controls.Input
                label="Tìm kiếm đợt kiểm kê "
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
                <TableCell>{item.startTime}</TableCell>
                <TableCell>{item.endTime}</TableCell>
                <TableCell>{item.inCheck.toString() === "true" ? "Cho phép" : "Không"}</TableCell>
                <TableCell>{item.createdAt}</TableCell>
                <TableCell>{item.updatedAt}</TableCell>
                <TableCell>
                <Controls.AlertDialogSlide 
                 value = {item.inCheck}
                  onChange = {() => handleChangeStatus(item)}
                  description = {"Khi bạn mở đợt kiểm kê, các thao tác Scan QR code để kiểm kê được cho phép, ngược lại sẽ không có thao tác kiểm kê nào được thực hiện!"}
                  title = {"Xác nhận việc chuyển đổi trạng thái của đợt kiểm kê"} 
                  toolTipOff = {"Chuyển sang cho phép kiểm kê"}
                  toolTipOn = {"Chuyến sang kết thúc đợt kiểm kê"}
                  />
                </TableCell>
            
                <TableCell>
                  <Controls.ActionButton
                    color="secondary"
                    onClick={() => {
                      setConfirmDialog({
                        isOpen: true,
                        title: "Bạn có chắc chắn xóa đợt kiểm kê này không?",
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
        title="Biểu mẫu Đợt kiểm kê"
        openPopup={openPopup}
        setOpenPopup={setOpenPopup}
      >
        <InventoryForm recordForEdit={recordForEdit} addOrEdit={addOrEdit} />
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
