import React, { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import Controls from "../commons/Controls";
import { useForm, Form } from "../commons/useForm";
import * as Utils from "../../../utils/Utils";

const initialFValues = {
  id: 0,
  time: new Date(),
  userId: "",
};

export default function LiquidateForm(props) {
  const { addOrEdit, recordForEdit } = props;
  const [Users, setUsers] = useState([]);

  const validate = (fieldValues = values) => {
    let temp = { ...errors };
    if ("userId" in fieldValues)
      temp.userId =
        fieldValues.userId ? "" : "Trường này không được để trống.";
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
    Utils.getDataUser().then((response) => {
      setUsers([...response]);
    });
  }, []);
  return (
    <Form onSubmit={handleSubmit}>
      <Grid container>
        <Grid item xs={6}>
        <Controls.DatePicker
            name="time"
            label="Thời gian thanh lý"
            value={values.time}
            onChange={handleInputChange}
          />
        </Grid>
        <Grid item xs={6}>
          <Controls.AutoCompleteButton
            name="userId"
            label="Người thanh lý"
            value={Users.find((item) => item.id === values.userId)}
            onChange={handleInputChange}
            options={Users}
            error={errors.userId}
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
