import React from "react";

import NavigationContext from "../../context/NavigationContext";
import OftadehDrawer from "./OftadehDrawer";

import Main from "./Main";
import Header from "./Header";

import OftadehRightPanel from "../../components/OftadehRightPanel/OftadehRightPanel";

const handleDrawerResponsive = () => {
  return window.innerWidth >= 900;
};

const Layout = (props) => {
  const [open, setOpen] = React.useState(() => handleDrawerResponsive());

  const resizeChecker = () => {
    if (handleDrawerResponsive()) {
      setOpen(true);
    } else {
      setOpen(false);
    }
  };

  React.useEffect(() => {
    window.addEventListener("resize", resizeChecker);

    return () => {
      window.removeEventListener("resize", resizeChecker); // 🛠 Fix lỗi
    };
  }, []);

  const handleDrawerToggle = () => {
    setOpen(!open);
  };

  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const [openRightPanel, setOpenRightPanel] = React.useState(false);
  const handleRightPanelOpen = (event, newValue) => {
    setOpenRightPanel(!openRightPanel);
    handleChange(event, newValue);
  };

  return (
      <NavigationContext.Provider
          value={{
            open,
            handleDrawerToggle,
            value,
            handleChange,
            handleRightPanelOpen,
            openRightPanel,
            setOpenRightPanel,
          }}
      >
        <Header />
        <OftadehDrawer drawerWidth={240} />
        <OftadehRightPanel />
        <Main drawerWidth={240} />
      </NavigationContext.Provider>
  );
};

export default Layout;
