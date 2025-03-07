import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/department";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getAllDepartment() {
  return HTTP.GET(getAllQuery);
}

export function insertDepartment(department) {
  return HTTP.POST(postQuery, department);
}

export function updateDepartment(department) {
  return HTTP.PUT(putQuery, department);
}

export function deleteDepartment(departmentId) {
  return HTTP.DELETE(`${deleteQuery}${departmentId}`);
}

export default {
  getAllDepartment,
  insertDepartment,
  updateDepartment,
  deleteDepartment,
};
