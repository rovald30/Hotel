import {defineStore} from "pinia";
import { ref } from "vue";
import RoomAPI from "@/services/modules/RoomAPI.js";

export const useRoomsStore = defineStore("rooms", () => {
    const responseData = ref(null);

    const getAllRoomsWithParams = async (sortRuleObj) => {
        await RoomAPI.getAllRooms(sortRuleObj).then(response => {
            responseData.value = response.data
        });
    }

    const updateRoomInfo = async (room) => {
        await RoomAPI.updateRoomInfo(room).then(response => console.log(response.data))
    }

    const deleteRoom = async (roomId) => {
        await RoomAPI.deleteRoom(roomId).then(response => console.log(response.data))
    }

    return { responseData, getAllRoomsWithParams, updateRoomInfo, deleteRoom }
})