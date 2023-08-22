import { ref } from "vue";
import { defineStore } from "pinia";

export const useMobileStore = defineStore("mobile", () => {
  const scrolledNav = ref(null);
  const mobileNav = ref(null);
  const mobile = ref(null);
  const windowWidth = ref(null);

  const toggleMobileNav = () => {
    mobileNav.value = !mobileNav.value;
  };

  const checkScreen = () => {
    windowWidth.value = window.innerWidth;
    if (windowWidth.value <= 950) {
      mobile.value = true;
      return;
    }
    mobile.value = false;
    mobileNav.value = false;
    return;
  };

  const updateScroll = () => {
    const scrollPosition = window.scrollY;
    if (scrollPosition > 0) {
      scrolledNav.value = true;
      return;
    }

    scrolledNav.value = false;
    return;
  };

  return {
    scrolledNav,
    mobileNav,
    mobile,
    windowWidth,
    toggleMobileNav,
    checkScreen,
    updateScroll,
  };
});
