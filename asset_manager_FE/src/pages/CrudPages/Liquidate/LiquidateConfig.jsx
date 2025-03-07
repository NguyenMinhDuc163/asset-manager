import Liquidate from "./Liquidate";

export const LiquidateConfig = {
  routes: [
    {
      path: "/liquidate",
      exact: true,
      component: Liquidate,
    },
  ],
};
