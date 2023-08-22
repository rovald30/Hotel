<script setup>
import { ref, inject, onMounted } from "vue";
import VueEasyLightbox, { useEasyLightbox } from "vue-easy-lightbox";
import BaseButton from "@/components/_atoms/BaseButton/BaseButton.vue";
import { useBookingStore } from "@/stores/booking.js";
import { storeToRefs } from "pinia";
import { useMobileStore } from "@/stores/mobile";

const { checkScreen } = useMobileStore();
const { mobile } = storeToRefs(useMobileStore());

const $image = inject("$image");

const props = defineProps({
  room: String,
  roomData: Object,
  styleImgsBlock: String,
  images: Array,
});

const imgUrl = ref(null);
const imgArr = ref([]);

const { visibleRef, indexRef, imgsRef } = useEasyLightbox({
  imgs: [props.images],
});

const { roomIsChosen, calculateRoomPrice } = useBookingStore();
const { chosenRoom } = storeToRefs(useBookingStore());

const showImg = (roomImgId) => {
  indexRef.value = roomImgId;
  imgArr.value = imgsRef.value[0];
  visibleRef.value = true;
};

</script>

<template>
  <div v-bind="$attrs" class="room-molecule">
    <hr class= />
    <h1 class="font">{{ room }}</h1>
    <div class="pb-10">
      <div class="grid grid-cols-2 gap-2">
        <img
          class="object-cover w-fit cursor-pointer"
          :src="imgsRef[0][0]"
          @click="() => showImg(0)"
          alt="room"
        />
        <div class="grid grid-cols-2 gap-2">
    <div class="containerHead pb-4 h-1/2">
      <div class="item">
        <p>
          Our luxurious {{ room.toLowerCase() }} suite is the pinnacle of an
          amalgamation between traditional hospitality and modern amenities. 
        </p>
      </div>
      <div class="item ">
        <p>
          <strong>Size:</strong>
          &nbsp;{{ roomData.size }} m2&nbsp;
        </p>
        <p>
          <strong>Beds:</strong>
          &nbsp;{{ roomData.numberOfBeds }} &nbsp;
        </p>
        <p>
          <strong>Floor:</strong>
          &nbsp;{{ roomData.floorId }} &nbsp;
        </p>
        <p>
          <strong>Price:</strong>
          &nbsp;{{ calculateRoomPrice(room) }} &nbsp;
        </p>
        <p>
          <strong>Rooms features:</strong>
          &nbsp;
          <span class="fa"></span>
          &nbsp;
          <span class="fa"></span>
          &nbsp;
          <span class="fa"></span>
        </p>

      </div>
    </div>
        </div>
      </div>

      <vue-easy-lightbox
        :visible="visibleRef"
        :index="indexRef"
        :imgs="imgArr"
        :scroll-disabled="false"
        @hide="visibleRef = false"
      />
    </div>



    <div class="text-center pb-4">
      <BaseButton
        textContent="BOOK NOW"
        class="rounded-md px-16 py-3 book-now"
        @click-handler="roomIsChosen(roomData)"
      />
    </div>
  </div>
</template>

<style lang="scss" scoped>

.book-now {
  align-items: center;
  justify-content: center;
  background-color: #e7e7e7;
  font-weight: 00;
  color: #000;
  list-style: none;
  text-decoration: none;
  border: 2px solid saddlebrown;
  &:hover {
    box-shadow: 0 0 7px rgba(0, 0, 0, 0.3);
  }
}

.containerHead {
  display: flex;
  gap: 110px;
}

.item {
  flex-basis: 100%;
}

.font {
  font-family: "Monotype Corsiva", serif;
  font-size: 2vw;
  color: #d9d9d9;
  text-shadow: 1px 1px 1px black;
  text-align: center;
}

.room-molecule {
}
</style>
