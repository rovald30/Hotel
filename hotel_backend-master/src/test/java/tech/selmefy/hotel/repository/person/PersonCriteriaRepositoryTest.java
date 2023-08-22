package tech.selmefy.hotel.repository.person;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomCriteriaRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonCriteriaRepositoryTest {


    @Mock
    private EntityManager entityManager;

    @Mock
    CriteriaBuilder criteriaBuilder;

    @Mock
    Root<Person> root;

    @Mock
    CriteriaQuery<Person> personCriteriaQuery;

    @InjectMocks
    private PersonCriteriaRepository personCriteriaRepository;


    @Test
    void personSearchQuery_defaultRunsProperly() {
        // given
        BDDMockito.given(personCriteriaQuery.from(Person.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Person.class)).willReturn(personCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);


        // then
        assertDoesNotThrow(() -> {
            personCriteriaRepository.personSearchQuery(
                    "identityCode",
                    "ASC",
                    Optional.empty(),
                    Optional.empty());

        });
    }

    @Test
    void personSearchQuery_orderTypeDESCRunsProperly() {
        // given
        BDDMockito.given(personCriteriaQuery.from(Person.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Person.class)).willReturn(personCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);


        // then
        assertDoesNotThrow(() -> {
            personCriteriaRepository.personSearchQuery(
                    "identityCode",
                    "DESC",
                    Optional.empty(),
                    Optional.empty());

        });
    }

    @Test
    void personSearchQuery_filteringLocalDateRunsProperly() {

        // given
        BDDMockito.given(personCriteriaQuery.select(root)).willReturn(personCriteriaQuery);
        BDDMockito.given(personCriteriaQuery.from(Person.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Person.class)).willReturn(personCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);


        // then
        assertDoesNotThrow(() -> {
            personCriteriaRepository.personSearchQuery(
                    "identityCode",
                    "DESC",
                    Optional.of("dateOfBirth"),
                    Optional.of("15/04/2021"));
        });
    }

    @Test
    void personSearchQuery_filteringStringRunsProperly() {

        // given
        BDDMockito.given(personCriteriaQuery.select(root)).willReturn(personCriteriaQuery);
        BDDMockito.given(personCriteriaQuery.from(Person.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Person.class)).willReturn(personCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);

        // then
        assertDoesNotThrow(() -> {
            personCriteriaRepository.personSearchQuery(
                    "identityCode",
                    "DESC",
                    Optional.of("firstName"),
                    Optional.of("John"));
        });
    }
}