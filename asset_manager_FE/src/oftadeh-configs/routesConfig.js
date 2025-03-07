import React from "react";
import { Redirect } from "react-router-dom";

import { DashboardPageConfig } from "../pages/dashboard/DashboardPageConfig";

import { LoginPageConfig } from "../pages/auth/login/LoginPageConfig";
import { LogoutPageConfig } from "../pages/auth/logout/LogoutPageConfig";
import { Error404PageConfig } from "../pages/errors/404/Error404PageConfig";
import { Error500PageConfig } from "../pages/errors/500/Error500PageConfig";

import { EmployeesConfig } from "../pages/CrudPages/Employees/EmployeesConfig";
import { TableTestConfig } from "../tests/TableTestConfig";

import { OrganizationConfig } from "../pages/CrudPages/Organization/OrganizationConfig";
import { CalculationUnitConfig } from "../pages/CrudPages/CalculationUnit/CalculationUnitConfig";
import { CategoryConfig } from "../pages/CrudPages/Category/CategoryConfig";
import { ProductConfig } from "../pages/CrudPages/Product/ProductConfig";
import { CampusConfig } from "../pages/CrudPages/Campus/CampusConfig";
import { TypePlaceConfig } from "../pages/CrudPages/TypePlace/TypePlaceConfig";
import { DepartmentConfig } from "../pages/CrudPages/Department/DepartmentConfig";
import { UserConfig } from "../pages/CrudPages/User/UserConfig";
import { PlaceConfig } from "../pages/CrudPages/Place/PlaceConfig";
import { GroupConfig } from "../pages/CrudPages/Group/GroupConfig";
import { AdditionalConfig } from "../pages/CrudPages/Additional/AdditionalConfig";
import { LiquidateConfig } from "../pages/CrudPages/Liquidate/LiquidateConfig";
import { InventoryConfig } from "../pages/CrudPages/Inventory/InventoryConfig";
import { MaterialConfig } from "../pages/CrudPages/Material/MaterialConfig";
import { BackupConfig } from "../pages/CrudPages/Backup/BackupConfig";
const routeConfigs = [
  ...DashboardPageConfig.routes,

  ...LoginPageConfig.routes,
  ...LogoutPageConfig.routes,
  ...Error404PageConfig.routes,
  ...Error500PageConfig.routes,

  ...EmployeesConfig.routes,
  ...TableTestConfig.routes,

  ...OrganizationConfig.routes,
  ...CalculationUnitConfig.routes,
  ...CategoryConfig.routes,
  ...ProductConfig.routes,
  ...CampusConfig.routes,
  ...TypePlaceConfig.routes,
  ...DepartmentConfig.routes,
  ...UserConfig.routes,
  ...PlaceConfig.routes,
  ...GroupConfig.routes,
  ...AdditionalConfig.routes,
  ...LiquidateConfig.routes,
  ...InventoryConfig.routes,
  ...MaterialConfig.routes,
  ...BackupConfig.routes,
];

const routes = [
  ...routeConfigs,
  {
    component: () => <Redirect to="/error-404" />,
  },
];

export default routes;
