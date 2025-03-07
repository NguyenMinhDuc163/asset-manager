import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

// set up Query String
const apiEndpoint = baseApiUrl + "/additionalProduct";
const postQuery = apiEndpoint + ":createInBox";

HTTP.handleProtectedAPI();

export function insertAdditionalProduct(additionalProduct) {
  return HTTP.POST(postQuery, additionalProduct);
}

export default {
  insertAdditionalProduct,
};
