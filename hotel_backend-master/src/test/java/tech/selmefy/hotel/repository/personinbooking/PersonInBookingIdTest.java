package tech.selmefy.hotel.repository.personinbooking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertTrue;

import static org.junit.jupiter.api.Assertions.*;

class PersonInBookingIdTest {

    @Test
    void testEquals_true() {

        Long bookingId = 5L;
        Long personId = 4L;

        PersonInBookingId firstInBookingId = new PersonInBookingId(bookingId, personId);
        PersonInBookingId secondInBookingId = new PersonInBookingId(bookingId, personId);

        assertTrue(firstInBookingId.equals(secondInBookingId));
    }

    @Test
    void testEquals_falseBookingIdDiffers() {

        Long bookingId1 = 6L;
        Long bookingId2 = 5L;
        Long personId = 4L;

        PersonInBookingId firstInBookingId = new PersonInBookingId(bookingId1, personId);
        PersonInBookingId secondInBookingId = new PersonInBookingId(bookingId2, personId);

        assertFalse(firstInBookingId.equals(secondInBookingId));
    }

    @Test
    void testEquals_falsePersonIdDiffers() {

        Long bookingId1 = 6L;
        Long personId1 = 5L;
        Long personId2 = 4L;

        PersonInBookingId firstInBookingId = new PersonInBookingId(bookingId1, personId1);
        PersonInBookingId secondInBookingId = new PersonInBookingId(bookingId1, personId2);

        assertFalse(firstInBookingId.equals(secondInBookingId));
    }

    @Test
    void gettersWork() {

        Long bookingId1 = 6L;
        Long personId1 = 5L;


        PersonInBookingId bookingId = new PersonInBookingId(bookingId1, personId1);

        assertEquals(bookingId1, bookingId.getBookingId());
        assertEquals(personId1, bookingId.getPersonId());
    }
}