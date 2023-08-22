<script setup>
import { RouterView } from "vue-router";
import BottomNotification from "@/components/_atoms/BottomNotification/BottomNotification.vue";
</script>

<template>
  <div class="app">
    <RouterView />
    <div class="absolute z-50">
      <BottomNotification />
    </div>
  </div>
</template>

<style>
@tailwind base;
@tailwind components;
@tailwind utilities;
@import url("https://fonts.googleapis.com/css2?family=Raleway:wght@400;500&display=swap");

* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
  font-family: "Raleway", sans-serif;
  font-weight: 400;
}

.app {
  min-height: 100vh;
  position: relative;
  background-color: #f1f1f1;
}

.container {
  padding: 100px 20px 10px 10px;
  max-width: 1140px;
  margin: 0 auto;
}
</style>
