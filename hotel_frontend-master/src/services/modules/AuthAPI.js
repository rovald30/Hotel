import api from "../api";

export default {
  login(username, password) {
    if (username !== null && password !== null) {
      return api()
        .post("/api/auth/login", { username, password })
        .then((response) => response)
        .catch((error) => error);
    }
  },
  register(accountData) {
    return api()
      .post("/api/auth/signup", accountData)
      .then((response) => response)
      .catch((error) => error);
  },
  confirmEmailToken(token) {
    return api()
      .get("/api/auth/confirm?token=" + token)
      .then((response) => response)
      .catch((error) => error.response);
  },
};
