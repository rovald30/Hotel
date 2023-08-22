<script setup>
import Navbar from "@/components/_organisms/Navbar/Navbar.vue";
import ContentWrapper from "@/components/_molecules/ContentWrapper/ContentWrapper.vue";
import RoomSelection from "@/components/_organisms/RoomSelection/RoomSelection.vue";
import PersonRegistration from "@/components/_molecules/PersonRegistration/PersonRegistration.vue";
import BaseButton from "@/components/_atoms/BaseButton/BaseButton.vue";
import { usePersonstore } from "@/stores/person.js";
import { useBookingStore } from "@/stores/booking.js";
import { useRoomsStore } from "@/stores/rooms";
import { onMounted } from "vue";
import { storeToRefs } from "pinia";


const { isRoomsAvailable } = useRoomsStore();

const { checkIn, checkOut, chosenRoom, amountAdults, amountChildren, submitBooking, cancelBooking } =
  useBookingStore();

const { countNumberOfPeopleInBooking } =
  usePersonstore();

  onMounted(() => {
  countNumberOfPeopleInBooking(amountAdults + amountChildren);
});

const bookingInfoDates = `Start date: ${checkIn}, end date: ${checkOut}`
const bookingInfoNumbers = `Adults: ${amountAdults}, children: ${amountChildren}`
</script>

<template>
  <Navbar />
  <ContentWrapper>
    <div v-if="!isRoomsAvailable">
      <p class="flex place-content-around">
      No rooms available for this date</p>
    </div>
    <div v-else-if="useBookingStore().chosenRoom === null">
      <div class="flex place-content-around">{{bookingInfoDates}}</div>
      <div class="flex place-content-around">{{bookingInfoNumbers}}</div>
      <RoomSelection />
      <BaseButton textContent="Cancel" @click-handler="cancelBooking()" />
    </div>
    <div v-else>
      <div v-for="n in amountAdults + amountChildren">
        <PersonRegistration :formNumber="n" />
      </div>
      <BaseButton textContent="Cancel" @click-handler="cancelBooking()" />
      <BaseButton textContent="Submit" @click-handler="submitBooking()" />
    </div>
  </ContentWrapper>
</template>

<style scoped>
p {
  padding: 50px 0 50px 0;
}
</style>
