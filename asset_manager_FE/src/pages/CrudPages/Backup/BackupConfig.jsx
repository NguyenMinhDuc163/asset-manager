import Backup from "./Backup";

export const BackupConfig = {
  routes: [
    {
      path: "/backup",
      exact: true,
      component: Backup,
    },
  ],
};
