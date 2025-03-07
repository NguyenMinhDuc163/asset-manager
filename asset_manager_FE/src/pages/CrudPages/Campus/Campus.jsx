import React, { useState, useEffect } from "react";
import AccountBalanceIcon from "@material-ui/icons/AccountBalance";
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
import CampusForm from "./CampusForm";
import PageHeader from "../../../oftadeh-layouts/layout/PageHeader";
import useTable from "../commons/useTable";
import Controls from "../commons/Controls";
import Popup from "../commons/Popup";
import Notification from "../commons/Notification";
import ConfirmDialog from "../commons/ConfirmDialog";
import * as CampusService from "../../../services/campusService";
import * as utils from "../../../utils/Utils.js";

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
  { id: "name", label: "Mã khuôn viên " },
  { id: "campusType", label: "Loại khuôn viên" },
  { id: "contactEmail", label: "Email liên lạc" },
  { id: "contactPhone", label: "Số đ.t liên lạc" },
  { id: "description", label: "Mô tả chi tiết" },
  { id: "location", label: "Địa điểm" },
  { id: "mapUrl", label: "Map Url" },
  { id: "actions", label: "Actions", disableSorting: true },
];

export default function Campus(props) {
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
  const CampusHandled = (object) => {
    const objectConverted = object.map((item) => {
      item.createdAt = utils.convertDateTime(item.createdAt);
      item.updatedAt = utils.convertDateTime(item.updatedAt);
      return item;
    });
    return objectConverted;
  };

  //=======================================   XỬ LÝ CALL API    ===========================================
  const getCampusAndUpdateToState = async () => {
    try {
      const { data: responseData } = await CampusService.getAllCampus();
      const { data: Campus } = responseData;
      setRecords(CampusHandled(Campus));
    } catch (ex) {
      if ((ex.response && ex.response.status === 403) || (ex.response && ex.response.status === 401))
      {
        toast("Bạn không có quyền hạn truy cập trang này");
      }else {
        toast.error("Errors: Lỗi thêm mới dữ liệu ");
      }
    }
  };

  const insertCampus = async (Campus) => {
    try {
      await CampusService.insertCampus(Campus);
      getCampusAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Thêm thành công",
        type: "success",
      });
    } catch (ex) {
      if (ex.response && ex.response.status === 500)
      {
        toast("Email hoặc thông tin khác bị trùng với hệ thống");
      }else {
        toast.error("Errors: Lỗi cập nhật dữ liệu ");
      }
    }
  };

  const updateCampus = async (Campus) => {
    try {
      await CampusService.updateCampus(Campus);
      getCampusAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Cập nhật thành công",
        type: "success",
      });
    } catch (ex) {
      if (ex.response && ex.response.status === 500)
      {
        toast("Email hoặc thông tin khác bị trùng với hệ thống");
      }else {
        toast.error("Errors: Lỗi cập nhật dữ liệu ");
      }


    }
  };

  const deleteCampus = async (CampusId) => {
    const originalCampusRecord = records;
    const newCampusRecord = originalCampusRecord.filter(
      (x) => x.id !== CampusId
    );
    setRecords(newCampusRecord);
    try {
      await CampusService.deleteCampus(CampusId);
      setNotify({
        isOpen: true,
        message: "Đã xoá thành công",
        type: "error",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi xóa dữ liệu ");
      setRecords(originalCampusRecord);
    }
  };
  //===================================================================================
  useEffect(getCampusAndUpdateToState, []);
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
            x.name.toLowerCase().includes(target.value)
          );
      },
    });
  };

  const addOrEdit = (Campus, resetForm) => {
    if (Campus.id === 0) insertCampus(Campus);
    else updateCampus(Campus);
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
    deleteCampus(id);
  };

  return (
    <>
     <OftadehLayout>
     <PageHeader
        history={history}
        title="Khuôn viên"
        subTitle="Tất cả các khuôn viên mà bạn hiện đang quản lý"
        icon={<AccountBalanceIcon fontSize="large" />}
      />
      <Paper elevator={3} className={classes.pageContent}>
        <div className={classes.paper}>
          <Grid container spacing={3}>
            <Grid item sm={9}>
              <Controls.Input
                label="Tìm kiếm khuôn viên "
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
                <TableCell>{item.name}</TableCell>
                <TableCell>
                  {item.campusType === "FACILITY" ? "Cơ sở" : "Trụ sở chính"}
                </TableCell>
                <TableCell>{item.contactEmail}</TableCell>
                <TableCell>{item.contactPhone}</TableCell>
                <TableCell>{item.description}</TableCell>
                <TableCell>{item.location}</TableCell>
                <TableCell>{item.mapUrl}</TableCell>

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
                        title: "Bạn có chắc chắn xóa khuôn viên này không?",
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
        title="Biểu mẫu khuôn viên"
        openPopup={openPopup}
        setOpenPopup={setOpenPopup}
      >
        <CampusForm recordForEdit={recordForEdit} addOrEdit={addOrEdit} />
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
