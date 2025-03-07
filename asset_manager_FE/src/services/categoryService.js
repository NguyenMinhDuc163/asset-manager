import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/category";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getCategory(categoryId) {
  return HTTP.get(`${getOneQuery}${categoryId}`);
}

export function getAllCategory() {
  return HTTP.GET(getAllQuery);
}

export function insertCategory(category) {
  return HTTP.POST(postQuery, category);
}

export function updateCategory(category) {
  return HTTP.PUT(putQuery, category);
}

export function deleteCategory(categoryId) {
  return HTTP.DELETE(`${deleteQuery}${categoryId}`);
}

export default {
  getCategory,
  getAllCategory,
  insertCategory,
  updateCategory,
  deleteCategory,
};
