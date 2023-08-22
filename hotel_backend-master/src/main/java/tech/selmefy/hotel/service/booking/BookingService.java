package tech.selmefy.hotel.service.booking;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.booking.dto.BookingResponseDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.BookingMapper;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.booking.BookingCriteriaRepository;
import tech.selmefy.hotel.repository.booking.BookingRepository;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;
import tech.selmefy.hotel.repository.personinbooking.PersonInBookingRepository;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {
        private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

        public final BookingRepository bookingRepository;
        public final PersonRepository personRepository;
        public final RoomRepository roomRepository;
        public final PersonInBookingRepository personInBookingRepository;

        public final BookingCriteriaRepository bookingCriteriaRepository;


    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(BookingMapper.INSTANCE::toDTO).toList();
    }

    public BookingResponseDTO getAllBookingsWithParams(int pageNumber, int pageSize, String orderBy, String orderType,
                                                     Optional<String> filterBy, Optional<String> filterValue) {

        logger.info("Request getAllBookingsWithParams received");
        TypedQuery<Booking> bookingSortedQuery = bookingCriteriaRepository.bookingSearchQuery(orderBy, orderType, filterBy, filterValue);

        int bookingLength;
        if (filterBy.isEmpty() && filterValue.isEmpty()) {
            bookingLength = bookingRepository.findAll().size();
        } else {
            bookingLength = bookingSortedQuery.getResultList().size();
        }

        bookingSortedQuery.setFirstResult(pageNumber * pageSize);
        bookingSortedQuery.setMaxResults(pageSize);
        List<Booking> bookingSortedList = bookingSortedQuery.getResultList();
        List<BookingDTO> bookingDTOList = BookingMapper.INSTANCE.toDTOList(bookingSortedList);

        return new BookingResponseDTO(bookingDTOList, bookingLength);
    }

    /**
     * Creates a new booking based on the BookingDTO and adds all connected people
     * to the person_in_booking table. People that are added need to have been registered
     * in the Person table of the database. If they are not, use PersonService.createNewPerson() first.
     * @param bookingDTO data about the Booking as described in BookingDTO class
     * @param roomId id of the room that is booked.
     * @param ownerIdentityCode personal identity code of the person for whom the room is booked.
     * @param otherGuestsIdentityCodes personal identity codes of all other people
     * that are connected with the booking.
     */
    @Transactional
    public void createNewBooking(@NonNull BookingDTO bookingDTO, Long roomId,
        String ownerIdentityCode, Optional<List<String>> otherGuestsIdentityCodes) {

        logger.info("Request createNewBooking received");
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiRequestException("No such room!"));

        validateBookingRequest(bookingDTO, room, Optional.empty());

        Person bookingOwner = personRepository.findPersonByIdentityCode(ownerIdentityCode)
            .orElseThrow(() -> new ApiRequestException("Owner of the booking is not in the database!"));

        Booking booking = BookingMapper.INSTANCE.toEntity(bookingDTO);
        booking.setRoom(room);
        booking.setPerson(bookingOwner);

        // Initializing the result of .save() method as a new variable
        // will make it possible to test certain logic in unit tests.
        Booking savedBooking = bookingRepository.save(booking);
        addPersonInBooking(savedBooking, bookingOwner);
        logger.info("Successfully added owner of the booking");


        if (otherGuestsIdentityCodes.isPresent()) {
            List<Person> otherPeople = otherGuestsIdentityCodes.get().stream()
                    .map(idCode -> personRepository.findPersonByIdentityCode(idCode).orElseThrow(
                            () -> new ApiRequestException("Additional guest is not in the database: " + idCode))).toList();
            for (Person person : otherPeople) {
                addPersonInBooking(savedBooking, person);
            }
            logger.info("Successfully added all other guests of the booking");
        }
    }

    /*
    If someone wants to change their check in date then we need to remove their booking
    from the list otherwise we will get "Room is not available" error.
     */

    /**
     * Check whether the data in the bookingDTO is valid
     * and whether the room is available at the period in question.
     * @param bookingDTO Data of the new booking.
     * @param room Room that is being booked.
     * @param originalBooking In case of a POST request, this is empty. In case of PUT
     * @throws ApiRequestException
     */
    private void validateBookingRequest(BookingDTO bookingDTO, Room room, Optional<Booking> originalBooking) throws ApiRequestException {
        logger.info("Request validateBookingRequest received");

        if (bookingDTO.getCheckInDate().isAfter(bookingDTO.getCheckOutDate())
                || bookingDTO.getCheckInDate().isEqual(bookingDTO.getCheckOutDate())) {
            throw new ApiRequestException("Check-out has to be after check-in!");
        }

        if (bookingDTO.getCheckInDate().isBefore(LocalDate.now())) {
            throw new ApiRequestException("Check-in cannot be in the past!");
        }

        if (!isRoomAvailable(room, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate(), originalBooking)) {
            logger.warn("Ran into exception that room is not available.");
            LocalDate checkInDate = bookingDTO.getCheckInDate();
            LocalDate checkOutDate = bookingDTO.getCheckOutDate();
            Long roomId = room.getId();
            logger.warn("Check-in : {}", checkInDate);
            logger.warn("Check-out : {}", checkOutDate);
            logger.warn("Room : {}", roomId);
            throw new ApiRequestException("Room is not available at the provided dates!");
        }
        logger.info("Successfully finished validateBookingRequest");
    }

    private void addPersonInBooking(Booking booking, Person person) {
        logger.info("Request addPersonInBooking received");

        personInBookingRepository.addPersonInBooking(booking.getId(),
            person.getId(), person.getIdentityCode());
    }
    private boolean isRoomAvailable(Room room, LocalDate fromDate, LocalDate toDate, Optional<Booking> bookingUpdate) {
        logger.info("Request isRoomAvailable received");

        if (room.getRoomAvailableForBooking().equals(Boolean.FALSE)) {
            logger.info("Returned false due to room {} not being available for booking.", room.getId());
            return false;
        } else {
            List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(booking -> booking.getRoomId().equals(room.getId()))
                .collect(Collectors.toCollection(ArrayList::new));
            if (bookingUpdate.isPresent()) {
                bookings.remove(bookingUpdate.get());
            }
            for (Booking booking : bookings) {
                logger.info("Requested room id: {}, booking room id: {}", room.getId(), booking.getRoomId());
                logger.info("Comparing existing bookings to requested dates");
                logger.info("Requested start date: {}, date in booking: {}", fromDate, booking.getCheckInDate());
                logger.info("Requested end date: {}, date in booking: {}", toDate, booking.getCheckOutDate());
                return doesBookingOverlap(fromDate, toDate, booking);
                }
            }
        logger.info("Successfully finished isRoomAvailable");
        return true;
    }

    private boolean doesBookingOverlap(LocalDate fromDate, LocalDate toDate, Booking booking) {
        logger.info("Request doesBookingOverlap received");
        if (fromDate.isBefore(booking.getCheckInDate())) {
            if (toDate.isAfter(booking.getCheckInDate())) {
                logger.info("Returned false case 1.");
                return false;
            }
        } else if (fromDate.isBefore(booking.getCheckOutDate())) {
            if (toDate.isAfter(booking.getCheckInDate()) || toDate.isAfter(booking.getCheckOutDate())) {
                logger.info("Returned false case 2.");
                return false;
            }
        }
        logger.info("Successfully finished doesBookingOverlap");
        return true;
    }

    public BookingDTO getBookingById(Long id) {
        return bookingRepository.findById(id).map(BookingMapper.INSTANCE::toDTO).orElseThrow();
    }

    public BookingDTO updateBooking(Long id, @NonNull BookingDTO updatedBookingDTO) {
        logger.info("Request updateBooking received");

        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ApiRequestException("Booking does not exist with id: " + id));

        Room room = roomRepository.findById(updatedBookingDTO.getRoomId())
            .orElseThrow(() -> new ApiRequestException("No such room!"));

        validateBookingRequest(updatedBookingDTO, room, Optional.of(booking));

        Person person = personRepository.findPersonByIdentityCode(updatedBookingDTO.getPersonIdentityCode())
            .orElseThrow(() -> new ApiRequestException("No such person!"));

        booking.setCheckInDate(updatedBookingDTO.getCheckInDate());
        booking.setCheckOutDate(updatedBookingDTO.getCheckOutDate());
        booking.setPrice(updatedBookingDTO.getPrice());
        booking.setComments(updatedBookingDTO.getComments());

        booking.setPerson(person);
        booking.setPersonIdentityCode(updatedBookingDTO.getPersonIdentityCode());

        booking.setRoomId(updatedBookingDTO.getRoomId());
        booking.setRoom(room);
        booking.setLateCheckOut(updatedBookingDTO.isLateCheckOut());

        bookingRepository.save(booking);
        logger.info("Successfully finished updateBooking");
        return BookingMapper.INSTANCE.toDTO(booking);
    }
}
