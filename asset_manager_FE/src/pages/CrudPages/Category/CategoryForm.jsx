import React, { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import Controls from "../commons/Controls";
import { useForm, Form } from "../commons/useForm";

import * as Utils from "../../../utils/Utils";

const initialFValues = {
  id: 0,
  name: "",
  description: "",
  groupId: 0,
};

export default function CategoryForm(props) {
  const { addOrEdit, recordForEdit } = props;
  const [Groups, setGroups] = useState([]);

  const validate = (fieldValues = values) => {
    let temp = { ...errors };
    if ("name" in fieldValues)
      temp.name = fieldValues.name ? "" : "Trường này là bắt buộc.";

    if ("description" in fieldValues)
      temp.description = fieldValues.description
        ? ""
        : "Trường này là bắt buộc.";
    if ("groupId" in fieldValues)
      temp.groupId = fieldValues.groupId ? "" : "Trường này là bắt buộc.";

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
    Utils.getDataGroup().then((response) => {
      setGroups([...response]);
    });
  }, []);

  return (
    <Form onSubmit={handleSubmit}>
      <Grid container>
        <Grid item xs={6}>
          <Controls.Input
            name="name"
            label="Nhập mã danh mục"
            value={values.name}
            onChange={handleInputChange}
            error={errors.name}
          />
          <Controls.Select
            name="groupId"
            label="Thuộc nhóm"
            value={values.groupId}
            onChange={handleInputChange}
            options={Groups}
            error={errors.name}
          />
        </Grid>
        <Grid item xs={6}>
          <Controls.Input
            label="Nhập mô tả chi tiết"
            name="description"
            value={values.description}
            onChange={handleInputChange}
            error={errors.description}
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
