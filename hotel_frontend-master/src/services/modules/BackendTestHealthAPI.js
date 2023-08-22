import api from "../api";

export default {
  getTestHealth() {
    return api().get("/api/test-health");
  },
};
