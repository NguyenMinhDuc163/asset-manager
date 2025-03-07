import Product from "./Product";

export const ProductConfig = {
  routes: [
    {
      path: "/product",
      exact: true,
      component: Product,
    },
  ],
};
