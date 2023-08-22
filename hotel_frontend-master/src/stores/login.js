import { defineStore } from "pinia";
import { ref } from "vue";
import AuthAPI from "@/services/modules/AuthAPI.js";
import { notify } from "notiwind";
import { useButtonLoaderStore } from "@/stores/buttonLoader.js";

export const useLoginStore = defineStore("login", () => {
  const username = ref("");
  const password = ref("");
  const responseData = ref(null);

  const buttonLoader = useButtonLoaderStore();

  const logInToAccount = async () => {
    buttonLoader.setIsLoading(true);
    setTimeout(async () => {
      await AuthAPI.login(username.value, password.value)
        .then((response) => (responseData.value = response))
        .catch((error) => (responseData.value = error));

      if (responseData.value.status === 200) {
        localStorage.setItem(
          "userData",
          JSON.stringify(responseData.value.data)
        );
        buttonLoader.setIsLoading(false);
        setTimeout(() => location.reload());
      } else {
        buttonLoader.setIsLoading(false);
        await notify(
          {
            group: "bottom",
            title: "Error",
            text: responseData.value.response.data.message,
          },
          6000
        );
      }
    }, 2000);
  };

  return {
    username,
    password,
    logInToAccount,
  };
});
