import { defineStore } from "pinia";
import { ref } from "vue";

export const useAccountStore = defineStore("accountStore", () => {
  const username = ref(
    "" || JSON.parse(localStorage.getItem("userData")).username
  );
  const userRole = ref(
    [] || JSON.parse(localStorage.getItem("userData")).roles
  );

  return {
    username,
    userRole,
  };
});
