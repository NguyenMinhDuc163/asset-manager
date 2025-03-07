import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Slide from '@material-ui/core/Slide';
import Switch from '@material-ui/core/Switch';
import Tooltip from "@material-ui/core/Tooltip";


const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

export default function AlertDialogSlide(props) {
  const [open, setOpen] = React.useState(false);
  const {toolTipOn, toolTipOff,disabled = false, value, onChange, description, title } = props;
  const [checked, setChecked] = React.useState(value);

  const handleClose = () => {
    setOpen(false);
    setChecked(!checked);  
  };
  
  const handleChange = (event) => {
    setChecked(event.target.checked );  
    setOpen(true); 
  };

  const handleSubmit = (event) => {
    setOpen(false); 
    onChange();
  };

  return (
    <React.Fragment>
      <Tooltip title={checked ? toolTipOn  : toolTipOff} arrow>             
      <Switch
      disabled = {disabled}
      checked={checked}
      onChange={handleChange}
      color="primary"
      name="checked"
      inputProps={{ 'aria-label': 'primary checkbox' }}
      />
      </Tooltip>
      
      <Dialog
        open={open}
        TransitionComponent={Transition}
        keepMounted
        onClose={handleClose}
        aria-labelledby="alert-dialog-slide-title"
        aria-describedby="alert-dialog-slide-description"
      >
        <DialogTitle id="alert-dialog-slide-title">{title}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-slide-description">
          {description}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleSubmit} color="primary">
            Đồng ý
          </Button>
          <Button onClick={handleClose} color="primary">
            Hủy bỏ
          </Button>
        </DialogActions>
      </Dialog>
      </React.Fragment>
  );
}
