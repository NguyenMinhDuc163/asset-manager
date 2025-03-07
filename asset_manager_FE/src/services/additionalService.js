import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/additional";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

// extra Query String
const putQueryChangeStatus = apiEndpoint + ":changeStatus";

HTTP.handleProtectedAPI();

export function getAdditional(additionalId) {
  return HTTP.get(`${getOneQuery}${additionalId}`);
}

export function getAllAdditional() {
  return HTTP.GET(getAllQuery);
}

export function insertAdditional(additional) {
  return HTTP.POST(postQuery, additional);
}

export function changeStatusAdditional(changeStatusAdditional) {
  return HTTP.PUT(putQueryChangeStatus, changeStatusAdditional);
}

export function updateAdditional(additional) {
  return HTTP.PUT(putQuery, additional);
}

export function deleteAdditional(additionalId) {
  return HTTP.DELETE(`${deleteQuery}${additionalId}`);
}

export default {
  getAdditional,
  getAllAdditional,
  insertAdditional,
  changeStatusAdditional,
  updateAdditional,
  deleteAdditional,
};
