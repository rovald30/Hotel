package tech.selmefy.hotel.service.room;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.room.dto.AddNewRoomDTO;
import tech.selmefy.hotel.controller.room.dto.RoomAvailableHistoryDTO;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.controller.room.dto.RoomResponseDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.RoomAvailableHistoryMapper;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.room.*;
import tech.selmefy.hotel.service.booking.BookingService;
import tech.selmefy.hotel.service.room.type.RoomType;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    public final RoomRepository roomRepository;
    private BookingService bookingService;
    public final RoomAvailableHistoryRepository roomAvailableHistoryRepository;
    public final RoomCriteriaRepository roomCriteriaRepository;

    public RoomResponseDTO getAllRoomsWithParams(int pageNumber, int pageSize, String orderBy, String orderType,

                                                 Optional<String> filterBy, Optional<String> filterValue) {
        logger.info("Request getAllRoomsWithParams received");
        TypedQuery<Room> roomSortedQuery = roomCriteriaRepository.roomSearchQuery(orderBy, orderType, filterBy, filterValue);

        int roomLength;
        if (filterBy.isEmpty() && filterValue.isEmpty()) {
            roomLength = roomRepository.findAll().size();
        } else {
            roomLength = roomSortedQuery.getResultList().size();
        }

        roomSortedQuery.setFirstResult(pageNumber * pageSize);
        roomSortedQuery.setMaxResults(pageSize);
        List<Room> roomSortedList = roomSortedQuery.getResultList();
        List<RoomDTO> roomDTOList = RoomMapper.INSTANCE.toDTOList(roomSortedList);

        logger.info("Successfully finished getAllRoomsWithParams");
        return new RoomResponseDTO(roomDTOList, roomLength);
    }

    public List<RoomDTO> getRoomsByType(String roomType) {
        return roomRepository.findRoomsByRoomType(RoomType.valueOf(roomType.toUpperCase())).stream()
                .map(RoomMapper.INSTANCE::toDTO)
                .toList();
    }

    public RoomDTO getRoomById(Long id) {
        return roomRepository.findById(id).map(RoomMapper.INSTANCE::toDTO).orElseThrow();
    }

    /**
     * Returns a list of RoomDTO-s that are available for the whole duration between the provided dates.
     * @param fromDate start date of the search
     * @param toDate end date of the search
     * @return List of RoomDTO-s that available between the two provided dates.
     */
    public List<RoomDTO> getAvailableRooms(
        LocalDate fromDate,
        LocalDate toDate,
        short adults,
        short children,
        Optional<RoomType> roomType) {

        logger.info("Request getAvailableRooms received");


        if (fromDate.isEqual(toDate) || fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Start date must be earlier than end date");
        }

        if (adults == 0) {
            throw new IllegalArgumentException("At least one adult needs to be present");
        }
        Map<Long, Room> rooms = roomRepository.findAll().stream()
            .filter(Room::getRoomAvailableForBooking)
            .filter(room -> doesRoomTypeMatch(room, roomType))
            .filter(room -> room.getNumberOfBeds() >= adults + children)
            .collect(Collectors.toMap(Room::getId, Function.identity()));
        List<BookingDTO> bookings = bookingService.getAllBookings();

        logger.info("Fetched all bookings for getAvailableRooms");
        for (BookingDTO booking : bookings) {
            Optional<Room> room = Optional.ofNullable(rooms.get(booking.getRoomId()));
            if (room.isPresent() && Boolean.TRUE.equals(room.get().getRoomAvailableForBooking())) {
                if (fromDate.isBefore(booking.getCheckInDate())) {
                    if (toDate.isAfter(booking.getCheckInDate())) {
                        rooms.remove(booking.getRoomId());
                    }
                } else if (fromDate.isBefore(booking.getCheckOutDate())) {
                    rooms.remove(booking.getRoomId());
                }
            }
        }

        List<RoomDTO> output = new ArrayList<>();
        rooms.forEach((key, room) -> transformToRoomDTOAndPutToList(room, output));

        logger.info("Successfully finished getAvailableRooms");
        return output;
    }
    private void transformToRoomDTOAndPutToList(Room room, List<RoomDTO> roomDTOList) {
        RoomDTO roomDTO = RoomMapper.INSTANCE.toDTO(room);
        roomDTOList.add(roomDTO);
    }

    private static boolean doesRoomTypeMatch(Room room, Optional<RoomType> requiredRoomType) {
        if (requiredRoomType.isPresent()) {
            return room.getRoomType() == requiredRoomType.get();
        }
        return true;
    }

    public List<RoomAvailableHistoryDTO> getRoomAvailableHistoryByRoomId(Long roomId) {
        return roomAvailableHistoryRepository.findRoomAvailableHistoriesByRoomId(roomId).stream()
            .map(RoomAvailableHistoryMapper.INSTANCE::toDTO)
            .toList();
    }

    public void updateRoomAvailableForBooking(Long roomId, Boolean isBookingAvailable) {
        logger.info("Request updateRoomAvailableForBooking received");
        Room room = roomRepository.findById(roomId).orElseThrow();
        if(room.getRoomAvailableForBooking().equals(isBookingAvailable)) {
            throw new ApiRequestException("This value is already defined for this field.");
        }
        room.setRoomAvailableForBooking(isBookingAvailable);
        roomRepository.save(room);

        RoomAvailableHistory roomAvailableHistory = new RoomAvailableHistory();
        roomAvailableHistory.setRoomId(roomId);
        roomAvailableHistory.setRoomAvailableForBooking(isBookingAvailable);
        roomAvailableHistory.setCreatedDtime(Timestamp.valueOf(LocalDateTime.now()));
        roomAvailableHistoryRepository.save(roomAvailableHistory);
        logger.info("Successfully finished updateRoomAvailableForBooking");
    }

    public ResponseEntity<Object> updateRoomInfo(RoomDTO roomDTO) {
        logger.info("Request updateRoomInfo received");

        Room room = RoomMapper.INSTANCE.toEntity(roomDTO);

        try {
            Room roomToUpdate = roomRepository.getReferenceById(room.getId());
            roomToUpdate.setRoomType(room.getRoomType());
            roomToUpdate.setRoomAvailableForBooking(room.getRoomAvailableForBooking());
            roomToUpdate.setSize(room.getSize());
            roomToUpdate.setNumberOfBeds(room.getNumberOfBeds());
            roomRepository.save(roomToUpdate);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
        logger.info("Successfully finished updateRoomInfo");
        return ResponseEntity.ok("Room with id " + roomDTO.getId() + " is updated!");
    }

    public ResponseEntity<Object> deleteRoom(Long roomId) {
        logger.info("Request deleteRoom received");

        try {
            roomRepository.deleteById(roomId);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }

        logger.info("Successfully finished deleteRoom");
        return ResponseEntity.ok("Room with id " + roomId + " is successfully removed!");
    }

    public ResponseEntity<Object> addNewRoom(AddNewRoomDTO roomDTO) {
        logger.info("Request addNewRoom received");

        Room room = RoomMapper.INSTANCE.addNewRoomToEntity(roomDTO);
        int roomsLength = roomRepository.findAll().size();
        room.setId(roomsLength + 1L);

        try {
            roomRepository.save(room);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }

        logger.info("Successfully finished addNewRoom");
        return ResponseEntity.ok("New room is successfully created!");
    }
}
