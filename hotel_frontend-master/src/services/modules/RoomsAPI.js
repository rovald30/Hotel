import api from "../api";

const getAllRooms = () => {
  return api().get(`/api/rooms`);
};

const getAvailableRooms = (startDate, endDate, adults, children, roomType) => {
  let paramString = `?adults=${adults}`;

  if (children !== undefined) {
    paramString += `&children=${children}`;
  }

  if (roomType !== undefined && roomType !== "Any") {
    paramString += `&roomType=${roomType}`;
  }

  return api().get(`/api/rooms/public/${startDate}/${endDate}${paramString}`);
};

export { getAllRooms, getAvailableRooms };
