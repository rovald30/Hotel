import api from "../api";

export default {
  login(login, password) {
    if (login !== null && password !== null) {
      return api()
        .post("/api/auth/login", { username: login, password })
        .then((response) => response)
        .catch((error) => error.response);
    }
  },
  checkToken() {
    return api()
      .get("/api/rooms")
      .then((response) => response)
      .catch((error) => error.response);
  },
};
