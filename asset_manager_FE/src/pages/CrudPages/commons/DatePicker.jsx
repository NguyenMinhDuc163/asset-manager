import React from 'react'
import { MuiPickersUtilsProvider, KeyboardDatePicker } from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";
import moment from "moment";
export default function DatePicker(props) {
    const { name, label, value, onChange } = props
    const convertToDefEventPara = (name, value) =>  {
        return ({
            target: {
                name, value
            }
        })
    }

    const convertValue = (date) => {
        if (date instanceof Date){
            return date;
        } 
        else {
        const dateMomentObject = moment(date, "DD-MM-YYYY hh:mm:ss A");
        const dateObject = dateMomentObject.toDate();
        return  dateObject; 
        }
    }
    return (
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <KeyboardDatePicker variant="inline" inputVariant="outlined"
                label={label}
                format="dd/MM/yyyy"
                margin="normal"
                name={name}
                value={convertValue(value)}
                onChange={date =>onChange(convertToDefEventPara(name,date))}
                KeyboardButtonProps={{
                    'aria-label': 'change date',
                  }}
            />
        </MuiPickersUtilsProvider>
    )
}
