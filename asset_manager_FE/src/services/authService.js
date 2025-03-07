import HTTP from "./httpService";
import { baseApiUrl } from "../config.json";

const apiEndpoint = baseApiUrl + "/auth/login";
const userKey = "user";

HTTP.handleProtectedAPI();

export async function login(userLogin) {
  const { data: user } = await HTTP.POST(apiEndpoint, userLogin);
  if (user.data.token) {
    localStorage.removeItem(userKey);
    localStorage.setItem(userKey, JSON.stringify(user.data));
   
  }
  return user.meta;
}

export function logout() {
  localStorage.removeItem(userKey);
}

export function getCurrentUser() {
  try {
    const user = JSON.parse(localStorage.getItem(userKey));
    return user;
  } catch (ex) {
    return null;
  }
}

export function isLoggedIn() {
  try {
    const user = JSON.parse(localStorage.getItem(userKey));
    if (user){
      return true;
    }else
    return false;
  } catch (ex) {
    return false;
  }
}

export function getRoles() {
  try {
    const user = JSON.parse(localStorage.getItem(userKey));
    if (user){
      return user.roles;
    }else
    return [];
  } catch (ex) {
    return [];
  }
}


export default {
  login,
  logout,
  getCurrentUser,
  isLoggedIn,
  getRoles,
};
