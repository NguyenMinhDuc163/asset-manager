import Inventory from "./Inventory";

export const InventoryConfig = {
  routes: [
    {
      path: "/inventory",
      exact: true,
      component: Inventory,
    },
  ],
};
