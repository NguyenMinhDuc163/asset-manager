import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/place";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getPlace(placeId) {
  return HTTP.get(`${getOneQuery}${placeId}`);
}

export function getAllPlace() {
  return HTTP.GET(getAllQuery);
}

export function insertPlace(place) {
  return HTTP.POST(postQuery, place);
}

export function updatePlace(place) {
  return HTTP.PUT(putQuery, place);
}

export function deletePlace(placeId) {
  return HTTP.DELETE(`${deleteQuery}${placeId}`);
}

export default {
  getPlace,
  getAllPlace,
  insertPlace,
  updatePlace,
  deletePlace,
};
