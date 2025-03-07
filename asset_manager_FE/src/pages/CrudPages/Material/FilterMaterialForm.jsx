import React from "react";
import MUIDataTable from "mui-datatables";

const columns = [
  {
    name: "credentialCode",
    label: "Mã CSVC",
    options: {
      filter: true,
      sort: true,
    },
  },
  {
    name: "status",
    label: "Trạng thái",
    options: {
      filter: true,
      sort: false,
    },
  },
  {
    name: "productName",
    label: "Mã sản phẩm",
    options: {
      filter: true,
      sort: false,
    },
  },
  {
    name: "productDescription",
    label: "Tên mô tả",
    options: {
      filter: false,
      sort: false,
    },
  },
  {
    name: "priceOrigin",
    label: "Nguyên giá ",
    options: {
      filter: true,
      sort: true,
    },
  },
  {
    name: "currentValue",
    label: "Giá trị h.tại ",
    options: {
      filter: true,
      sort: true,
    },
  },


  {
    name: "productType",
    label: "Loại CSVC",
    options: {
      filter: true,
      sort: true,
    },
  },
  {
    name: "productTimeAllocationType",
    label: "Kiểu phân bổ",
    options: {
      filter: true,
      sort: true,
    },
  },
  {
    name: "currentPlaceNameSpecification",
    label: "Vị trí",
    options: {
      filter: true,
      sort: true,
    },
  },
  {
    name: "department",
    label: "Phòng ban",
    options: {
      filter: true,
      sort: true,
    },
  },
  {
    name: "timeStartDepreciation",
    label: "Bắt đầu khấu hao",
    options: {
      filter: true,
      sort: true,
    },
  },
  {
    name: "haveInclude",
    label: "Là bộ",
    options: {
      filter: true,
      sort: true,
    },
  },
  {
    name: "parentCode",
    label: "Mã Cơ Sở Vật Chất Cha",
    options: {
      filter: true,
      sort: true,
    },
  },
];

const options = {
  filterType: "checkbox",
  download: false,
  print: false,
  selectableRows: false,
  textLabels: {
    body: {
      noMatch: "Xin lỗi, không tìm thấy dữ liệu phù hợp",
      toolTip: "Sắp sếp",
      columnHeaderTooltip: (column) => `Sắp xếp cho cột ${column.label}`,
    },
    pagination: {
      next: "Trang sau",
      previous: "Trang trước",
      rowsPerPage: "Rows per page:",
      displayRows: "of",
    },
    toolbar: {
      search: "Tìm kiếm",
      downloadCsv: "Tải CSV",
      print: "In",
      viewColumns: "Xem các cột",
      filterTable: "Lọc theo tiêu chí",
    },
    filter: {
      all: "Tất cả",
      title: "Bộ lọc",
      reset: "Reset",
    },
    viewColumns: {
      title: "Hiển thị tất cả các cột",
      titleAria: "Hiện và ẩn các cột",
    },
    selectedRows: {
      text: "hàng được chọn",
      delete: "Xóa",
      deleteAria: "Xóa các hàng đã chọn",
    },
  },
};

const FilterMaterialForm = (props) => {
  const {records, convertStatus  } = props;
  const data = records.map((item) => 
  {
      return {
        credentialCode: item.credentialCode,
        status: convertStatus(item.status),
        productName: item.product.name,
        productDescription: item.product.description,
        priceOrigin: item.extendedInfo &&  item.extendedInfo.priceOrigin.toLocaleString('it-IT', {style : 'currency', currency : 'VND'}),
        currentValue: item.extendedInfo &&  item.extendedInfo.currentValue.toLocaleString('it-IT', {style : 'currency', currency : 'VND'}),
        productType: item.product.type === "ASSET" ? "TSCĐ" : "CCDC",
        productTimeAllocationType: item.product.timeAllocationType === "YEAR" ? "Năm" : "Tháng",
        currentPlaceNameSpecification: item.currentPlace && item.currentPlace.nameSpecification,
        department: item.currentPlace && item.currentPlace.department.description,
        timeStartDepreciation: item.timeStartDepreciation,
        haveInclude: item.haveInclude ? "yes" : "no",
        parentCode: item.parentCode,
      }
  });

  return (
    <div>
      <MUIDataTable
        title={"Bảng lọc cơ sở vật chất "}
        data={data}
        columns={columns}
        options={options}
      />
    </div>
  );
};

export default FilterMaterialForm;
