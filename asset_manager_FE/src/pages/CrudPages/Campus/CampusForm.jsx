import React, { useEffect } from "react";
import { Grid } from "@material-ui/core";
import Controls from "../commons/Controls";
import { useForm, Form } from "../commons/useForm";

const initialFValues = {
  id: 0,
  campusType: "",
  contactEmail: "",
  contactPhone: "",
  description: "",
  location: "",
  mapUrl: "",
  name: "",
  createdAt: new Date(),
  updatedAt: new Date(),
};
const getCampusCollection = () => [
  { id: "FACILITY", title: "Cơ sở" },
  { id: "HEADQUARTERS", title: "Trụ sở chính" },
];
export default function CampusForm(props) {
  const { addOrEdit, recordForEdit } = props;

  const validate = (fieldValues = values) => {
    let temp = { ...errors };
    if ("name" in fieldValues)
      temp.name = fieldValues.name.length > 2 &&  fieldValues.name.length < 15 ? "" : "Chiều dài trường này được giới hạn từ 2 đến 15 ký tự";
    if ("description" in fieldValues)
      temp.description = fieldValues.description
        ? ""
        : "Trường này là bắt buộc.";

    if ("contactPhone" in fieldValues)
      temp.contactPhone = /((05[5|8|9]|08[1|2|3|4|5|86|9]|03[2|3|4|5|6|7|8|9]|07[0|9|7|6|8]|09[0|2|1|4|3|6|7|8|9]|01[2|9])+([0-9]{7,8})\b)/g.test(
        fieldValues.contactPhone
      )
        ? ""
        : "Số điện thoại không hợp lệ.";

    if ("contactEmail" in fieldValues)
      temp.contactEmail = /^([A-Za-z0-9_\-.])+@([A-Za-z0-9_\-.])+\.([A-Za-z]{2,4})$/.test(
        fieldValues.contactEmail
      )
        ? ""
        : "Kiểm tra lại email";
    if ("campusType" in fieldValues)
      temp.campusType = fieldValues.campusType
        ? ""
        : "Cơ sở hoặc trụ sở chính ";
    if ("location" in fieldValues)
      temp.location = fieldValues.location ? "" : "Trường này là bắt buộc.";

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
          <Controls.Input
            name="name"
            label="Nhập mã khuôn viên"
            value={values.name}
            onChange={handleInputChange}
            error={errors.name}
          />
          <Controls.Input
            name="description"
            label="Nhập mô tả chi tiết"
            value={values.description}
            onChange={handleInputChange}
            error={errors.description}
          />

          <Controls.Select
            name="campusType"
            label="Loại khuôn viên"
            value={values.campusType}
            onChange={handleInputChange}
            options={getCampusCollection()}
            error={errors.campusType}
          />
          <Controls.Input
            name="mapUrl"
            label="Nhập URL Map"
            value={values.mapUrl}
            onChange={handleInputChange}
            error={errors.mapUrl}
          />
        </Grid>
        <Grid item xs={6}>
          <Controls.Input
            label="Nhập số điện thoại"
            name="contactPhone"
            value={values.contactPhone}
            onChange={handleInputChange}
            error={errors.contactPhone}
          />
          <Controls.Input
            label="Nhập Email"
            name="contactEmail"
            value={values.contactEmail}
            onChange={handleInputChange}
            error={errors.contactEmail}
          />
          <Controls.Input
            label="Nhập địa chỉ"
            name="location"
            value={values.location}
            onChange={handleInputChange}
            error={errors.location}
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
