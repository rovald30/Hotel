import api from "../api";

export default {
  getAllRooms({
    pageNumber = null,
    pageSize = null,
    orderBy = null,
    orderType = null,
    filterBy = null,
    filterValue = null,
  }) {
    let requestPath = `/api/rooms?`;
    if (pageNumber !== null) {
      requestPath += `&pageNumber=${pageNumber}`;
    }

    if (pageSize !== null) {
      requestPath += `&pageSize=${pageSize}`;
    }

    if (orderBy !== null) {
      requestPath += `&orderBy=${orderBy}`;
    }

    if (orderType !== null) {
      requestPath += `&orderType=${orderType}`;
    }

    if (filterBy !== null) {
      requestPath += `&filterBy=${filterBy}`;
    }

    if (filterValue !== null) {
      requestPath += `&filterValue=${filterValue}`;
    }

    return api().get(requestPath);
  },
  updateRoomInfo(room) {
    return api().put('/api/rooms', room)
  },
  deleteRoom(roomId) {
    return api().delete(`api/rooms/${roomId}`)
  }
};
