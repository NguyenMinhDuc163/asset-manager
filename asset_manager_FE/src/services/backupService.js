import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/system/backup";
const getAllQuery = apiEndpoint + ":fetchAllVersion";
const getBackup = baseApiUrl + "/system/restoreDatabase?versionBackup=";
const restoreQuery = baseApiUrl + "/system/backup:createBackup"

HTTP.handleProtectedAPI();

export function getAllVersionBackup() {
  return HTTP.GET(getAllQuery);
}
export function Backup(position) {
  return HTTP.GET(`${getBackup}${position}`);
}

export function Restore() {
  return HTTP.GET(restoreQuery);
}



export default {
  getAllVersionBackup,
  Backup,
  Restore,
};
