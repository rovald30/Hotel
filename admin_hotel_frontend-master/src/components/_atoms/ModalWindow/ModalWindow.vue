<script setup>
import IconClose from "~icons/mdi/close.vue";
import { ref } from "vue";
import useClickOutside from "@/composables/useClickOutside.js";
import { useModalWindowStore } from "@/stores/modal-window.js";

const modalWindowStore = useModalWindowStore();
const modal = ref(null);

const { onClickOutside } = useClickOutside();

onClickOutside(modal, () => {
  if (modalWindowStore.showModal === true) {
    closeModal();
  }
});

const closeModal = () => {
  modalWindowStore.showModal = false;
};
</script>

<template>
  <teleport to="body">
    <transition
      enter-active-class="transition ease-out duration-200 transform"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition ease-in duration-200 transform"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div
        ref="modal-backdrop"
        class="fixed z-10 inset-0 overflow-y-auto bg-black bg-opacity-50"
        v-show="modalWindowStore.showModal"
      >
        <div
          class="flex items-start justify-center min-h-screen pt-52 text-center"
        >
          <transition
            enter-active-class="transition ease-out duration-300 transform "
            enter-from-class="opacity-0 translate-y-10 scale-95"
            enter-to-class="opacity-100 translate-y-0 scale-100"
            leave-active-class="ease-in duration-200"
            leave-from-class="opacity-100 translate-y-0 scale-100"
            leave-to-class="opacity-0 translate-y-10 translate-y-0 scale-95"
          >
            <div
              class="bg-white rounded-lg text-left overflow-hidden shadow-xl p-8 w-1/2"
              role="dialog"
              ref="modal"
              aria-modal="true"
              v-show="modalWindowStore.showModal"
              aria-labelledby="modal-headline"
            >
              <button class="absolute top-4 right-4">
                <IconClose @click="closeModal" />
              </button>
              <slot>I am empty :(</slot>
            </div>
          </transition>
        </div>
      </div>
    </transition>
  </teleport>
</template>

<style scoped></style>
