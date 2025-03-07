import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/user";
const postQuery = baseApiUrl + "/auth/register";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getUser(userId) {
  return HTTP.get(`${getOneQuery}${userId}`);
}

export function getAllUser() {
  return HTTP.GET(getAllQuery);
}

export function insertUser(user) {
  return HTTP.POST(postQuery, user);
}

export function updateUser(user) {
  return HTTP.PUT(putQuery, user);
}

export function deleteUser(userId) {
  return HTTP.DELETE(`${deleteQuery}${userId}`);
}

export default {
  getUser,
  getAllUser,
  insertUser,
  updateUser,
  deleteUser,
};
