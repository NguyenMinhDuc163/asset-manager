import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/typePlace";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getTypePlace(typePlaceId) {
  return HTTP.get(`${getOneQuery}${typePlaceId}`);
}

export function getAllTypePlace() {
  return HTTP.GET(getAllQuery);
}

export function insertTypePlace(typePlace) {
  return HTTP.POST(postQuery, typePlace);
}

export function updateTypePlace(typePlace) {
  return HTTP.PUT(putQuery, typePlace);
}

export function deleteTypePlace(typePlaceId) {
  return HTTP.DELETE(`${deleteQuery}${typePlaceId}`);
}

export default {
  getTypePlace,
  getAllTypePlace,
  insertTypePlace,
  updateTypePlace,
  deleteTypePlace,
};
