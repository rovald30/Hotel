<script setup>
import { RouterLink, RouterView } from "vue-router";
import Navbar from "@/components/_organisms/Navbar/Navbar.vue";
import { useAuthStore } from "@/stores/auth";
import { storeToRefs } from "pinia";
import LoginForm from "@/components/_organisms/LoginForm/LoginForm.vue";
import NotificationBottom from "@/components/_atoms/NotificationBottom/NotificationBottom.vue";
import NotificationTop from "@/components/_atoms/NotificationTop/NotificationTop.vue";

const { loggedIn } = storeToRefs(useAuthStore());
</script>

<template>
  <div v-if="loggedIn">
    <Navbar>
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/bookings">Bookings</RouterLink>
      <RouterLink to="/rooms">Rooms</RouterLink>
      <RouterLink to="/users">Users</RouterLink>
      <RouterLink to="/special-offers">Special Offers</RouterLink>
    </Navbar>
    <RouterView />
  </div>
  <div v-else>
    <LoginForm />
    <NotificationTop />
    <NotificationBottom />
  </div>
</template>

<style>
@tailwind base;
@tailwind components;
@tailwind utilities;
</style>
