<script setup>
import BaseInput from "@/components/_atoms/BaseInput/BaseInput.vue";
import BaseButton from "@/components/_atoms/BaseButton/BaseButton.vue";
import { useAuthStore } from "@/stores/auth";
import { storeToRefs } from "pinia";
import { computed, onMounted } from "vue";

const { login, password } = storeToRefs(useAuthStore());
const { signIn } = useAuthStore();

const isDisabled = computed(() => !(login.value && password.value));
onMounted(() => {
  document.addEventListener("keydown", (event) => {
    if (event.code === "Enter" && !isDisabled.value) {
      signIn();
    }
  });
});
</script>

<template>
  <section class="h-screen">
    <div data-testid="login-form-view" class="container px-6 py-12 h-full">
      <div
        class="flex justify-center items-center flex-wrap h-full g-6 text-gray-800"
      >
        <div class="md:w-8/12 lg:w-6/12 mb-12 md:mb-0">
          <img
            src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.svg"
            class="w-full"
            alt="Phone image"
          />
        </div>
        <div class="md:w-8/12 lg:w-5/12 lg:ml-20">
          <form
            @submit="
              (e) => {
                e.preventDefault();
              }
            "
          >
            <div class="mb-6">
              <p class="flex font-mono w-full justify-center text-2xl">
                <b class="text-blue-500">ADMIN PANEL</b>&nbsp; - &nbsp;<i
                  class="font-serif"
                  >Selmefy Hotel</i
                >
              </p>
            </div>

            <!-- Login input -->
            <div class="mb-6">
              <BaseInput v-model="login" placeholder="Login" />
            </div>

            <!-- Password input -->
            <div class="mb-6">
              <BaseInput
                v-model="password"
                placeholder="Password"
                type="password"
              />
            </div>

            <!--            <div class="flex justify-between items-center mb-6">-->
            <!--              <Checkbox />-->
            <!--            </div>-->

            <!-- Submit button -->
            <BaseButton @click="signIn" :disabled="isDisabled" />
          </form>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped></style>
