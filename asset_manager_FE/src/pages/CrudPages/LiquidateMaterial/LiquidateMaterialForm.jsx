import React, { useEffect, useState } from "react";
import { Grid } from "@material-ui/core";
import Controls from "../commons/Controls";
import { useForm, Form } from "../commons/useForm";
import * as Utils from "../../../utils/Utils";

const initialFValues = (materialId) => {
  return {
    materialId: materialId,
    liquidateId: "",
  };
};

export default function LiquidateMaterialForm(props) {
  const { addLiquidateMaterial, inforLiquidate } = props;
  const [DataLiquidate , setDataLiquidate ] = useState([]);
  const validate = (fieldValues = values) => {
    let temp = { ...errors };
    if ("liquidateId" in fieldValues) {
      temp.liquidateId = fieldValues.liquidateId
        ? ""
        : "Trường này là bắt buộc.";
    }
    setErrors({
      ...temp,
    });

    if (fieldValues === values)
      return Object.values(temp).every((x) => x === "");
  };

  const { values, errors, setErrors, handleInputChange, resetForm } = useForm(
    initialFValues(inforLiquidate.id),
    true,
    validate
  );

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      addLiquidateMaterial(values, resetForm);
    }
  };

  useEffect(() => {
    Utils.getDataLiquidate().then((response) => {
      setDataLiquidate([...response]);
    });
  }, []);

  return (
    <Form onSubmit={handleSubmit}>
      <Grid container>
        <Grid item sm={12}>
          <Controls.AutoCompleteButton
            name="liquidateId"
            label="Chọn đợt thanh lý"
            value={DataLiquidate.find((item) => item.id === values.liquidateId)}
            onChange={handleInputChange}
            options={DataLiquidate}
            error={errors.liquidateId}
          />
          <div>
            <Controls.Button type="submit" text="Thực thi" />
            <Controls.Button text="Reset" color="default" onClick={resetForm} />
          </div>
        </Grid>
      </Grid>
    </Form>
  );
}
