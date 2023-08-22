import axios from "axios";

const tokenFromLocalStorage = localStorage.getItem("userData")
  ? JSON.parse(localStorage.getItem("userData"))["token"]
  : null;

export default (
  url = import.meta.env.VITE_API_URL,
  token = tokenFromLocalStorage
) => {
  return axios.create({
    baseURL: url,
    headers: {
      Authorization: "Bearer" + token,
    },
  });
};
