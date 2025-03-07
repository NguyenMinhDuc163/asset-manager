import React, { useEffect } from "react";
import { Grid } from "@material-ui/core";
import Controls from "../commons/Controls";
import { useForm, Form } from "../commons/useForm";

const initialFValues = {
  id: 0,
  time: new Date(),
  startTime: new Date(),
  endTime: new Date(),
};

export default function InventoryForm(props) {
  const { addOrEdit, recordForEdit } = props;

  const validate = (fieldValues = values) => {
    let temp = { ...errors };
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

  return (
    <Form onSubmit={handleSubmit}>
      <Grid container>
        <Grid item xs={6}>
          <Controls.DatePicker
            name="startTime"
            label="Thời Gian Bắt đầu"
            value={values.startTime}
            onChange={handleInputChange}
          />
          <Controls.DatePicker
            name="endTime"
            label="Thời Gian Kết Thúc"
            value={values.endTime}
            onChange={handleInputChange}
          />
        </Grid>
        <Grid item xs={6}>
          <Controls.DatePicker
            name="time"
            label="Định kỳ kiểm kê"
            value={values.time}
            onChange={handleInputChange}
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
