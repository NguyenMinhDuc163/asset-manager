import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/calculationUnit";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getCalculationUnit(calculationUnitId) {
  return HTTP.get(`${getOneQuery}${calculationUnitId}`);
}

export function getAllCalculationUnit() {
  return HTTP.GET(getAllQuery);
}

export function insertCalculationUnit(calculationUnit) {
  return HTTP.POST(postQuery, calculationUnit);
}

export function updateCalculationUnit(calculationUnit) {
  return HTTP.PUT(putQuery, calculationUnit);
}

export function deleteCalculationUnit(calculationUnitId) {
  return HTTP.DELETE(`${deleteQuery}${calculationUnitId}`);
}

export default {
  getCalculationUnit,
  getAllCalculationUnit,
  insertCalculationUnit,
  updateCalculationUnit,
  deleteCalculationUnit,
};
