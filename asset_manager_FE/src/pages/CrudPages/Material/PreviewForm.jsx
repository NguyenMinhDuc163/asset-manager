import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {  withStyles } from "@material-ui/core";
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Delete';
import moment from "moment";
const useStyles = makeStyles({
  table: {
     minWidth: 400,
  },
  delete: {
    padding: 0,
  },
  container: {
    maxHeight: 400,
  },
});


const StyledTableRow = withStyles((theme) => ({
  root: {
    "&:nth-of-type(even)": {
      backgroundColor: theme.palette.action.selected,
    },
  },
}))(TableRow);


export default function DenseTable(props) {
  const{ values , onDelete, dataPlace } = props;
  const classes = useStyles();

  return (
    <TableContainer className={classes.container} component={Paper}>
      <Table className={classes.table} size="small" aria-label="a dense table">
        <TableHead>
          <TableRow>
            <TableCell>Mã CSVC</TableCell>
            <TableCell >Nơi phân bổ</TableCell>
            <TableCell >Thời gian</TableCell>
            <TableCell >Xóa</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {values.map((row) => (
              <StyledTableRow key={row.credential}>
              <TableCell component="th" scope="row">
                {row.credential}
              </TableCell>   
              <TableCell >{dataPlace.find((item) => item.id === row.placeId).title}</TableCell>
              <TableCell >{moment(row.timeStartDepreciation.toString()).format("DD-MM-YYYY hh:mm:ss A")}</TableCell>
              <TableCell>
              <IconButton 
              className={classes.delete}
              edge="end"
              aria-label="delete"
              onClick={() => onDelete(row)}
              >
                    <DeleteIcon />
              </IconButton>
              </TableCell>
              </StyledTableRow >
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
