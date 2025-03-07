import React, { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import Controls from "../commons/Controls";
import { useForm, Form } from "../commons/useForm";

import * as Utils from "../../../utils/Utils";

const initialFValues = {
  id: 0,
  time: new Date(),
  userId: "",
  organizationId: "",
};

export default function AdditionalForm(props) {
  const { addOrEdit, recordForEdit } = props;
  const [Users, setUsers] = useState([]);
  const [Organizations, setOrganizations] = useState([]);
  const [updatedFlag, setUpdatedFlag] = useState(false);
  const validate = (fieldValues = values) => {
    let temp = { ...errors };

    if ("userId" in fieldValues)
      temp.userId = fieldValues.userId ? "" : "Trường này là bắt buộc.";

    if ("organizationId" in fieldValues)
      temp.organizationId = fieldValues.organizationId
        ? ""
        : "Trường này là bắt buộc.";

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
    if (recordForEdit != null) {
      setUpdatedFlag(true);
      setValues({
        ...recordForEdit,
      });
    }
  }, [recordForEdit, setValues]);

  useEffect(() => {
    Utils.getDataOrganization().then((response) => {
      setOrganizations([...response]);
    });
    Utils.getDataUser().then((response) => {
      setUsers([...response]);
    });
  }, []);

  return (
    <Form onSubmit={handleSubmit}>
      <Grid container>
        <Grid item xs={6}>
          <Controls.AutoCompleteButton
            name="userId"
            label="Người bổ sung"
            value={Users.find((item) => item.id === values.userId)}
            onChange={handleInputChange}
            options={Users}
            error={errors.userId}
            disabled = {updatedFlag}
          />
          <Controls.DatePicker
            name="time"
            label="Nhập thời gian đợt bổ sung"
            value={values.time}
            onChange={handleInputChange}
            error={errors.time}
          />
        </Grid>
        <Grid item xs={6}>
          <Controls.AutoCompleteButton
            name="organizationId"
            label="Thuộc tổ chức nào"
            value={Organizations.find(
              (item) => item.id === values.organizationId
            )}
            onChange={handleInputChange}
            options={Organizations}
            error={errors.organizationId}
            disabled = {updatedFlag}
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
