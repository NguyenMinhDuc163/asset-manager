import axios from "axios";
import { toast } from "react-toastify";

const userKey = "user";

axios.interceptors.response.use(null, (error) => {
  const expectedError =
    error.response &&
    error.response.status >= 400 &&
    error.response.status < 500;
  if (!expectedError) {
    toast.error("An unexpected error occurrred.");
  }
  return Promise.reject(error);
});

// đối với Protected API, cần phải có token đi  kèm, nên cài đặt mỗi lần gửi request thì gán nó vào phần header..
function handleProtectedAPI(token) {
  const user = JSON.parse(localStorage.getItem(userKey));
  if (user && user.token) {
    axios.defaults.headers.common["Authorization"] = "Bearer " + user.token;
  }
}

export default {
  GET: axios.get,
  POST: axios.post,
  PUT: axios.put,
  DELETE: axios.delete,
  handleProtectedAPI,
};
