import axios from "axios";

export default {
  getAllCountries() {
    return axios
      .get("https://restcountries.com/v3.1/all")
      .then((response) => response.data);
  },
};
