import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/transferMaterial";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getTransferMaterial(transferMaterialId) {
  return HTTP.get(`${getOneQuery}${transferMaterialId}`);
}

export function getAllTransferMaterial() {
  return HTTP.GET(getAllQuery);
}

export function insertTransferMaterial(transferMateral) {
  return HTTP.POST(postQuery, transferMateral);
}

export function updateTransferMaterial(transferMateral) {
  return HTTP.PUT(putQuery, transferMateral);
}

export function deleteTransferMaterial(transferMaterialId) {
  return HTTP.DELETE(`${deleteQuery}${transferMaterialId}`);
}

export default {
  getTransferMaterial,
  getAllTransferMaterial,
  insertTransferMaterial,
  updateTransferMaterial,
  deleteTransferMaterial,
};
