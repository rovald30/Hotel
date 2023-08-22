<script setup>
import Navbar from "@/components/_organisms/Navbar/Navbar.vue";
import ContentWrapper from "@/components/_molecules/ContentWrapper/ContentWrapper.vue";
import Rooms from "@/components/_organisms/Rooms/Rooms.vue";
import { useRoomsStore } from "@/stores/rooms";
import { storeToRefs } from "pinia";

const { roomsViewData } = storeToRefs(useRoomsStore());

const roomTypes = ["REGULAR", "DELUXE", "ECONOMY", "KING"];
</script>

<template>
  <slot>
    <Navbar />
  </slot>

  <div class="main-block" />

  <div class="hero-image">
    <div class="hero-text">
      <h1 class="h1">Rooms & Suits</h1>
    </div>
  </div>
  <ContentWrapper>
    <div>
      <Rooms :rooms="roomTypes" :rooms-data="roomsViewData" />
    </div>
  </ContentWrapper>
</template>

<style scoped>
.main-block {
  padding-top: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: -1;
}

.hero-image {
  background-image: url("@/assets/img/rooms-images/hero-1.jpg");
  height: 700px;
  background-position: center;
  background-size: cover;
}

.hero-text {
  text-align: center;
  position: absolute;
  top: 450px;
  left: 50%;
  transform: translate(-50%, -50%);
}

.h1 {
  font-family: "Monotype Corsiva", serif;
  font-size: 100px;
  color: #d9d9d9;
  text-shadow: 1px 1px 3px black;
}
</style>
