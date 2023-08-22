<script setup>
import { ref, inject } from "vue";
import VueEasyLightbox, { useEasyLightbox } from "vue-easy-lightbox";

/*
TODO: this entire component is too big and complex
I suggest we refactor this into smaller pieces 
using RoomMolecule component.
*/
const $image = inject("$image");

const props = defineProps({
  rooms: Array,
  roomsData: Array,
  styleImgsBlock: String,
});

const imgUrl = ref(null);

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

const imgArr = ref([]);

const { visibleRef, indexRef, imgsRef } = useEasyLightbox({
  imgs: [
    regularRoomImages,
    deluxeRoomImages,
    economyRoomImages,
    kingRoomImages,
  ],
});

const showImg = (arrayId, roomImgId) => {
  indexRef.value = roomImgId;
  imgArr.value = imgsRef.value[arrayId];
  visibleRef.value = true;
};

const getRoomInfo = (roomType) => {
  return props.roomsData.filter(
    (roomInfoObj) => roomInfoObj.roomType === roomType
  );
};
</script>

<template>
  <div v-for="(room, arrayTypeId) in rooms" class="overflow-hidden">
    <div v-bind="$attrs">
      <hr class="new1 pb-10" />
      <h1 class="font">{{ room }}</h1>
      <div class="pb-10">
        <div class="grid grid-cols-2 gap-2">
          <img
            class="object-cover w-fit h-full cursor-pointer"
            :src="imgsRef[arrayTypeId][0]"
            @click="() => showImg(arrayTypeId, 0)"
            alt="room"
          />
          <div class="grid grid-cols-2 grid-rows-2 gap-2">
            <div v-for="(roomImg, roomImgId) in imgsRef[arrayTypeId].slice(1)">
              <img
                class="object-cover w-full h-64 cursor-pointer"
                :src="roomImg"
                alt="Room"
                @click="() => showImg(arrayTypeId, roomImgId + 1)"
              />
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

      <div class="containerHead pb-16">
        <div class="item">
          <p>
            Our luxurious {{ room.toLowerCase() }} suite is the pinnacle of an
            amalgamation between traditional hospitality and modern amenities.
            If you were to spend a day doing absolutely nothing, taking in the
            majestic seascape outdoors, this suite would let you do just that
          </p>
        </div>
        <div class="item">
          <p>
            <strong>Size:</strong>
            &nbsp;{{ getRoomInfo(room)[0].roomSize }} m2&nbsp;
          </p>
          <p>
            <strong>Rooms feature:</strong>
            &nbsp;
            <span class="fa"></span>
            &nbsp;
            <span class="fa"></span>
            &nbsp;
            <span class="fa"></span>
          </p>
          <li>mini-bar, safe, hair dryer, flat-screen TV, writing table.</li>
        </div>
      </div>

      <div class="text-center pb-16">
        <router-link
          class="rounded-md text-xl px-16 py-3 book-now"
          to="/"
          tag="button"
          >BOOK NOW</router-link
        >
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
hr.new1 {
  border-top: 1px solid black;
}

.book-now {
  align-items: center;
  justify-content: center;
  background-color: #e7e7e7;
  font-weight: 500;
  color: #000;
  list-style: none;
  text-decoration: none;
  border: 2px solid saddlebrown;
  &:hover {
    box-shadow: 0 0 7px rgba(0, 0, 0, 0.3);
  }
}

.img-cat {
  width: 200px;
}

.column {
  float: left;
  width: 50%;
  padding: 10px;
}

.container {
  display: flex;
  align-content: stretch;
  justify-content: center;
  max-height: 700px;
  max-width: 800px;
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
  font-size: 100px;
  color: #d9d9d9;
  text-shadow: 1px 1px 1px black;
  text-align: center;
}
</style>
