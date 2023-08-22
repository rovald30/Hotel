import { computed, ref, onMounted } from "vue";
import { defineStore } from "pinia";
import { useLoadingStore } from "@/stores/loading.js";
import { notify } from "notiwind";
import AuthAPI from "@/services/modules/AuthAPI.js";

export const useAuthStore = defineStore("authStore", () => {
  const { setIsLoading } = useLoadingStore();

  const login = ref(null);
  const password = ref(null);
  const response = ref();
  const isTokenValid = ref(false);
  const loggedIn = computed(() => {
    return isTokenValid.value;
  });

  onMounted(() => {
    if (localStorage.getItem("userData")) {
      AuthAPI.checkToken().then((response) =>
        response.status >= 400
          ? (isTokenValid.value = false)
          : (isTokenValid.value = true)
      );
    }
  });

  const signIn = async () => {
    setIsLoading(true);
    setTimeout(async () => {
      response.value = await AuthAPI.login(login.value, password.value);

      if (response.value.status === 200 && response.value.statusText === "OK") {
        if (response.value.data.roles.includes("ROLE_DEFAULT_USER")) {
          setIsLoading(false);
          await notify(
            {
              group: "bottom",
              title: "Error",
              text: "You don't have privileges for login.",
            },
            6000
          );
          return;
        } else {
          localStorage.setItem("userData", JSON.stringify(response.value.data));
          await notify(
            {
              group: "top",
              title: "Success",
              text: "You are successfully logged in",
            },
            2000
          );
          setTimeout(() => location.reload(), 2500);
        }
      } else {
        setIsLoading(false);

        if (response.value.data.status >= 401) {
          await notify(
            {
              group: "bottom",
              title: "Error",
              text: "Login or password is incorrect.",
            },
            6000
          );
        }
      }
    }, 2000);
  };

  const logOut = () => {
    localStorage.removeItem("userData");
    location.reload();
  };

  return {
    loggedIn,
    login,
    password,
    signIn,
    logOut,
    isTokenValid,
  };
});
