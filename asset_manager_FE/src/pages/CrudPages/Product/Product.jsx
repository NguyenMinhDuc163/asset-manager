import React, { useState, useEffect } from "react";
import MarkunreadMailboxIcon from "@material-ui/icons/MarkunreadMailbox";
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
import ProductForm from "./ProductForm";
import PageHeader from "../../../oftadeh-layouts/layout/PageHeader";
import useTable from "../commons/useTable";
import Controls from "../commons/Controls";
import Popup from "../commons/Popup";
import Notification from "../commons/Notification";
import ConfirmDialog from "../commons/ConfirmDialog";
import * as ProductService from "../../../services/productService";
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
  { id: "name", label: "Mã sản phẩm" },
  { id: "description", label: "Tên mô tả " },
  { id: "origin", label: "Xuất xứ" },
  { id: "type", label: "Kiểu sản phẩm" },
  { id: "timeAllocationType", label: "Kiểu phân bổ" },
  { id: "allocationDuration", label: "Thời hạn phân bổ" },
  { id: "depreciationRate", label: "Tỉ lệ khấu hao" },
  { id: "category.description", label: "Thuộc danh mục", disableSorting: true },
  { id: "calculationUnit.unit", label: "Đ.v tính", disableSorting: true },
  { id: "actions", label: "Actions", disableSorting: true },
];

export default function Product(props) {
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
  const ProductHandledToShow = (obj) => {
    const objConverted = obj.map((item) => {
      item.createdAt = utils.convertDateTime(item.createdAt);
      item.updatedAt = utils.convertDateTime(item.updatedAt);
      const aditionalProps = {
        categoryId: item.category.id,
        calculationUnitId: item.calculationUnit.id,
      };

      return Object.assign(item, aditionalProps);
    });
    return objConverted;
  };

  const ProductHandledToInsert = (obj) => {
    const temp = {
      name: obj.name,
      description: obj.description,
      origin: obj.origin,
      type: obj.type,
      timeAllocationType: obj.timeAllocationType,
      allocationDuration: obj.allocationDuration,
      embedded: {
        categoryId: obj.categoryId,
        calculationUnitId: obj.calculationUnitId,
      },
    };
    return temp;
  };

  const ProductHandledToUpdate = (obj) => {
    const temp = {
      id: obj.id,
      name: obj.name,
      description: obj.description,
      origin: obj.origin,
      type: obj.type,
      timeAllocationType: obj.timeAllocationType,
      allocationDuration: obj.allocationDuration,
      embedded: {
        categoryId: obj.categoryId,
        calculationUnitId: obj.calculationUnitId,
      },
    };
    return temp;
  };
  //=======================================   XỬ LÝ CALL API    ===========================================
  const getProductAndUpdateToState = async () => {
    try {
      const { data: responseData } = await ProductService.getAllProduct();
      const { data: Product } = responseData;
      setRecords(ProductHandledToShow(Product));
    } catch (ex) {
      if ((ex.response && ex.response.status === 403) || (ex.response && ex.response.status === 401))
      {
        toast("Bạn không có quyền hạn truy cập trang này");
      }else {
        toast.error("Errors: Lỗi tải lên dữ liệu ");
      }
    }
  };

  const insertProduct = async (Product) => {
    try {
      await ProductService.insertProduct(ProductHandledToInsert(Product));
      getProductAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Thêm thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi thêm mới dữ liệu ");
    }
  };

  const updateProduct = async (Product) => {
    try {
      await ProductService.updateProduct(ProductHandledToUpdate(Product));
      getProductAndUpdateToState();
      setNotify({
        isOpen: true,
        message: "Cập nhật thành công",
        type: "success",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi cập nhật dữ liệu ");
    }
  };

  const deleteProduct = async (ProductId) => {
    const originalProductRecord = records;
    const newProductRecord = originalProductRecord.filter(
      (x) => x.id !== ProductId
    );
    setRecords(newProductRecord);
    try {
      await ProductService.deleteProduct(ProductId);
      setNotify({
        isOpen: true,
        message: "Đã xoá thành công",
        type: "error",
      });
    } catch (ex) {
      toast.error("Errors: Lỗi xóa dữ liệu ");
      setRecords(originalProductRecord);
    }
  };
  //===================================================================================

  useEffect(getProductAndUpdateToState, []);

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

  const addOrEdit = (Product, resetForm) => {
    if (Product.id === 0) insertProduct(Product);
    else updateProduct(Product);
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
    deleteProduct(id);
  };

  return (
    <>
     <OftadehLayout>
     <PageHeader
        history={history}
        title="Sản phẩm"
        subTitle="Tất cả các sản phẩm mà hệ thống đang quản lý"
        icon={<MarkunreadMailboxIcon fontSize="large" />}
      />
      <Paper elevator={3} className={classes.pageContent}>
        <div className={classes.paper}>
          <Grid container spacing={3}>
            <Grid item sm={9}>
              <Controls.Input
                label="Tìm kiếm sản phẩm"
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
                <TableCell>{item.description}</TableCell>
                <TableCell>
                  {item.origin === null ? "null" : item.origin}
                </TableCell>
                <TableCell>{item.type === "ASSET" ? "TSCĐ" : "CCDC"}</TableCell>
                <TableCell>
                  {item.timeAllocationType === "YEAR" ? "Năm" : "Tháng"}
                </TableCell>
                <TableCell>{item.allocationDuration}</TableCell>
                <TableCell>{(Math.round(item.depreciationRate * 100) / 100).toFixed(2)}</TableCell>
                <TableCell>{item.category.description}</TableCell>
                <TableCell>{item.calculationUnit.name}</TableCell>

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
                        title: "Bạn có chắc chắn xóa sản phẩm này không?",
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
        title="Biểu mẫu sản phẩm"
        openPopup={openPopup}
        setOpenPopup={setOpenPopup}
      >
        <ProductForm recordForEdit={recordForEdit} addOrEdit={addOrEdit} />
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
