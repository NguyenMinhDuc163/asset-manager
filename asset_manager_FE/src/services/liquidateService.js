import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/liquidate";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

// extra Query String
const putQueryChangeStatus = apiEndpoint + ":changeStatus";

HTTP.handleProtectedAPI();

export function getLiquidate(liquidateId) {
  return HTTP.get(`${getOneQuery}${liquidateId}`);
}

export function getAllLiquidate() {
  return HTTP.GET(getAllQuery);
}

export function insertLiquidate(liquidate) {
  return HTTP.POST(postQuery, liquidate);
}

export function changeStatusLiquidate(changeStatusLiquidate) {
  return HTTP.PUT(putQueryChangeStatus, changeStatusLiquidate);
}

export function updateLiquidate(liquidate) {
  return HTTP.PUT(putQuery, liquidate);
}

export function deleteLiquidate(liquidateId) {
  return HTTP.DELETE(`${deleteQuery}${liquidateId}`);
}

export default {
  getLiquidate,
  getAllLiquidate,
  insertLiquidate,
  changeStatusLiquidate,
  updateLiquidate,
  deleteLiquidate,
};
