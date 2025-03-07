import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/product";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getProduct(productId) {
  return HTTP.get(`${getOneQuery}${productId}`);
}

export function getAllProduct() {
  return HTTP.GET(getAllQuery);
}

export function insertProduct(product) {
  return HTTP.POST(postQuery, product);
}

export function updateProduct(product) {
  return HTTP.PUT(putQuery, product);
}

export function deleteProduct(productId) {
  return HTTP.DELETE(`${deleteQuery}${productId}`);
}

export default {
  getProduct,
  getAllProduct,
  insertProduct,
  updateProduct,
  deleteProduct,
};
