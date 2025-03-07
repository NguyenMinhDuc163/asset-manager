import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/group";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getGroup(groupId) {
  return HTTP.get(`${getOneQuery}${groupId}`);
}

export function getAllGroup() {
  return HTTP.GET(getAllQuery);
}

export function insertGroup(group) {
  return HTTP.POST(postQuery, group);
}

export function updateGroup(group) {
  return HTTP.PUT(putQuery, group);
}

export function deleteGroup(groupId) {
  return HTTP.DELETE(`${deleteQuery}${groupId}`);
}

export default {
  getGroup,
  getAllGroup,
  insertGroup,
  updateGroup,
  deleteGroup,
};
