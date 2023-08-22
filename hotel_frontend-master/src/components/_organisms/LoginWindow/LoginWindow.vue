<script setup>
import { DialogTitle } from "@headlessui/vue";
import ModalWindow from "@/components/_molecules/ModalWindow/ModalWindow.vue";
import IconClose from "@/components/icons/IconClose.vue";
import BaseInput from "@/components/_atoms/BaseInput/BaseInput.vue";
import { useModalWindowStore } from "@/stores/modalWindow";
import { useLoginStore } from "@/stores/login";
import { storeToRefs } from "pinia";
import { computed, onMounted } from "vue";
import ButtonWithLoader from "@/components/_atoms/ButtonWithLoader/ButtonWithLoader.vue";

const { closeModal } = useModalWindowStore();
const { logInToAccount } = useLoginStore();

const { username, password } = storeToRefs(useLoginStore());

const isDisabled = computed(() => !(username.value && password.value));
onMounted(() => {
  document.addEventListener("keydown", (event) => {
    if (event.code === "Enter" && !isDisabled.value) {
      logInToAccount();
    }
  });
});
</script>

<template>
  <ModalWindow>
    <DialogTitle as="h3" class="text-lg font-medium leading-6 text-gray-900">
      Log in..
    </DialogTitle>
    <div class="mt-2">
      <BaseInput
        v-model="username"
        label="Username"
        placeholder="username"
        autocomplete="on"
      />
    </div>

    <div class="mt-2">
      <BaseInput
        v-model="password"
        label="Password"
        placeholder="Password"
        type="password"
        autocomplete="on"
      />
    </div>

    <div class="mt-4 flex items-center justify-between">
      <ButtonWithLoader
        class="w-24 h-10"
        buttonText="Log in"
        @click="logInToAccount"
      />

      <a
        class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800"
        href="#"
      >
        Forgot Password?
      </a>
    </div>
    <div class="absolute flex top-3 right-5 cursor-pointer" @click="closeModal">
      <IconClose />
    </div>
  </ModalWindow>
</template>

<style scoped></style>
