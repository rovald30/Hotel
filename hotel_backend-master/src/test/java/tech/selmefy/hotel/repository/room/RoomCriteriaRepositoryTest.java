package tech.selmefy.hotel.repository.room;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.booking.BookingCriteriaRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomCriteriaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    CriteriaBuilder criteriaBuilder;

    @Mock
    Root<Room> root;

    @Mock
    CriteriaQuery<Room> roomCriteriaQuery;

    @InjectMocks
    private RoomCriteriaRepository roomCriteriaRepository;


    @Test
    void roomSearchQuery_defaultRunsProperly() {
        // given
        BDDMockito.given(roomCriteriaQuery.from(Room.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Room.class)).willReturn(roomCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);


        // then
        assertDoesNotThrow(() -> {
            roomCriteriaRepository.roomSearchQuery(
                    "size",
                    "ASC",
                    Optional.empty(),
                    Optional.empty());

        });
    }


    @Test
    void roomSearchQuery_orderTypeDESCRunsProperly() {
        // given
        BDDMockito.given(roomCriteriaQuery.from(Room.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Room.class)).willReturn(roomCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);

        // then
        assertDoesNotThrow(() -> {
            roomCriteriaRepository.roomSearchQuery(
                    "size",
                    "DESC",
                    Optional.empty(),
                    Optional.empty());

        });
    }

    @Test
    void roomSearchQuery_filteringWithBooleanRunsProperly() {
        // given
        BDDMockito.given(roomCriteriaQuery.select(root)).willReturn(roomCriteriaQuery);
        BDDMockito.given(roomCriteriaQuery.from(Room.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Room.class)).willReturn(roomCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);

        // then
        assertDoesNotThrow(() -> {
            roomCriteriaRepository.roomSearchQuery(
                    "size",
                    "DESC",
                    Optional.of("roomAvailableForBooking"),
                    Optional.of("true"));

        });
    }

    @Test
    void roomSearchQuery_filteringWithRoomTypeRunsProperly() {
        // given
        BDDMockito.given(roomCriteriaQuery.select(root)).willReturn(roomCriteriaQuery);
        BDDMockito.given(roomCriteriaQuery.from(Room.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Room.class)).willReturn(roomCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);

        // then
        assertDoesNotThrow(() -> {
            roomCriteriaRepository.roomSearchQuery(
                    "size",
                    "DESC",
                    Optional.of("roomType"),
                    Optional.of("REGULAR"));

        });
    }

    @Test
    void roomSearchQuery_filteringWithFloatRunsProperly() {
        // given
        BDDMockito.given(roomCriteriaQuery.select(root)).willReturn(roomCriteriaQuery);
        BDDMockito.given(roomCriteriaQuery.from(Room.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Room.class)).willReturn(roomCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);

        // then
        assertDoesNotThrow(() -> {
            roomCriteriaRepository.roomSearchQuery(
                    "size",
                    "DESC",
                    Optional.of("size"),
                    Optional.of("5.0"));

        });
    }

    @Test
    void roomSearchQuery_filteringWithIntRunsProperly() {
        // given
        BDDMockito.given(roomCriteriaQuery.select(root)).willReturn(roomCriteriaQuery);
        BDDMockito.given(roomCriteriaQuery.from(Room.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Room.class)).willReturn(roomCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);

        // then
        assertDoesNotThrow(() -> {
            roomCriteriaRepository.roomSearchQuery(
                    "size",
                    "DESC",
                    Optional.of("numberOfBeds"),
                    Optional.of("2"));

        });
    }
}