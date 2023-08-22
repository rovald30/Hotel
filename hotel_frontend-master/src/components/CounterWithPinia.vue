<script setup>
import { useCounterStore } from "@/stores/counter";
import { storeToRefs } from "pinia";

const { increment, decrement } = useCounterStore();
const { count, doubleCount } = storeToRefs(useCounterStore());
</script>

<template>
  <div class="count">Count: {{ count }}</div>
  <div class="doubleCount">Double count: {{ doubleCount }}</div>
  <div class="buttons">
    <button @click="decrement">-</button>
    <button @click="increment">+</button>
  </div>
</template>

<style scoped>
.count {
  font-size: 20px;
}
button {
  width: 25px;
  margin: 3px;
  font-weight: bold;
}
.buttons {
  padding-top: 10px;
}
</style>
