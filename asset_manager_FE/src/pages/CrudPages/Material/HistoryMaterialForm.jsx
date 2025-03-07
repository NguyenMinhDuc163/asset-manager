import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";
import {
  Paper,
  makeStyles,
  withStyles,
  TableBody,
  TableRow,
  TableCell,
} from "@material-ui/core";
import useTable from "../commons/useTable";
import * as materialService from "../../../services/materialService";
import moment from "moment";
const useStyles = makeStyles((theme) => ({
  pageContent: {
    marginBottom: theme.spacing(5),
    padding: theme.spacing(3),
  },
  pageTitle: {
    padding: theme.spacing(1),
    marginBottom: theme.spacing(1),
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
  { id: "timeAction", label: "Thời gian điều chuyển" },
  { id: "placeFrom", label: "Vị trí ban đầu" },
  { id: "placeTarget", label: "Vị trí chuyển đến" },
  { id: "reason", label: "Lý do điều chuyển" },
];

export default function Backup(props) {
  const {inforHistory} = props;
  const classes = useStyles();
  const [records, setRecords] = useState([]);

  // ======================================    XỬ LÝ DATA FROM SERVER ====================================
  const historyMaterialHandled = (object) => {
    const objectConverted = object.map((item) => {
      return {
      timeAction : moment(item.timeAction, "YYYY-MM-DD hh:mm:ss A").format("DD-MM-YYYY hh:mm:ss A"),
      placeFrom : item.placeFrom,
      placeTarget : item.placeTarget,
      reason : item.reason,
      };
    });
    return objectConverted;
  };

  //=======================================   XỬ LÝ CALL API    ===========================================
  const getHistoryMaterialAndUpdateToState = async () => {

    try {
      const { data: responseData } = await materialService.getHistoryMaterial(inforHistory.id);
      const { data: history } = responseData;
      setRecords(historyMaterialHandled(history));
    } catch (ex) {
      toast.error("Errors: Lỗi lấy dữ liệu ");
    }
  };

  //===================================================================================
  useEffect(getHistoryMaterialAndUpdateToState, []);

  const {
    TblContainer,
    TblHead,
    TblPagination,
  } = useTable(records, headCells);

  return (
    <>
        <Paper elevator={3} className={classes.pageContent}>
          <TblContainer>
            <TblHead />
            <TableBody>
              {records.map((item) => (
                <StyledTableRow key={item.id}>
                  <TableCell>{item.timeAction}</TableCell>
                  <TableCell>{item.placeFrom || item.placeTarget}</TableCell>
                  <TableCell>{item.placeTarget}</TableCell>
                  <TableCell>{item.reason}</TableCell>
                </StyledTableRow>
              ))}
            </TableBody>
          </TblContainer>
          <TblPagination />
        </Paper>
    </>
  );
}
