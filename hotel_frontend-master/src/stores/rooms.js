import { defineStore } from "pinia";
import { ref } from "vue";
import { getAvailableRooms } from "@/services/modules/RoomsAPI";
import router from "@/router/index.js";

export const useRoomsStore = defineStore("Rooms", () => {
  const roomsViewData = ref(null);
  const availableRoomsData = ref(null);
  const apiUrl = ref(null);
  const isRoomsAvailable = ref(false);

  /* 
        This function is repurposed from booking store
        as is conceptually more appropriate to check for availabilities
        in the rooms store.  
    */
  async function checkRoomAvailability(
    startDate,
    endDate,
    adult,
    children,
    roomType
  ) {
    if (startDate !== null && endDate !== null) {
      getAvailableRooms(startDate, endDate, adult, children, roomType).then(
        (response) => {
          if (response['status'] === 200) {
            availableRoomsData.value = response;
            apiUrl.value = response.config.baseURL + response.config.url;
            if (availableRoomsData.value['data'].length > 0) {
              isRoomsAvailable.value = true
            }
            router.push("/booking");
          } else {
            // Since /error is not a public endpoint, only responses with status 200 are returned to the front-end. 
            alert("Failed to search for available rooms. You should never see this.")
          }
        }).catch(
          () => {
            isRoomsAvailable.value = false
            alert("Something went wrong. Could not search for available rooms")
          });
    }
  }

  roomsViewData.value = [
    { roomType: "REGULAR", roomSize: 20 },
    { roomType: "DELUXE", roomSize: 35 },
    { roomType: "ECONOMY", roomSize: 16 },
    { roomType: "KING", roomSize: 46 },
  ];

  return {
    checkRoomAvailability,
    roomsViewData,
    availableRoomsData,
    isRoomsAvailable,
    apiUrl,
  };
});
