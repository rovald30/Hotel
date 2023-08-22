import { defineStore } from "pinia";
import { ref } from "vue";

export const useFieldTextStore = defineStore("fieldText", () => {
  const text = ref("");

  function showInput() {
    let alertText =
      text.value === "" ? "Empty. Please write something." : text.value;
    alert("Your input from field is: \n" + alertText);
  }

  return {
    text,
    showInput,
  };
});
