import { ref } from "vue";
import { defineStore } from "pinia";

export const useLoadingStore = defineStore("loading", () => {
  const isLoading = ref(false);

  const setIsLoading = (val) => {
    isLoading.value = val;
  };

  return {
    isLoading,
    setIsLoading,
  };
});
