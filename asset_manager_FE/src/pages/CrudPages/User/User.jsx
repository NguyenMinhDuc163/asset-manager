import React, { useState, useEffect } from "react";
import PersonIcon from "@material-ui/icons/Person";
import { Search } from "@material-ui/icons";
import AddIcon from "@material-ui/icons/Add";
import EditOutlinedIcon from "@material-ui/icons/EditOutlined";
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
import UserForm from "./UserForm";
import PageHeader from "../../../oftadeh-layouts/layout/PageHeader";
import useTable from "../commons/useTable";
import Controls from "../commons/Controls";
import Popup from "../commons/Popup";
import Notification from "../commons/Notification";
import ConfirmDialog from "../commons/ConfirmDialog";
import * as userService from "../../../services/userService";
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
  { id: "acive", label: "Trạng thái" },
  { id: "username", label: "Tên tài khoản" },
  { id: "fullName", label: "Họ tên" },
  { id: "phone", label: "Số điện thoại" },
  { id: "email", label: "Email" },
  { id: "department", label: "Thuộc phòng ban" },
  { id: "actions", label: "Actions", disableSorting: true },
];

export default function User(props) {
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
  const UserHandledToShow = (obj) => {
    console.log(obj,  " obj là");
    const objConverted = obj.map((item) => {
      item.createdAt = utils.convertDateTime(item.createdAt);
      item.updatedAt = utils.convertDateTime(item.updatedAt);
      const additionalProps = {
        departmentId: item.department.id,
        password: "",
        roles: item.roles.map((e) => {
          return e.roleName;
        }),
      };
      return Object.assign(item, additionalProps);
    });
    return objConverted;
  };

  const UserHandledToInsert = (obj) => {
    const temp = {
      fullName: obj.fullName,
      phone: obj.phone,
      email: obj.email,
      username: obj.username,
      password: obj.password,
      active: obj.active,
      roles: obj.roles,
      embedded: {
        departmentId: obj.departmentId,
      },
    };
    return temp;
  };

  const UserHandledToUpdate = (obj) => {
    const temp = {
      id: obj.id,
      fullName: obj.fullName,
      phone: obj.phone,
      email: obj.email,
      username: obj.username,
      password: obj.password,
      active: obj.active,
      roles: obj.roles,
      embedded: {
        departmentId: obj.departmentId,
      },
    };
    return temp;
  };
  // ======================================================================================================

  //=======================================   XỬ LÝ CALL API    ===========================================
  const getUserAndUpdateToState = async () => {
    try {
      const { data: responseData } = await userService.getAllUser();
      const { data: User } = responseData;
      console.log(User, "user alf gì");
      setRecords(UserHandledToShow(User));
    } catch (ex) {
      console.log("chỗ ày là gì");
      if ((ex.response && ex.response.status === 403) || (ex.response && ex.response.status === 401))
      {
        toast("Bạn không có quyền hạn truy cập trang này");
      }else {
        toast.error("Errors: Lỗi tải lên dữ liệu ");
      }
    }
  };

  const insertUser = async (User) => {
    try {
      await userService.insertUser(UserHandledToInsert(User));
      getUserAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Thêm thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi thêm mới dữ liệu ");
    }
  };

  const updateUser = async (User) => {
    try {
      await userService.updateUser(UserHandledToUpdate(User));
      getUserAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Cập nhật thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi cập nhật dữ liệu ");
    }
  };

  //===================================================================================
  useEffect(getUserAndUpdateToState, []);

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
            x.username.toLowerCase().includes(target.value)
          );
      },
    });
  };

  const addOrEdit = (User, resetForm) => {
    if (User.id === 0) insertUser(User);
    else updateUser(User);
    resetForm();
    setRecordForEdit(null);
    setOpenPopup(false);
  };

  const openInPopup = (item) => {
    setRecordForEdit(item);
    setOpenPopup(true);
  };

  return (
    <>
     <OftadehLayout>
     <PageHeader
        history={history}
        title="User "
        subTitle="Tất cả các user or nhân viên  mà bạn hiện đang quản lý"
        icon={<PersonIcon fontSize="large" />}
      />
      <Paper elevator={3} className={classes.pageContent}>
        <div className={classes.paper}>
          <Grid container spacing={3}>
            <Grid item sm={9}>
              <Controls.Input
                label="Nhập thông tin để tìm kiếm"
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
                <TableCell>{item.active ? "Hoạt động" : "Khóa"}</TableCell>
                <TableCell>{item.username}</TableCell>
                <TableCell>{item.fullName}</TableCell>
                <TableCell>{item.phone}</TableCell>
                <TableCell>{item.email}</TableCell>

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
                </TableCell>
              </StyledTableRow>
            ))}
          </TableBody>
        </TblContainer>
        <TblPagination />
      </Paper>
      <Popup
        title="Biểu mẫu user"
        openPopup={openPopup}
        setOpenPopup={setOpenPopup}
      >
        <UserForm recordForEdit={recordForEdit} addOrEdit={addOrEdit} />
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
