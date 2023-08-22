import { defineStore } from "pinia";
import { ref } from "vue";

export const useModalWindowStore = defineStore("modalWindow", () => {
  const isOpen = ref(false);

  function closeModal() {
    isOpen.value = false;
  }
  function openModal() {
    isOpen.value = true;
  }

  return {
    isOpen,
    closeModal,
    openModal,
  };
});
