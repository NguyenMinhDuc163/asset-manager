import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/campus";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getCampus(campusId) {
  return HTTP.get(`${getOneQuery}${campusId}`);
}

export function getAllCampus() {
  return HTTP.GET(getAllQuery);
}

export function insertCampus(campus) {
  return HTTP.POST(postQuery, campus);
}

export function updateCampus(campus) {
  return HTTP.PUT(putQuery, campus);
}

export function deleteCampus(campusId) {
  return HTTP.DELETE(`${deleteQuery}${campusId}`);
}

export default {
  getCampus,
  getAllCampus,
  insertCampus,
  updateCampus,
  deleteCampus,
};
