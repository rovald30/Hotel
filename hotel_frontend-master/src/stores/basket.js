import { ref } from "vue";
import { defineStore } from "pinia";

export const useBasketStore = defineStore("basket", () => {
  const basketState = ref({});

  function changeBasket(productName, changeAmount) {
    if (Object.prototype.hasOwnProperty.call(basketState.value, productName)) {
      if (basketState.value[productName] + changeAmount >= 0) {
        basketState.value[productName] += changeAmount;
      }
    } else {
      basketState.value[productName] = changeAmount;
    }
  }

  return { basketState, changeBasket };
});
