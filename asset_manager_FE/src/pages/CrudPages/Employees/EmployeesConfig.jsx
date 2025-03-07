import Employees from "./Employees";

export const EmployeesConfig = {
  routes: [
    {
      path: "/employees",
      exact: true,
      component: Employees,
    },
  ],
};
