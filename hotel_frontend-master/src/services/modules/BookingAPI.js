import api from "../api";

const addBooking = (roomId, ownerId, otherIds, requestBody) => {
  return api().post(`/api/booking/public?roomId=${roomId}&ownerId=${ownerId}&otherId=${otherIds}`, requestBody);
};

export { addBooking };
