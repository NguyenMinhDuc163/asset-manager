import React, { useState, useEffect } from "react";
import RestoreIcon from '@material-ui/icons/Restore';
import { Search } from "@material-ui/icons";
import AutorenewIcon from "@material-ui/icons/Autorenew";
import Grid from "@material-ui/core/Grid";
import SettingsBackupRestoreIcon from '@material-ui/icons/SettingsBackupRestore';
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
import PageHeader from "../../../oftadeh-layouts/layout/PageHeader";
import useTable from "../commons/useTable";
import Controls from "../commons/Controls";
import Notification from "../commons/Notification";
import ConfirmDialog from "../commons/ConfirmDialog";
import * as BackupService from "../../../services/backupService";

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
  { id: "position", label: "Phiên bản" },
  { id: "backup_start_date", label: "Thời gian bắt đầu" },
  { id: "backup_finish_date", label: "Thời gian kết thúc" },
  { id: "Actions", label: "Restore", disableSorting: true },
];

export default function Backup(props) {
  const { history } = props;
  const classes = useStyles();
  const [records, setRecords] = useState([]);
  const [filterFn, setFilterFn] = useState({
    fn: (items) => {
      return items;
    },
  });
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
  const BackupHandled = (object) => {
    const objectConverted = object.map((item) => {
      return item;
    });
    return objectConverted;
  };

  //=======================================   XỬ LÝ CALL API    ===========================================
  const getBackupAndUpdateToState = async () => {
    try {
      const { data: responseData } = await BackupService.getAllVersionBackup();
      const { data: Backup } = responseData;
      setRecords(BackupHandled(Backup));
    } catch (ex) {
      if ((ex.response && ex.response.status === 403) || (ex.response && ex.response.status === 401))
      {
        toast("Bạn không có quyền hạn truy cập trang này");
      }else {
        toast.error("Errors: Lỗi thêm mới dữ liệu ");
      }
    }
  };

  const Backup = async (BackupId) => {
    const originalBackupRecord = records;
    const newBackupRecord = originalBackupRecord.filter(
      (x) => x.position !== BackupId
    );
    setRecords(newBackupRecord);
    try {
      await BackupService.Backup(BackupId);
      setNotify({
        isOpen: true, 
        message: "Thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: không thể restore");
      setRecords(originalBackupRecord);
    }
  };

  const Restore = async () => {
    try {
      await BackupService.Restore();
      getBackupAndUpdateToState();
      setNotify({
        isOpen: true, 
        message: "Backup thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: không thể sao lưu");
    
    }
  };

  //===================================================================================

  useEffect(getBackupAndUpdateToState, []);

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
            x.position.toString().toLowerCase().includes(target.value)
          );
      },
    });
  };

  const onBackup = (position) => {
    setConfirmDialog({
      ...confirmDialog,
      isOpen: false,
    });
    Backup(position);
  };

  const handleRestoreData = () => {
    if (window.confirm("Bạn có chắc chắn muốn sao lưu dữ liệu hệ thống!")) {
      Restore();
    } 
  }
  return (
    <>
      <OftadehLayout>
        <PageHeader
          history={history}
          title="Backup"
          subTitle="Tất cả các phiên bản backup"
          icon={<RestoreIcon fontSize="large" />}
        />
        <Paper elevator={3} className={classes.pageContent}>
          <div className={classes.paper}>
            <Grid container spacing={3}>
              <Grid item sm={10}>
                <Controls.Input
                  label="Tìm kiếm phiên bản backup "
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
              <Grid item sm={2}>
                 <Controls.Button
                text="Sao lưu"
                variant="outlined"
                startIcon={<SettingsBackupRestoreIcon />}
                className={classes.newButton}
                onClick={() => {
                 // chỗ này call api nè
                 handleRestoreData();
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
                  <TableCell>{item.position}</TableCell>
                  <TableCell>{item.backup_start_date}</TableCell>
                  <TableCell>{item.backup_finish_date}</TableCell>
                  <TableCell>
                    <Controls.ActionButton
                      color="secondary"
                      onClick={() => {
                        setConfirmDialog({
                          isOpen: true,
                          title: "Bạn có chắc chắn về hành động của mình không?",
                          subTitle: "Bạn không thể hoàn tác thao tác này",
                          onConfirm: () => {
                            onBackup(item.position);
                          },
                        });
                      }}
                    >
                      <AutorenewIcon fontSize="small" />
                    </Controls.ActionButton>
                  </TableCell>
                </StyledTableRow>
              ))}
            </TableBody>
          </TblContainer>
          <TblPagination />
        </Paper>

        <Notification notify={notify} setNotify={setNotify} />
        <ConfirmDialog
          confirmDialog={confirmDialog}
          setConfirmDialog={setConfirmDialog}
        />
      </OftadehLayout>
    </>
  );
}
