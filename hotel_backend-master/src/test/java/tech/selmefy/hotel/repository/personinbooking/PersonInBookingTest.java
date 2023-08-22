package tech.selmefy.hotel.repository.personinbooking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.service.room.type.RoomType;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonInBookingTest {

    private Room room1 = new Room(1L, 5F, 1, 1, 5, RoomType.REGULAR, true);
    private Person person1 = new Person(1L, "1236",
            "Tester3",
            "Trickster3",
            "Estonia",
            "12234",
            LocalDate.of(2000, 12, 20), new Timestamp(System.currentTimeMillis()));

    private Person person2 = new Person(2L, "1234",
            "Tester3",
            "Trickster3",
            "Estonia",
            "12234",
            LocalDate.of(2000, 12, 20), new Timestamp(System.currentTimeMillis()));

    Booking booking1 = Booking.builder()
            .id(1L)
            .roomId(1L)
            .room(room1)
            .price(50)
            .checkInDate(LocalDate.of(2024, 10, 20))
            .checkOutDate(LocalDate.of(2024, 10, 25))
            .comments("Something something")
            .lateCheckOut(true)
            .build();




    PersonInBooking personInBooking = PersonInBooking.builder()
        .bookingId(1L)
        .booking(booking1)
        .person(person1)
        .personId(1L)
        .personIdentityCode(person1.getIdentityCode())
        .build();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void gettersWork() {

        assertEquals(booking1.getId(), personInBooking.getBookingId());
        assertEquals(booking1, personInBooking.getBooking());
        assertEquals(person1.getId(), personInBooking.getPersonId());
        assertEquals(person1, personInBooking.getPerson());
        assertEquals(person1.getIdentityCode(), personInBooking.getPersonIdentityCode());
    }

    @Test
    void settersWork() {

        Booking booking2 = Booking.builder()
                .id(2L)
                .roomId(1L)
                .room(room1)
                .price(50)
                .checkInDate(LocalDate.of(2024, 10, 26))
                .checkOutDate(LocalDate.of(2024, 10, 28))
                .comments("Something something")
                .lateCheckOut(false)
                .build();

        PersonInBooking personInBooking2 = PersonInBooking.builder()
                .bookingId(1L)
                .booking(booking1)
                .person(person1)
                .personId(1L)
                .personIdentityCode(person1.getIdentityCode())
                .build();

        personInBooking2.setPersonId(person2.getId());
        personInBooking2.setPerson(person2);
        personInBooking2.setPersonIdentityCode(person2.getIdentityCode());
        personInBooking2.setBooking(booking2);
        personInBooking2.setBookingId(booking2.getId());

        assertEquals(person2.getId(), personInBooking2.getPersonId());
        assertEquals(person2.getIdentityCode(), personInBooking2.getPersonIdentityCode());
        assertEquals(person2, personInBooking2.getPerson());
        assertEquals(booking2.getId(), personInBooking2.getBookingId());
        assertEquals(booking2, personInBooking2.getBooking());
    }
}