import React, { useState, useEffect } from "react";
import { Grid } from "@material-ui/core";
import Controls from "../commons/Controls";
import {  Form } from "../commons/useForm";
import PreviewForm from "./PreviewForm";
import SaveIcon from '@material-ui/icons/Save';
import Icon from '@material-ui/core/Icon';
import * as Utils from "../../../utils/Utils";

const initialFValues = {
    additionalId: "",
    productId: "",
    price: "",
    timeStartDepreciation:  new Date(),
    placeId: "",
    listMaterialCode: [],
};

export default function AddNewAdditionalProductForm(props) {
  const { addNewAdditionalProduct } = props;
  
  const [values, setValues] = useState(initialFValues);
  const [valuesPreView, setValuesPreView] = useState([]);
  const [resetMulInput, setResetMulInput] = useState(false);
  const [errors, setErrors] = useState({});
  const [disableInput, setDisableInput] = useState(false);

  const [DataAdditional, setDataAdditional] = useState([]);
  const [DataProduct, setDataProduct] = useState([]);
  const [DataCredentialCode, setDataCredentialCode] = useState([]);
  const [DataPlace, setDataPlace] = useState([]);

  const [isEmptyMultiInput, setIsEmptyMultiInput] = useState(false);

  const validate = (fieldValues = values) => {
    let temp = { ...errors };
      if ("productId" in fieldValues)
      temp.productId = fieldValues.productId ? "" : "Trường này là bắt buộc.";
      
      if ("additionalId" in fieldValues)
      temp.additionalId = fieldValues.additionalId ? "" : "Trường này là bắt buộc.";

      if ("placeId" in fieldValues)
      temp.placeId = fieldValues.placeId ? "" : "Trường này là bắt buộc.";

      if ("price" in fieldValues) {
        temp.price = /^([0-9.])+$/.test(
          fieldValues.price
        )
          ? ""
          : "Trường này không hợp lệ.";

        // check trường hợp là tài sản hay là công cụ dụng cụ
        if(/^([0-9.])+$/.test(fieldValues.price) && fieldValues.productId)
        {
          const product = DataProduct.find((item) => item.id === fieldValues.productId )
          if (parseFloat(fieldValues.price) > 30000000.0 &&  product.type === "TOOL")
          {
            temp.price = "Sản phẩm thuộc kiểu CCDC, giá phải nhỏ hơn 30tr";
          }
          else if (parseFloat(fieldValues.price) < 30000000.0 &&  product.type === "ASSET")
          {
              temp.price = "Sản phẩm thuộc kiểu tài sản cố định, giá phải lớn hơn 30tr";
          }
        }
 
      }
       
    setErrors({
      ...temp,
    });

    if (fieldValues === values)
      return Object.values(temp).every((x) => x === "");
  };

  // fake : dùng để fix tự động gọi onSubmit event khi nhấn enter trong chip input component
  const handleSubmit = (e) => {
    e.preventDefault();
  };

  const validateMultiInput = () =>
  {
    // trường hợp rỗng
    if (!values.listMaterialCode.toString())
    {
      setIsEmptyMultiInput(true);
      return false;
    }
    else
    {
      setIsEmptyMultiInput(false);
      return true;
    }
  }
  // đây mới thực sự gọi là hàm xử lý sự kiện submit
  const handleSubmitOnClick =  (e) => {
    e.preventDefault();
    if (window.confirm("Mời bạn hãy xem kỹ lại danh sách một lần nữa trước khi lưu vào hệ thống!")) {
      addNewAdditionalProduct(ObjecAdditionalProduct(values,valuesPreView ));
    }
  };

  const handleInputChange = e => {
    const { name, value } = e.target;
    setValues(prevValues => ({...prevValues, [name]: value})); 
    validate({ [name]: value });
  }

  const ObjecAdditionalProduct = (values, valuesPreView) => {
   const temp = {
      embedded : {
      additionalId :values.additionalId,
      recordList : [
        {
          productId: values.productId,
          price: values.price,
          listMaterial: valuesPreView
        }
      ]
    }
  }
    return temp;
  }

// xử lý việc nhập tiếp theo ...
 const handleNextButton = () =>
 {    
    const tempArr = [...valuesPreView];
    if (validate() && validateMultiInput() ){
      setDisableInput(true);
      setResetMulInput(!resetMulInput);
      //lưu vào data vào trong state để render ra preview form
      values.listMaterialCode.forEach((item) => {
        // loại bỏ trường hợp trùng
        const arrayToCheckUnique = valuesPreView.map((item) => item.credential);
        if (!arrayToCheckUnique.includes(item)) {
          const temp = {  credential: item,
            timeStartDepreciation: values.timeStartDepreciation,
            placeId: values.placeId,
          }
          tempArr.push(temp);
        }
      });
      setValuesPreView(tempArr);
    }
 }
  const onDeletePreview = (row) => {
  const valuesPreview = valuesPreView.filter(m => m.credential!== row.credential);
  setValuesPreView(valuesPreview);

 }
 useEffect(() => {
    Utils.getDataAdditional().then((response) => {
      const additional = [...response].filter((item) => (item.inProcess.toString() === "true"));
      setDataAdditional([...additional]);
    });
    Utils.getDataPlace().then((response) => {
      setDataPlace([...response]);
    });

    Utils.getDataProduct().then((response) => {
      setDataProduct([...response]);
    });
    Utils.getDataCredentialCode().then((response) => {
      setDataCredentialCode([...response]);
    });
  }, []);

  return (
    <Form onSubmit={handleSubmit}>
    <Grid container>
      <Grid item xs={3}>
          <Controls.AutoCompleteButton
            name="additionalId"
            disabled =  {disableInput}
            label="Chọn đợt bổ sung"
            value={DataAdditional.find(
              (item) => item.id === values.additionalId
            )}
            onChange={handleInputChange}
            options={DataAdditional}
            error={errors.additionalId}
          />
          <Controls.AutoCompleteButton
            name="productId"
            disabled = {disableInput}
            label="Chọn sản phẩm"
            value={DataProduct.find((item) => item.id === values.productId)}
            onChange={handleInputChange}
            options={DataProduct}
            error={errors.productId}
          />
            <Controls.Input
            name="price"
            disabled = {disableInput}
            label="Giá"
            value={values.price}
            onChange={handleInputChange}
            error={errors.price}
          />
      <div>
      <Controls.Button 
      text="Next" 
      size="small"
      endIcon={<Icon>skip_next</Icon>}
      color="secondary"
      onClick={handleNextButton} />

      </div>
      </Grid>
      <Grid item xs={3}>
      <Controls.MultipleInput
        name = "listMaterialCode"
        resetMulInput = {resetMulInput}
        chipCheckList = {DataCredentialCode}
        onChange ={handleInputChange}
        isEmptyMultiInput={isEmptyMultiInput}
      />
      <Controls.AutoCompleteButton
            name="placeId"
            label="Chọn nơi phân bổ"
            value={DataPlace.find((item) => item.id === values.placeId)}
            onChange={handleInputChange}
            options={DataPlace}
            error={errors.placeId}
          />
      <Controls.DatePicker
            name="timeStartDepreciation"
            label="Thời gian b.đầu tính khấu hao"
            value={values.timeStartDepreciation}
            onChange={handleInputChange}
            error={errors.timeStartDepreciation}
          />
      </Grid>
      <Grid item xs={6}>
      <PreviewForm
      values = {valuesPreView}
      onDelete = {onDeletePreview}
      dataPlace = {DataPlace}
       /> 
      <Controls.Button 
      style = {{float : "right"}}
      size="small"
      startIcon={<SaveIcon />} 
      onClick = {handleSubmitOnClick} 
      text="Save All" />
      </Grid>
    </Grid>
  </Form>
  );
}
