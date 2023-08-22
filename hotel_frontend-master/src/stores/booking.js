import { defineStore } from "pinia";
import { ref } from "vue";
import { useButtonLoaderStore } from "@/stores/buttonLoader.js";
import { usePersonstore } from "@/stores/person";
import { useRoomsStore } from "@/stores/rooms";
import router from "@/router/index.js";
import { addBooking } from "../services/modules/BookingAPI";
export const useBookingStore = defineStore("bookingForm", () => {
  const amountAdults = ref(1);
  const amountChildren = ref(0);
  const checkIn = ref(null);
  const checkOut = ref(null);
  const chosenRoom = ref(null);

  const bookingFormMob = ref(null);
  const windowWidth = ref(null);
  const scrolledNav = ref(null);
  const bookingPrice = ref(0)

  const { peopleInBooking, addPersonDataToDB } =
  usePersonstore();

  const { isRoomsAvailable } = useRoomsStore();

  function checkScreen() {
    windowWidth.value = window.innerWidth;
    if (windowWidth.value <= 750) {
      bookingFormMob.value = true;
      return;
    }
    bookingFormMob.value = false;
    return;
  }

  function calculateRoomPrice(roomType) {
    let pricePerDay = 0;
    let finalPrice = 0;
    if (roomType === 'REGULAR') {
      pricePerDay = 100
    } else if (roomType === 'DELUXE') {
      pricePerDay = 250
    } else if (roomType === 'KING_SIZE') {
      pricePerDay = 140
    } else if (roomType === 'ECONOMY') {
      pricePerDay = 80
    }

    const end = new Date(checkOut.value)
    const start = new Date(checkIn.value)

    const numberOfDays = (end - start) / 86400000;
    finalPrice = (pricePerDay * amountAdults.value) + (pricePerDay * amountChildren.value / 2) 
    return finalPrice
  }


  function roomIsChosen(chosenRoomData) {
    chosenRoom.value = chosenRoomData;
    bookingPrice.value = calculateRoomPrice(chosenRoom.value['roomType'])
  }

  async function submitBooking() {
    let ownerId = ""
    let otherIds = []
    for (let formNumber in peopleInBooking) {
      if (!peopleInBooking[formNumber]['foundInDatabase']) {
        addPersonDataToDB(formNumber)
      }
      if (formNumber == 1) {
        ownerId = peopleInBooking[formNumber]['idCode']
      } else {
        otherIds.push(peopleInBooking[formNumber]['idCode'])
      }
    }
    const requestBody = {
      price: bookingPrice.value,
      checkInDate: checkIn.value,
      checkOutDate: checkOut.value,
      lateCheckOut: true,
    }
    addBooking(chosenRoom.value['id'], ownerId, otherIds, requestBody).then((response) => {
      if (response['status'] === 200) {
        alert("Successfully added booking!")
      } else {
        // Since /error is not a public endpoint, only responses with status 200 are returned to the front-end. 
        alert("Failed to add booking. You should never see this.")
      }
    }).catch(() => {
      alert("Something went wrong. Could not add a booking");
    });
  }

  function cancelBooking() {
    chosenRoom.value = null;
    amountAdults.value = 1
    amountChildren.value = 0;
    checkIn.value = null;
    checkOut.value = null;
    isRoomsAvailable.value = false;
    bookingPrice.value = 0;
    router.push("/");
  }


  const updateScroll = () => {
    const scrollPosition = window.scrollY;
    if (scrollPosition > 0) {
      scrolledNav.value = true;
      return;
    }

    scrolledNav.value = false;
    return;
  };

  const buttonLoader = useButtonLoaderStore();

  const increaseAmountAdults = () => {
    amountAdults.value++;
  };

  const increaseAmountChildren = () => {
    amountChildren.value++;
  };

  const decreaseAmountAdults = () => {
    if (amountAdults.value > 1) {
      amountAdults.value--;
    }
  };

  const decreaseAmountChildren = () => {
    if (amountChildren.value > 0) {
      amountChildren.value--;
    }
  };

  return {
    amountAdults,
    amountChildren,
    checkIn,
    checkOut,
    bookingFormMob,
    chosenRoom,
    bookingPrice,
    roomIsChosen,
    checkScreen,
    submitBooking,
    cancelBooking,
    increaseAmountAdults,
    increaseAmountChildren,
    decreaseAmountAdults,
    decreaseAmountChildren,
    updateScroll,
    calculateRoomPrice,
  };
});
