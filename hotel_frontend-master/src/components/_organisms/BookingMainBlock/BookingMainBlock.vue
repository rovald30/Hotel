<script setup>
import BookingForm from "@/components/_molecules/BookingForm/BookingForm.vue";
import "vue3-carousel/dist/carousel.css";
import { Carousel, Slide } from "vue3-carousel";
import { onMounted, ref } from "vue";
import { useBookingStore } from "@/stores/booking.js";
import { storeToRefs } from "pinia";

const imagesFileNames = ["hero-1.jpg", "hero-2.jpg", "hero-3.jpg"];
const dirName = "slider-imgs";
const myCarousel = ref(null);

const { checkScreen } = useBookingStore();
const { bookingFormMob } = storeToRefs(useBookingStore());

onMounted(() => {
  window.addEventListener("resize", checkScreen);
  checkScreen();
});

onMounted(() => {
  setTimeout(() => {
    myCarousel.value.restartCarousel();
  }, 50);
});
</script>

<template>
  <div data-testid="main-block" class="main-block">
    <BookingForm class="booking-form" />
    <div v-if="!bookingFormMob">
      <Carousel
        ref="myCarousel"
        :autoplay="7500"
        :itemsToShow="1"
        :transition="700"
        :wrapAround="true"
        :pauseAutoplayOnHover="false"
      >
        <Slide v-for="fileName in imagesFileNames" :key="fileName">
          <img :src="$image(fileName, dirName)" alt="" />
        </Slide>
      </Carousel>
    </div>
    <div v-else class="h-screen">
      <Carousel
        class=""
        ref="myCarousel"
        :autoplay="7500"
        :itemsToShow="1"
        :transition="700"
        :wrapAround="true"
        :pauseAutoplayOnHover="false"
      >
        <Slide v-for="fileName in imagesFileNames" :key="fileName">
          <img
            class="object-none h-screen"
            :src="$image(fileName, dirName)"
            alt=""
          />
        </Slide>
      </Carousel>
    </div>
  </div>
</template>

<style scoped>
.main-block {
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: -1;
}

.booking-form {
  position: absolute;
  display: flex;
  padding: 20px 20px 14px 5px;
  z-index: 1;
}
</style>
