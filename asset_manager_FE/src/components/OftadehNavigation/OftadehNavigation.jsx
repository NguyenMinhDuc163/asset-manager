import React from "react";
import Divider from "@material-ui/core/Divider";
import List from "@material-ui/core/List";
import { makeStyles } from "@material-ui/core/styles";
import navigationConfig from "../../oftadeh-configs/navigationConfig";

import OftadehNavGroup from "./sections/OftadehNavGroup";
import OftadehNavCollapse from "./sections/OftadehNavCollapse";
import OftadehNavItem from "./sections/OftadehNavItem";
import OftadehNavLink from "./sections/OftadehNavLink";
import { Typography } from "@material-ui/core";
import clsx from "clsx";

import ptitLogo from "../../assets/images/ptit-logo.png";

const useStyles = makeStyles((theme) => ({
  toolbar: theme.mixins.toolbar,
  logo: {
    padding: "1rem",
    "& span": {
      display: "block",
    },
  },
  navCustom: {
    "& .MuiTypography-root": {
      fontSize: ".85rem",
    },
    "& .MuiListItemIcon-root": {
      minWidth: "35px",
    },
    "& .MuiCollapse-wrapperInner a": {
      paddingLeft: "50px",
    },
  },
}));

const OftadehNavigation = (props) => {
  const classes = useStyles(props);

  return (
    <div>
      <div className={clsx(classes.toolbar)}>
        <img style={{ padding: "15px" }} src={ptitLogo} alt="logo ptit" />

        <Typography
          className={classes.logo}
          variant="h6"
          component="h1"
          align="center"
        >
          <span> QUẢN LÝ TÀI SẢN HỌC VIỆN</span>
        </Typography>
      </div>
      <Divider />
      <List className={classes.navCustom}>
        {navigationConfig.map((item) => (
          <React.Fragment key={item.id}>
            {item.type === "group" && <OftadehNavGroup item={item} />}

            {item.type === "collapse" && <OftadehNavCollapse item={item} />}

            {item.type === "item" && <OftadehNavItem item={item} />}

            {item.type === "link" && <OftadehNavLink item={item} />}

            {item.type === "divider" && <Divider className="my-16" />}
          </React.Fragment>
        ))}
      </List>
    </div>
  );
};

export default OftadehNavigation;
