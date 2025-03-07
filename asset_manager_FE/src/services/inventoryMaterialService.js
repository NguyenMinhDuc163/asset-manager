import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/inventroyMaterial";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getInventoryMaterial(inventoryMaterialId) {
  return HTTP.get(`${getOneQuery}${inventoryMaterialId}`);
}

export function getAllInventoryMaterial() {
  return HTTP.GET(getAllQuery);
}

export function insertInventoryMaterial(inventroyMaterial) {
  return HTTP.POST(postQuery, inventroyMaterial);
}

export function updateInventoryMaterial(inventroyMaterial) {
  return HTTP.PUT(putQuery, inventroyMaterial);
}

export function deleteInventoryMaterial(inventoryMaterialId) {
  return HTTP.DELETE(`${deleteQuery}${inventoryMaterialId}`);
}

export default {
  getInventoryMaterial,
  getAllInventoryMaterial,
  insertInventoryMaterial,
  updateInventoryMaterial,
  deleteInventoryMaterial,
};
