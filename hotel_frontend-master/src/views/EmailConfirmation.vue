<script setup>
import AuthAPI from "@/services/modules/AuthAPI.js";
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";
import Navbar from "@/components/_organisms/Navbar/Navbar.vue";

const route = useRoute();
const token = route.query.token;
const responseData = ref(null);

onMounted(async () => {
  await AuthAPI.confirmEmailToken(token)
    .then((response) => (responseData.value = response.data))
    .catch((response) => (responseData.value = response.data));
});
</script>

<template>
  <Navbar />
  <div class="flex h-screen w-screen place-items-center justify-center pb-14">
    <p
      v-if="
        responseData?.status === 500 &&
        responseData?.message === 'Token is expired'
      "
      class="border border-red-900 px-14 py-10 border-l-8 border-l-red-500 bg-red-200/30"
    >
      {{ responseData.message }}
    </p>
    <p
      v-else-if="
        responseData?.status === 500 &&
        responseData?.message === 'Email is already confirmed!'
      "
      class="border border-blue-600 px-14 py-10 border-l-8 border-l-blue-400 bg-blue-200/30"
    >
      {{ responseData.message }}
    </p>
    <p
      v-else-if="responseData === 'Account is confirmed.'"
      class="border border-green-700 px-14 py-10 border-l-8 border-l-green-600 bg-green-200/30"
    >
      {{ responseData }}
    </p>
  </div>
</template>
