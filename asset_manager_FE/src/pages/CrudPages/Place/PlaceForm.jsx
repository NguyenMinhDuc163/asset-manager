import React, { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import Controls from "../commons/Controls";
import { useForm, Form } from "../commons/useForm";

import * as Utils from "../../../utils/Utils";

const initialFValues = {
  id: 0,
  code: "",
  nameSpecification: "",
  description: "",
  floor: "",
  direction: "",
  typePlaceId: "",
  campusId: "",
  departmentId: "",
};

export default function PlaceForm(props) {
  const { addOrEdit, recordForEdit } = props;
  const [Departments, setDepartments] = useState([]);
  const [TypePlaces, setTypePlaces] = useState([]);
  const [Campus, setCampus] = useState([]);
  const validate = (fieldValues = values) => {
    let temp = { ...errors };

    if ("code" in fieldValues) {
      temp.code = fieldValues.code ? "" : "Trường dữ liệu không được để trống.";
    }
    if ("nameSpecification" in fieldValues) {
      temp.nameSpecification = fieldValues.nameSpecification
        ? ""
        : "Trường dữ liệu không được để trống.";
    }
    if ("description" in fieldValues) {
      temp.description = fieldValues.description
        ? ""
        : "Trường dữ liệu không được để trống.";
    }

    if ("typePlaceId" in fieldValues) {
      temp.typePlaceId = fieldValues.typePlaceId
        ? ""
        : "Trường dữ liệu không được để trống.";
    }
    if ("floor" in fieldValues)
      temp.floor = /^([0-9])+$/.test(fieldValues.floor)
        ? ""
        : "Trường này không hợp lệ.";
    if ("departmentId" in fieldValues)
      temp.departmentId =
        fieldValues.departmentId.length !== 0
          ? ""
          : "Trường này không được để trống.";

    if ("campusId" in fieldValues)
      temp.campusId =
        fieldValues.campusId.length !== 0
          ? ""
          : "Trường này không được để trống.";

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
    Utils.getDataDepartment().then((response) => {
      setDepartments([...response]);
    });

    Utils.getDataCampus().then((response) => {
      setCampus([...response]);
    });
    Utils.getDataTypePlace().then((response) => {
      setTypePlaces([...response]);
    });
  }, []);

  return (
    <Form onSubmit={handleSubmit}>
      <Grid container>
        <Grid item xs={6}>
          <Controls.Input
            name="code"
            label="Code"
            value={values.code}
            onChange={handleInputChange}
            error={errors.code}
          />
          <Controls.Input
            label="Địa chỉ"
            name="nameSpecification"
            value={values.nameSpecification}
            onChange={handleInputChange}
            error={errors.nameSpecification}
          />
          <Controls.Input
            label="Mô tả chi tiết"
            name="description"
            value={values.description}
            onChange={handleInputChange}
            error={errors.description}
          />
          <Controls.Input
            label="Tầng"
            name="floor"
            value={values.floor}
            onChange={handleInputChange}
            error={errors.floor}
          />
        </Grid>
        <Grid item xs={6}>
          <Controls.Input
            label="Chỉ dẫn đến"
            name="direction"
            value={values.direction}
            onChange={handleInputChange}
            error={errors.direction}
          />
          <Controls.Select
            name="departmentId"
            label="Phòng ban"
            value={values.departmentId}
            onChange={handleInputChange}
            options={Departments}
            error={errors.departmentId}
          />
          <Controls.Select
            name="campusId"
            label="Cơ sở"
            value={values.campusId}
            onChange={handleInputChange}
            options={Campus}
            error={errors.campusId}
          />
          <Controls.Select
            name="typePlaceId"
            label="Loại vị trí"
            value={values.typePlaceId}
            onChange={handleInputChange}
            options={TypePlaces}
            error={errors.typePlaceId}
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
