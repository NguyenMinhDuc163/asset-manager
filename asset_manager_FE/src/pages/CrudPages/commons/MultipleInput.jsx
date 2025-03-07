import React , { useState, useEffect } from "react";
import ChipInput from "material-ui-chip-input";

export default function MultipleInput(props) {
  const { resetMulInput ,chipCheckList, onChange, isEmptyMultiInput, name } = props;
  const [chips, setChips] = useState([]);
  const [error, setError] = useState(false);
  const [checkFlag, setCheckFlag]  = useState(false);

  useEffect(() => {
    
    setChips([]);
    if (isEmptyMultiInput)
    {
      setError(true);
    }else
    {
      setError(false);
    }
  }, [resetMulInput, setChips, isEmptyMultiInput]);

  const convertToDefEventPara = (name, value) =>  {
    return ({
        target: {
            name, value
        }
    })
}

  // xử lý lỗi not update state after setValue
  // https://stackoverflow.com/questions/41446560/react-setstate-not-updating-state
  useEffect(() => {
    // dont call first time
    if ( checkFlag) {
      onChange(convertToDefEventPara(name,chips))
    }
   
  }, [chips, checkFlag, name,onChange ] )

  const validate = (chip) => {
    if (chipCheckList.find((item) => item.toLowerCase() === chip.toLowerCase()))
    {
      setError(true);
      return false;
    }
    else
    {
      setError(false);
      return true;
    }
  }

  const onBeforeAdd = (chip) => {
    return chip.length >= 5 && chip.length <= 100;
  }

  const handleAdd  = (chip) => {
    if (validate(chip)) {
      setCheckFlag (true);
      setChips((prevChips) => [...prevChips, chip]);
    }
  }

  const handleDelete = (deletedChip) => {
  setCheckFlag (true);
  setChips( (prevChips) => prevChips.filter((c) => c !== deletedChip) );
  }

  return (
    <div>
        <ChipInput
          value={chips}
          name={name}
          label="Mã CSVC"
          fullWidth
          placeholder="Nhập danh sách ..."
          onBeforeAdd={(chip) => onBeforeAdd(chip)}
          onAdd={(chip) => handleAdd(chip)}
          onDelete={(deletedChip) => handleDelete(deletedChip)}
          error  = {error}
          helperText = {!error ? "": " Mã bị trùng với dữ liệu trong hệ thống hoặc bị trống." }
        />
    </div>
  );
}
