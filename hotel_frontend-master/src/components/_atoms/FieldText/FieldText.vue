<script setup>
import { useFieldTextStore } from "@/stores/fieldText";
import { storeToRefs } from "pinia";

const { showInput } = useFieldTextStore();
const { text } = storeToRefs(useFieldTextStore());
</script>

<template>
  <div>Text: {{ text }}</div>
  <input placeholder="Write your text" v-model="text" />
  <div>
    <button @click="showInput">Test your input!</button>
  </div>
</template>

<style scoped>
button {
  margin-top: 5px;
}
</style>
