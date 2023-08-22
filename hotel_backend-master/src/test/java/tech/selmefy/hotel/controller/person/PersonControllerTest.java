package tech.selmefy.hotel.controller.person;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import tech.selmefy.hotel.controller.person.dto.PeopleResponseDTO;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.controller.room.dto.RoomResponseDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.service.person.PersonService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    private List<PersonDTO> personDTOList = new ArrayList<>();
    private PersonDTO personDTO1 = new PersonDTO("1","john","doe", LocalDate.of(2000, 10,5),"Estonia","555");
    private PersonDTO personDTO2 = new PersonDTO("2","john","doe", LocalDate.of(2000, 10,5),"Estonia","555");
    private PersonDTO personDTO3 = new PersonDTO("3","john","doe", LocalDate.of(2000, 10,5),"Estonia","555");
    private PersonDTO personDTO4 = new PersonDTO("4","john","doe", LocalDate.of(2000, 10,5),"Estonia","555");

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;


    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void tearDown() {
        personDTOList.clear();
    }


    @Test
    void getAllPeopleWithParams_throwsApiRequestException_WhenOrderByDoesNotExist() {
        String orderByWrong = "field";
        Exception exception = assertThrows(ApiRequestException.class, () -> personController
                .getAllPeopleWithParams(0,10,orderByWrong,"ASC", Optional.empty(), Optional.empty()));

        String expectedResponse = "Cannot sort by " + orderByWrong;
        String actualResponse = exception.getMessage();

        assertTrue(actualResponse.contains(expectedResponse));
    }

    @Test
    void getAllPeopleWithParams_throwsApiRequestException_WhenFilterByDoesNotExist() {
        Optional<String> filterByWrong = Optional.of("field");
        Exception exception = assertThrows(ApiRequestException.class, () -> personController
                .getAllPeopleWithParams(0,10,"lastName","ASC", filterByWrong, Optional.empty()));

        String expectedResponse = "Cannot filter by " + filterByWrong;
        String actualResponse = exception.getMessage();

        assertTrue(actualResponse.contains(expectedResponse));
    }

    @Test
    void getAllPeopleWithParams_returnsPeopleResponseDTO_WhenRequested() {

        //given
        personDTOList.add(personDTO1);
        personDTOList.add(personDTO2);
        personDTOList.add(personDTO3);
        personDTOList.add(personDTO4);
        PeopleResponseDTO peopleResponseDTO = new PeopleResponseDTO(personDTOList, 4);
        given(personService.getAllPeopleWithParams(0,200,"lastName","ASC",Optional.of("lastName"), Optional.empty()))
                .willReturn(peopleResponseDTO);

        //when
        PeopleResponseDTO result = personController.getAllPeopleWithParams(0,300,"lastName","ASC",Optional.of("lastName"), Optional.empty());

        //then
        assertEquals(personDTOList.size(), result.getTotalPeopleLength());
        assertTrue(result.getPeople().contains(personDTO1));
        assertTrue(result.getPeople().contains(personDTO2));
    }

    @Test
    void getAllPeople_returnsPersonDTOList_WhenRequested() {
        personDTOList.add(personDTO1);
        personDTOList.add(personDTO2);
        personDTOList.add(personDTO3);
        personDTOList.add(personDTO4);
        given(personService.getAllPeople()).willReturn(personDTOList);

        List<PersonDTO> result = personController.getAllPeople();

        assertEquals(result,personDTOList);
    }

    @Test
    void getPersonByIdentityCode_returnsOptionalPersonDTO_WhenRequestedByIdentityCode() {
        personDTOList.add(personDTO1);

        given(personService.getPersonByIdentityCode("1")).willReturn(Optional.of(personDTO1));

        Optional<PersonDTO> result = personController.getPersonByIdentityCode("1");

        assertEquals(result, Optional.of(personDTO1));
    }

    @Test
    void getPersonById_returnsPersonDTO_WhenRequestedById() {
        personDTOList.add(personDTO1);

        given(personService.getPersonById(1L)).willReturn(personDTO1);

        PersonDTO result = personController.getPersonById(1L);

        assertEquals(result, personDTO1);
    }

    @Test
    void createNewPerson_returnsNothing_WhenRequestedWithPersonDTO() {
        given(personService.createNewPerson(personDTO1)).willReturn(1L);
        assertDoesNotThrow(() -> personController.createNewPerson(personDTO1));
    }

    @Test
    void updateBooking_returnsResponseEntityPersonDTO_WhenRequestedWithIdAndPersonDTO() {
        personDTOList.add(personDTO1);

        given(personService.updatePerson(1L, personDTO2)).willReturn(personDTO2);

        ResponseEntity<PersonDTO> result = personController.updatePerson(1L, personDTO2);

        assertEquals(result.getBody(), personDTO2);
    }
}