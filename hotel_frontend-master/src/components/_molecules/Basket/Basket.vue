<script setup>
import { useBasketStore } from "@/stores/basket";
import { storeToRefs } from "pinia";

const { changeBasket } = useBasketStore();
const { basketState } = storeToRefs(useBasketStore());

import BaseButton from "@/components/_atoms/BaseButton/BaseButton.vue";
import { onMounted } from "vue";

const props = defineProps({
  productName: String,
});

onMounted(() => {
  changeBasket(props.productName, 0);
});
</script>

<template>
  <div data-testid="basket" class="mt-5 border-t-2 border-gray-400">
    <p data-testid="basket-status">
      Currently in basket: {{ basketState[productName] }}
    </p>
    <div class="mt-3 flex flex-wrap place-content-around">
      <BaseButton
        data-testid="minus-button"
        @click-handler="changeBasket(productName, -1)"
        textContent="-"
      />
      <BaseButton
        data-testid="plus-button"
        @click-handler="changeBasket(productName, 1)"
        textContent="+"
      />
    </div>
  </div>
</template>
