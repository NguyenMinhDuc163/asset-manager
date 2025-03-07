import React from "react";
import { makeStyles, useTheme } from "@material-ui/core/styles";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import Chip from "@material-ui/core/Chip";
import { FormHelperText } from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
  formControl: {
    margin: theme.spacing(5),
  },
  chips: {
    display: "flex",
    flexWrap: "wrap",
  },
  chip: {
    margin: 4,
  },
}));

function getStyles(name, personName, theme) {
  return {
    fontWeight:
      personName.indexOf(name) === -1
        ? theme.typography.fontWeightRegular
        : theme.typography.fontWeightMedium,
  };
}

export default function MultipleSelect(props) {
  const classes = useStyles();
  const theme = useTheme();

  const { name, label, value, error = null, onChange, options } = props;
  return (
    <div>
      <FormControl
        variant="outlined"
        className={classes.formControl}
        {...(error && { error: true })}
      >
        <InputLabel id="demo-mutiple-chip-label">{label}</InputLabel>
        <Select
          labelId="demo-mutiple-chip-label"
          label={label}
          name={name}
          id="demo-mutiple-chip"
          multiple
          value={value}
          onChange={onChange}
          input={<Input id="select-multiple-chip" />}
          renderValue={(selected) => (
            <div className={classes.chips}>
              {selected.map((value) => (
                <Chip
                  key={value}
                  label={options.find((item) => item.id === value).title}
                  className={classes.chip}
                />
              ))}
            </div>
          )}
        >
          {options.map((option) => (
            <MenuItem
              key={option.id}
              value={option.id}
              style={getStyles(option.title, value, theme)}
            >
              {option.title}
            </MenuItem>
          ))}
        </Select>
        {error && <FormHelperText>{error}</FormHelperText>}
      </FormControl>
    </div>
  );
}
