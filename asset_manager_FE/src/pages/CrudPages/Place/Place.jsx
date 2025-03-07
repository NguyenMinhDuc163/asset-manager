import React, { useState, useEffect } from "react";
import LocationOnIcon from "@material-ui/icons/LocationOn";
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
import PlaceForm from "./PlaceForm";
import PageHeader from "../../../oftadeh-layouts/layout/PageHeader";
import useTable from "../commons/useTable";
import Controls from "../commons/Controls";
import Popup from "../commons/Popup";
import Notification from "../commons/Notification";
import ConfirmDialog from "../commons/ConfirmDialog";
import * as placeService from "../../../services/placeService";
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
  { id: "code", label: "Code" },
  { id: "nameSpecification", label: "Tên vị trí" },
  { id: "description", label: "Mô tả chi tiết" },
  { id: "floor", label: "Tầng" },
  { id: "direction", label: "Chỉ dẫn đến" },
  { id: "typePlaceId", label: "Loại vị trí" },
  { id: "campusId", label: "Cơ sở" },
  { id: "departmentId", label: "Phòng ban" },
  { id: "actions", label: "Actions", disableSorting: true },
];

export default function Place(props) {
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
  const PlaceHandledToShow = (obj) => {
    const objConverted = obj.map((item) => {
      item.createdAt = utils.convertDateTime(item.createdAt);
      item.updatedAt = utils.convertDateTime(item.updatedAt);

      const additionalProps = {
        typePlaceId: item.typePlace.id,
        campusId: item.campus.id,
        departmentId: item.department.id,
      };
      return Object.assign(item, additionalProps);
    });
    return objConverted;
  };

  const PlaceHandledToInsert = (obj) => {
    const temp = {
      code: obj.code,
      nameSpecification: obj.nameSpecification,
      description: obj.description,
      floor: obj.floor,
      direction: obj.direction,

      embedded: {
        typePlaceId: obj.typePlaceId,
        campusId: obj.campusId,
        departmentId: obj.departmentId,
      },
    };

    return temp;
  };

  const PlaceHandledToUpdate = (obj) => {
    const temp = {
      id: obj.id,
      code: obj.code,
      nameSpecification: obj.nameSpecification,
      description: obj.description,
      floor: obj.floor,
      direction: obj.direction,

      embedded: {
        typePlaceId: obj.typePlaceId,
        campusId: obj.campusId,
        departmentId: obj.departmentId,
      },
    };

    return temp;
  };
  // ======================================================================================================

  //=======================================   XỬ LÝ CALL API    ===========================================
  const getPlaceAndUpdateToState = async () => {
    try {
      const { data: responseData } = await placeService.getAllPlace();
      const { data: Place } = responseData;
      setRecords(PlaceHandledToShow(Place));
    } catch (ex) {
      if ((ex.response && ex.response.status === 403) || (ex.response && ex.response.status === 401))
      {
        toast("Bạn không có quyền hạn truy cập trang này");
      }else {
        toast.error("Errors: Lỗi tải lên dữ liệu ");
      }
    }
  };

  const insertPlace = async (Place) => {
    try {
      await placeService.insertPlace(PlaceHandledToInsert(Place));
      getPlaceAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Thêm thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi thêm mới dữ liệu ");
    }
  };

  const updatePlace = async (Place) => {
    try {
      await placeService.updatePlace(PlaceHandledToUpdate(Place));
      getPlaceAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Cập nhật thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi cập nhật dữ liệu ");
    }
  };

  const deletePlace = async (PlaceId) => {
    const originalPlaceRecord = records;
    const newPlaceRecord = originalPlaceRecord.filter((x) => x.id !== PlaceId);
    setRecords(newPlaceRecord);
    try {
      await placeService.deletePlace(PlaceId);
      setNotify({
        isOpen: true,
        message: "Đã xoá thành công",
        type: "error",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi xóa dữ liệu ");
      setRecords(originalPlaceRecord);
    }
  };
  //===================================================================================

  useEffect(getPlaceAndUpdateToState, []);

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
        else {
          return items.filter((x) =>
            x.nameSpecification.toLowerCase().includes(target.value)
          );
        }
      },
    });
  };

  const addOrEdit = (Place, resetForm) => {
    if (Place.id === 0) insertPlace(Place);
    else updatePlace(Place);
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
    deletePlace(id);
  };

  return (
    <>
     <OftadehLayout>
     <PageHeader
        history={history}
        title="Vị trí"
        subTitle="Tất cả các vị trí mà bạn hiện đang quản lý"
        icon={<LocationOnIcon fontSize="large" />}
      />
      <Paper elevator={3} className={classes.pageContent}>
        <div className={classes.paper}>
          <Grid container spacing={3}>
            <Grid item sm={9}>
              <Controls.Input
                label="Nhập tên vị trí để tìm kiếm"
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
                <TableCell>{item.code}</TableCell>
                <TableCell>{item.nameSpecification}</TableCell>
                <TableCell>{item.description}</TableCell>
                <TableCell>{item.floor}</TableCell>
                <TableCell>{item.direction}</TableCell>
                <TableCell>{item.typePlace.name}</TableCell>
                <TableCell>{item.campus.name}</TableCell>
                <TableCell>{item.department.description}</TableCell>

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
                        title: "Bạn có chắc chắn xóa vị trí  này không?",
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
        title="Biểu mẫu vị trí"
        openPopup={openPopup}
        setOpenPopup={setOpenPopup}
      >
        <PlaceForm recordForEdit={recordForEdit} addOrEdit={addOrEdit} />
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
