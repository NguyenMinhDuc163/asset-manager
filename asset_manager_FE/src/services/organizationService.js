import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/organization";
const postQuery = apiEndpoint + ":create";
const deleteQuery = apiEndpoint + ":delete/";
const getAllQuery = apiEndpoint + ":fetchAll";
const getOneQuery = apiEndpoint + "/";
const putQuery = apiEndpoint + ":update";

HTTP.handleProtectedAPI();

export function getOrganization(organizationId) {
  return HTTP.get(`${getOneQuery}${organizationId}`);
}

export function getAllOrganization() {
  return HTTP.GET(getAllQuery);
}

export function insertOrganization(organization) {
  return HTTP.POST(postQuery, organization);
}

export function updateOrganization(organization) {
  return HTTP.PUT(putQuery, organization);
}

export function deleteOrganization(organizationId) {
  return HTTP.DELETE(`${deleteQuery}${organizationId}`);
}

export default {
  getOrganization,
  getAllOrganization,
  insertOrganization,
  updateOrganization,
  deleteOrganization,
};
