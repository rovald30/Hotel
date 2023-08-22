<script setup>
import { useMobileStore } from "@/stores/mobile.js";
import { useAuthStore } from "@/stores/auth.js";
import { useAccountStore } from "@/stores/account.js";
import { storeToRefs } from "pinia";
import { defineComponent, h, onMounted, ref, useSlots } from "vue";
import MyProfileNav from "@/components/_molecules/MyProfileNav/MyProfileNav.vue";

const { logOut } = useAuthStore();
const { username } = useAccountStore();
const { checkScreen } = useMobileStore();
const { mobile } = storeToRefs(useMobileStore());

const isMobileMenuOpen = ref(false);
const setMobileMenuOpen = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value;
};

onMounted(() => {
  window.addEventListener("resize", checkScreen);
  checkScreen();
});

const slots = useSlots();
const RouterLinks = defineComponent({
  render() {
    if (!slots.default) {
      return undefined;
    }
    return slots.default().map((vnode) =>
      h(
        "RouterLink",
        {
          class: {
            "collection-item": true,
          },
        },
        [vnode]
      )
    );
  },
});
</script>

<template>
  <div class="flex z-50 left-12 top-0 fixed items-center justify-center h-14">
    <p class="z-50 font-bold">SELMEFY ADMIN PANEL</p>

    <div
      v-show="!mobile"
      class="flex left-0 top-0 fixed w-full h-14 justify-center items-center shadow-xl bg-blue-300"
    >
      <div class="desktop-menu">
        <slot />
      </div>
    </div>
    <div
      v-show="mobile"
      class="flex flex-col absolute w-full justify-center items-center"
    >
      <div
        class="flex left-0 top-0 fixed w-full h-14 justify-center items-center shadow-xl bg-blue-300"
      >
        <button @click="setMobileMenuOpen">Menu</button>
      </div>
      <transition name="mobile-nav">
        <div
          v-show="isMobileMenuOpen"
          class="mob-menu flex flex-col -z-10 left-0 top-36 fixed w-full h-14 justify-center items-center"
        >
          <ul
            class="flex flex-col w-full items-center justify-center pt-8 bg-gray-300/50"
          >
            <RouterLinks />
          </ul>
        </div>
      </transition>
    </div>
    <div class="flex fixed bg-gray-300 right-10">
      <MyProfileNav :username="username" @logout="logOut" />
    </div>
  </div>
</template>

<style lang="scss">
.mobile-nav-enter-active,
.mobile-nav-leave-active {
  transition: 1s ease all;
}

.mobile-nav-enter-from,
.mobile-nav-leave-to {
  transform: translateY(-280px);
}

.mobile-nav-enter-to {
  transform: translateY(0);
}

.mob-menu a {
  display: flex;
  justify-content: center;
  width: 100vw;
  padding: 14px 0;

  &:hover {
    background-color: rgba(147, 197, 253, 0.5);
  }
}

.desktop-menu a {
  display: inline;
  padding: 1.2rem 1rem;
  border-left: 1px solid var(--color-border);

  &:first-of-type {
    border: 0;
  }

  &:hover {
    background-color: rgba(59, 130, 246, 0.25);
    background-size: 3em;
  }
}
</style>
