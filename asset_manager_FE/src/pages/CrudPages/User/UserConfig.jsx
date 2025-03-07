import User from "./User";

export const UserConfig = {
  routes: [
    {
      path: "/user",
      exact: true,
      component: User,
    },
  ],
};
