<script setup>
import BaseButton from "@/components/_atoms/BaseButton/BaseButton.vue";
import ContentWrapper from "@/components/_molecules/ContentWrapper/ContentWrapper.vue";
import { RouterLink } from "vue-router";
import RoomMolecule from "@/components/_molecules/RoomMolecule/RoomMolecule.vue";
import { onMounted, ref, inject } from "vue";
import { useRoomsStore } from "@/stores/rooms";
import { storeToRefs } from "pinia";

const roomOffers = ref(["Economy", "Regular", "Deluxe", "Family"]); // will change based on what rooms are compatible

const { isRoomsAvailable, roomsViewData, availableRoomsData } = storeToRefs(useRoomsStore());

const roomTypes = ["REGULAR", "DELUXE", "ECONOMY", "KING"];


const $image = inject("$image");
// Maybe all of this would be better off in room store?
const regularRoomImages = [
  $image("regular-room-1.jpg", "rooms-images/regular-room-images").href,
  $image("regular-room-2.jpg", "rooms-images/regular-room-images").href,
  $image("regular-room-3.jpg", "rooms-images/regular-room-images").href,
  $image("regular-room-4.jpg", "rooms-images/regular-room-images").href,
  $image("regular-room-5.jpg", "rooms-images/regular-room-images").href,
];

const deluxeRoomImages = [
  $image("deluxe-room-1.jpg", "rooms-images/deluxe-room-images").href,
  $image("deluxe-room-2.jpg", "rooms-images/deluxe-room-images").href,
  $image("deluxe-room-3.jpg", "rooms-images/deluxe-room-images").href,
  $image("deluxe-room-4.jpg", "rooms-images/deluxe-room-images").href,
  $image("deluxe-room-5.jpg", "rooms-images/deluxe-room-images").href,
];

const economyRoomImages = [
  $image("economy-room-1.jpg", "rooms-images/economy-room-images").href,
  $image("economy-room-2.jpg", "rooms-images/economy-room-images").href,
  $image("economy-room-3.jpg", "rooms-images/economy-room-images").href,
  $image("economy-room-4.jpg", "rooms-images/economy-room-images").href,
  $image("economy-room-4.jpg", "rooms-images/economy-room-images").href,
];

const kingRoomImages = [
  $image("king-room-1.jpg", "rooms-images/king-room-images").href,
  $image("king-room-2.jpg", "rooms-images/king-room-images").href,
  $image("king-room-3.jpg", "rooms-images/king-room-images").href,
  $image("king-room-4.jpg", "rooms-images/king-room-images").href,
  $image("king-room-5.jpg", "rooms-images/king-room-images").href,
];

const images = {
  REGULAR: regularRoomImages,
  DELUXE: deluxeRoomImages,
  KING_SIZE: kingRoomImages,
  ECONOMY: economyRoomImages,
};

onMounted(() => {
  
})
</script>

<template>
  <div v-if="availableRoomsData === null || availableRoomsData === undefined">
    <h1>ERROR: Booking was not requested</h1>
  </div>
  <div v-else>
    <div
      class="room-selection"
      v-for="(room, arrayIndex) in availableRoomsData['data']">
      <RoomMolecule
        class="p-10 border-4 border-gray-200 rounded-lg m-2"
        :room="room['roomType']"
        :room-data="room"
        :images="images[room['roomType']]"
      />

    </div>
  </div>
</template>

<style scoped>
.room-selection {

}
</style>
