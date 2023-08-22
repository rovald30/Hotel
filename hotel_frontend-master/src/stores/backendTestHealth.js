import { defineStore } from "pinia";
import { ref } from "vue";
import TestHealthAPI from "@/services/modules/BackendTestHealthAPI";

export const useBackendTestHealthStore = defineStore(
  "BackendTestHealth",
  () => {
    const responseData = ref(null);
    const apiUrl = ref(null);

    TestHealthAPI.getTestHealth().then((response) => {
      responseData.value = response;
      apiUrl.value = response.config.baseURL + response.config.url;
    });

    return {
      responseData,
      apiUrl,
    };
  }
);
