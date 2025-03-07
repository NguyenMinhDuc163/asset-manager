import React, { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import Controls from "../commons/Controls";
import { useForm, Form } from "../commons/useForm";

import * as Utils from "../../../utils/Utils";

const initialFValues = {
  id: 0,
  origin: "",
  timeAllocationType: "",
  name: "",
  description: "",
  categoryId: "",
  calculationUnitId: "",
  allocationDuration: "",
  type: "",
};

const getProductCollection = () => [
  { id: "YEAR", title: "Theo năm" },
  { id: "MONTH", title: "Theo tháng" },
];

const getProductTypeCollection = () => [
  { id: "ASSET", title: "Tài sản cố định" },
  { id: "TOOL", title: "Công cụ dụng cụ" },
];

export default function ProductForm(props) {
  const { addOrEdit, recordForEdit } = props;
  const [CalculationUnits, setCalculationUnits] = useState([]);
  const [Categories, setCategories] = useState([]);

  const validate = (fieldValues = values) => {
    let temp = { ...errors };
    if ("name" in fieldValues)
    temp.name =  fieldValues.name.length > 2 &&  fieldValues.name.length < 200 ? "" : "Chiều dài trường này được giới hạn từ 2 đến 200 ký tự";

    if ("origin" in fieldValues)
      temp.origin = fieldValues.origin ? "" : "Trường này là bắt buộc.";
    if ("description" in fieldValues)
      temp.description = fieldValues.description
        ? ""
        : "Trường này là bắt buộc.";

    if ("timeAllocationType" in fieldValues)
      temp.timeAllocationType = fieldValues.timeAllocationType
        ? ""
        : "Trường này là bắt buộc.";
    if ("categoryId" in fieldValues)
      temp.categoryId = fieldValues.categoryId ? "" : "Trường này là bắt buộc.";
    if ("calculationUnitId" in fieldValues)
      temp.calculationUnitId = fieldValues.calculationUnitId
        ? ""
        : "Trường này là bắt buộc.";

    if ("type" in fieldValues)
      temp.type = fieldValues.type ? "" : "Trường này là bắt buộc.";

    if ("allocationDuration" in fieldValues)
      temp.allocationDuration = /^([0-9.])+$/.test(
        fieldValues.allocationDuration
      )
        ? ""
        : "Trường này không hợp lệ.";

    setErrors({
      ...temp,
    });

    if (fieldValues === values)
      return Object.values(temp).every((x) => x === "");
  };

  const {
    values,
    setValues,
    errors,
    setErrors,
    handleInputChange,
    resetForm,
  } = useForm(initialFValues, true, validate);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      addOrEdit(values, resetForm);
    }
  };

  useEffect(() => {
    if (recordForEdit != null)
      setValues({
        ...recordForEdit,
      });
  }, [recordForEdit, setValues]);

  useEffect(() => {
    Utils.getDataCalculationUnit().then((response) => {
      setCalculationUnits([...response]);
    });

    Utils.getDataCategory().then((response) => {
      setCategories([...response]);
    });
  }, []);

  return (
    <Form onSubmit={handleSubmit}>
      <Grid container>
        <Grid item xs={6}>
          <Controls.Input
            name="name"
            label="Nhập mã sản phảm"
            value={values.name}
            onChange={handleInputChange}
            error={errors.name}
          />
          <Controls.Input
            name="description"
            label="Tên mô tả"
            value={values.description}
            onChange={handleInputChange}
            error={errors.description}
          />

          <Controls.Input
            label="Nguồn gốc "
            name="origin"
            value={values.origin}
            onChange={handleInputChange}
            error={errors.origin}
          />

          <Controls.Input
            name="allocationDuration"
            label="Thời hạn phân bổ"
            value={values.allocationDuration}
            onChange={handleInputChange}
            error={errors.allocationDuration}
          />
        </Grid>
        <Grid item xs={6}>
          <Controls.AutoCompleteButton
            name="categoryId"
            label="Thuộc danh mục"
            value={Categories.find((item) => item.id === values.categoryId)}
            onChange={handleInputChange}
            options={Categories}
            error={errors.categoryId}
          />

          <Controls.Select
            name="timeAllocationType"
            label="Kiểu phân bổ"
            value={values.timeAllocationType}
            onChange={handleInputChange}
            options={getProductCollection()}
            error={errors.timeAllocationType}
          />
          <Controls.Select
            name="type"
            label="Kiểu sản phẩm"
            value={values.type}
            onChange={handleInputChange}
            options={getProductTypeCollection()}
            error={errors.type}
          />
          <Controls.Select
            name="calculationUnitId"
            label="Đơn vị tính"
            value={values.calculationUnitId}
            onChange={handleInputChange}
            options={CalculationUnits}
            error={errors.calculationUnitId}
          />
          <div>
            <Controls.Button type="submit" text="Submit" />
            <Controls.Button text="Reset" color="default" onClick={resetForm} />
          </div>
        </Grid>
      </Grid>
    </Form>
  );
}
