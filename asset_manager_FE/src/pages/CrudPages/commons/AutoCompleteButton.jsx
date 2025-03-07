import React from "react";
import TextField from "@material-ui/core/TextField";
import Autocomplete from "@material-ui/lab/Autocomplete";
import FormControl from "@material-ui/core/FormControl";
import { FormHelperText } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";

const useStyles = makeStyles({
  root: {
    "& .MuiFormControl-fullWidth": {
      width: "100% !important",
      marginLeft: "0px !important",
    },
  },
});
export default function AutoCompleteButton(props) {
  const { name, label, value, onChange, error = null, options , ...other } = props;
  const classes = useStyles();
  const convertToDefEventPara = (event, name, values) => {
    if (values) {
      return {
        target: {
          name,
          value: values.id,
        },
      };
    } else {
      return {
        target: {
          name,
          value: "",
        },
      };
    }
  };
  return (
    <FormControl variant="outlined" {...(error && { error: true })}>
      <Autocomplete
      {...other}
        className={classes.root}
        name={name}
        value={value || null}
        options={options}
        getOptionLabel={(option) => option.title}
        getOptionSelected={(option, value) =>
          value && option.title === value.title
        }
        onChange={(event, values) =>
          onChange(convertToDefEventPara(event, name, values))
        }
        renderInput={(params) => (
          <TextField {...params} label={label} variant="outlined" />
        )}
      />
      {error && <FormHelperText>{error}</FormHelperText>}
    </FormControl>
  );
}
