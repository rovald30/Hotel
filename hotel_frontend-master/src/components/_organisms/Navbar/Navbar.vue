<script setup>
import { useMobileStore } from "@/stores/mobile";
import { storeToRefs } from "pinia";
import { onMounted } from "vue";

import { RouterLink } from "vue-router";
import MyProfileNav from "@/components/_molecules/MyProfileNav/MyProfileNav.vue";

const { mobile, scrolledNav, mobileNav } = storeToRefs(useMobileStore());
const { checkScreen, toggleMobileNav, updateScroll } = useMobileStore();

onMounted(() => {
  window.addEventListener("resize", checkScreen);
  checkScreen();
});

onMounted(() => {
  window.addEventListener("scroll", updateScroll);
});
</script>

<template>
  <header :class="{ 'scrolled-nav': scrolledNav }">
    <nav>
      <div class="branding">
        <img src="@/assets/img/selmefy-logo.png" alt="Selmefy" />
      </div>
      <ul v-show="!mobile" class="navigation">
        <li>
          <RouterLink class="link" active-class="underlined" to="/"
            >Home</RouterLink
          >
        </li>
        <li>
          <RouterLink class="link" active-class="underlined" to="/rooms"
            >Rooms</RouterLink
          >
        </li>
        <li>
          <RouterLink
            class="link"
            active-class="underlined"
            to="/special-offers"
            >Special Offers</RouterLink
          >
        </li>
        <li>
          <RouterLink class="link" active-class="underlined" to="/loyalty"
            >Loyalty</RouterLink
          >
        </li>
        <li>
          <RouterLink class="link" active-class="underlined" to="/about-us"
            >About Us</RouterLink
          >
        </li>
      </ul>
      <div v-show="!mobile">
        <MyProfileNav />
      </div>
      <div class="icon">
        <i
          @click="toggleMobileNav"
          v-show="mobile"
          class="far fa-bars"
          :class="{ 'icon-active': mobileNav }"
        ></i>
      </div>
    </nav>
  </header>
  <div class="dropdown-menu">
    <transition name="mobile-nav">
      <ul v-show="mobileNav" class="dropdown-nav">
        <li>
          <RouterLink class="link" active-class="underlined" to="/"
            >Home</RouterLink
          >
        </li>
        <li>
          <RouterLink class="link" active-class="underlined" to="/rooms"
            >Rooms</RouterLink
          >
        </li>
        <li>
          <RouterLink
            class="link"
            active-class="underlined"
            to="/special-offers"
            >Special Offers</RouterLink
          >
        </li>
        <li>
          <RouterLink class="link" active-class="underlined" to="/loyalty"
            >Loyalty</RouterLink
          >
        </li>
        <li>
          <RouterLink class="link" active-class="underlined" to="/about-us"
            >About Us</RouterLink
          >
        </li>
        <div class="my-profile-btn">
          <MyProfileNav />
        </div>
      </ul>
    </transition>
  </div>
</template>

<style lang="scss" scoped>
.logo {
  font-size: 20px;
  height: 100%;
  padding-top: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: black;
}
header {
  z-index: 99;
  width: 100%;
  position: fixed;
  transition: 0.5s ease all;
  color: #fff;

  nav {
    display: flex;
    flex-direction: row;
    padding: 25px 0;
    transition: 0.5s ease all;
    width: 90%;
    margin: 0 auto;
    @media (min-width: 1140px) {
      max-width: 1140px;
    }

    ul,
    .link {
      font-weight: 500;
      color: #000;
      list-style: none;
      text-decoration: none;
    }

    li {
      text-transform: uppercase;
      padding: 10px;
      margin-left: 10px;
    }

    .link {
      font-size: 14px;
      transition: 0.5s ease all;
      padding-bottom: 4px;
      border-bottom: 2px solid transparent;

      &:hover {
        border-color: black;
      }
    }

    .branding {
      display: flex;
      align-items: center;

      img {
        height: 4rem;
        transition: 0.5s ease all;
      }
    }

    .navigation {
      display: flex;
      align-items: center;
      flex: 1;
      justify-content: center;
    }

    .icon {
      color: black;
      align-items: center;
      display: flex;
      position: absolute;
      top: 0;
      right: 24px;
      height: 100%;

      i {
        cursor: pointer;
        font-size: 24px;
        transition: 0.8s ease all;
      }
    }

    .icon-active {
      transform: rotate(180deg);
    }

    .underlined {
      border-bottom: 2px solid black !important;
    }
  }
}

ul,
.link {
  font-weight: 500;
  color: #000;
  list-style: none;
  text-decoration: none;
}

li {
  text-transform: uppercase;
  padding: 8px;
  margin-left: 16px;
}

.link {
  font-size: 14px;
  transition: 0.5s ease all;
  padding-bottom: 4px;
  border-bottom: 2px solid transparent;

  &:hover {
    border-color: white;
  }
}

.dropdown-menu {
  width: 100%;
  position: fixed;
  transition: 0.5s ease all;
  color: #fff;
  z-index: 10;

  .dropdown-nav {
    display: flex;
    flex-direction: column;
    position: fixed;
    width: 100%;
    max-width: 200px;
    height: 100%;
    background-color: #5d5d5d;
    padding-top: 100px;
    top: 0;
    right: 0;
    z-index: -1;

    li {
      margin-left: 5px;
      .link {
        color: #d9d9d9;
      }
    }
  }

  .mobile-nav-enter-active,
  .mobile-nav-leave-active {
    transition: 1s ease all;
  }

  .mobile-nav-enter-from,
  .mobile-nav-leave-to {
    transform: translateX(250px);
  }

  .mobile-nav-enter-to {
    transform: translateX(0);
  }

  .underlined {
    border-bottom: 2px solid #f2f2f2 !important;
  }
}

.scrolled-nav {
  background-color: #dedede;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1),
    0 2px 4px -1px rgba(0, 0, 0, 0.06);
  z-index: 99;

  nav {
    padding: 20px 0;
    z-index: 99;

    .branding {
      img {
        margin-bottom: -5px;
        height: 3.5rem;
      }
    }
  }
}

.my-profile-btn {
  padding-left: 10px;
  margin: 25px 10px 10px 15px;
  width: 170px;
}
</style>
