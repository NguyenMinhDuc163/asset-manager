import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/liquidateMaterial";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getLiquidateMaterial(liquidateMaterialId) {
  return HTTP.get(`${getOneQuery}${liquidateMaterialId}`);
}

export function getAllLiquidateMaterial() {
  return HTTP.GET(getAllQuery);
}

export function insertLiquidateMaterial(liquidateMaterial) {
  return HTTP.POST(postQuery, liquidateMaterial);
}

export function updateLiquidateMaterial(liquidateMaterial) {
  return HTTP.PUT(putQuery, liquidateMaterial);
}

export function deleteLiquidateMaterial(liquidateMaterialId) {
  return HTTP.DELETE(`${deleteQuery}${liquidateMaterialId}`);
}

export default {
  getLiquidateMaterial,
  getAllLiquidateMaterial,
  insertLiquidateMaterial,
  updateLiquidateMaterial,
  deleteLiquidateMaterial,
};
