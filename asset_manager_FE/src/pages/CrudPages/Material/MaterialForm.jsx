import React, { useEffect, useState } from "react";
import { Grid } from "@material-ui/core";
import Controls from "../commons/Controls";
import { useForm, Form } from "../commons/useForm";
import * as Utils from "../../../utils/Utils";

const initialFValues = {
  id: 0,
  credentialCode: "",
  status: "",
  timeStartDepreciation: new Date(),
  haveInclude: false,
  parentCode: null,
  productId: "",
  additionalId: "",
  placeId: "",
  userId: "",
};

export default function MaterialForm(props) {
  const { addOrEdit, recordForEdit } = props;
  const [DataProduct, setDataProduct] = useState([]);
  const [DataAdditional, setDataAdditional] = useState([]);
  const [DataUser, setDataUser] = useState([]);
  const [DataPlace, setDataPlace] = useState([]);
  const [DataCredentialCode, setDataCredentialCode] = useState([]);
  const getMaterialStatus = [
    { id: "UN_USED", title: "Không Sử Dụng" },
    { id: "IN_USED", title: "Đang Sử Dụng" },
    { id: "DAMAGED", title: "Bị Hư Hại" },
    { id: "REQUEST_REPAIR", title: "Yêu Cầu Sửa Chữa" },
    { id: "REQUEST_LIQUIDATE", title: "Yêu Cầu Thanh Lý" },
    { id: "NO_LONGER", title: "Không Còn" },
  ];

  const validate = (fieldValues = values) => {
    let temp = { ...errors };
    if ("credentialCode" in fieldValues)
      temp.credentialCode = fieldValues.credentialCode ? "" : "Trường này là bắt buộc.";

      if ("placeId" in fieldValues)
      temp.placeId = fieldValues.placeId ? "" : "Trường này là bắt buộc.";

      if ("status" in fieldValues)
      temp.status = fieldValues.status ? "" : "Trường này là bắt buộc.";

      if ("productId" in fieldValues)
      temp.productId = fieldValues.productId ? "" : "Trường này là bắt buộc.";

      if ("userId" in fieldValues)
      temp.userId = fieldValues.userId ? "" : "Trường này là bắt buộc.";

      if ("additionalId" in fieldValues)
      temp.additionalId = fieldValues.additionalId ? "" : "Trường này là bắt buộc.";

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
      // xử lý trường hợp đặc biệt  - trường hơp là bộ
      if (values.haveInclude)
      {
        // nếu là bộ thì phải xóa hết credentialCode 
        const tempValues = {...values,  parentCode : null};
        addOrEdit(tempValues, resetForm);
      }else
      {
        addOrEdit(values, resetForm);
      }
 
    }
  };

  useEffect(() => {
    if (recordForEdit != null)
      setValues({
        ...recordForEdit,
      });
  }, [recordForEdit, setValues]);
  useEffect(() => {
    Utils.getDataProduct().then((response) => {
      setDataProduct([...response]);
    });

    Utils.getDataAdditional().then((response) => {
      setDataAdditional([...response]);
    });
    Utils.getDataUser().then((response) => {
      setDataUser([...response]);
    });
    Utils.getDataPlace().then((response) => {
      setDataPlace([...response]);
    });
    Utils.getDataCredentialCodes().then((response) => {
      setDataCredentialCode([...response]);
    });
  }, []);
  return (
    <Form onSubmit={handleSubmit}>
      <Grid container>
        <Grid item xs={6}>
          <Controls.Input
            name="credentialCode"
            label="Mã CSVC"
            value={values.credentialCode}
            onChange={handleInputChange}
            error={errors.credentialCode}
          />
           <Controls.Checkbox
            label="Là Bộ (Là CSVC cha)"
            name="haveInclude"
            value={values.haveInclude}
            onChange={handleInputChange}
          />
          <Controls.AutoCompleteButton
            disabled = {values.haveInclude}
            name="parentCode"
            label="Mã Cơ Sở Vật Chất Cha"
            value={DataCredentialCode.find(
              (item) => item.id === values.parentCode
            )}
            onChange={handleInputChange}
            options={DataCredentialCode}
            error={errors.parentCode}
          />


          <Controls.AutoCompleteButton
            name="additionalId"
            label="Chọn đợt bổ sung"
            value={DataAdditional.find(
              (item) => item.id === values.additionalId
            )}
            onChange={handleInputChange}
            options={DataAdditional}
            error={errors.additionalId}
          />

          <Controls.AutoCompleteButton
            name="userId"
            label="Người thêm"
            value={DataUser.find((item) => item.id === values.userId)}
            onChange={handleInputChange}
            options={DataUser}
            error={errors.userId}
          />
         
        </Grid>
        <Grid item xs={6}>
          <Controls.AutoCompleteButton
            disabled = {false}
            name="placeId"
            label="Vị trí"
            value={DataPlace.find((item) => item.id === values.placeId)}
            onChange={handleInputChange}
            options={DataPlace}
            error={errors.placeId}
          />
          <Controls.DatePicker
            name="timeStartDepreciation"
            label=" Thời gian bắt đầu khấu hao"
            value={values.timeStartDepreciation}
            onChange={handleInputChange}
          />
          <Controls.Select
            name="status"
            label="Trạng thái"
            value={values.status}
            onChange={handleInputChange}
            options={getMaterialStatus}
            error={errors.status}
          />
          <Controls.AutoCompleteButton
            name="productId"
            label="Chọn sản phẩm"
            value={DataProduct.find((item) => item.id === values.productId)}
            onChange={handleInputChange}
            options={DataProduct}
            error={errors.productId}
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
