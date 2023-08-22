package tech.selmefy.hotel.service.person;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.person.dto.PeopleResponseDTO;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.PersonMapper;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonCriteriaRepository;
import tech.selmefy.hotel.repository.person.PersonRepository;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

import static tech.selmefy.hotel.validators.ObjectUtilityValidator.isNullOrEmpty;

@Service
@AllArgsConstructor
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    public final PersonRepository personRepository;
    public final PersonCriteriaRepository personCriteriaRepository;

    public List<PersonDTO> getAllPeople() {
        return personRepository.findAll().stream().map(PersonMapper.INSTANCE::toDTO).toList();
    }

    public PeopleResponseDTO getAllPeopleWithParams(int pageNumber, int pageSize, String orderBy, String orderType,
                                                    Optional<String> filterBy, Optional<String> filterValue) {
        logger.info("Request getAllPeopleWithParams received");

        TypedQuery<Person> peopleSortedQuery = personCriteriaRepository.personSearchQuery(orderBy, orderType, filterBy, filterValue);

        int peopleLength;
        if (filterBy.isEmpty() && filterValue.isEmpty()) {
            peopleLength = personRepository.findAll().size();
        } else {
            peopleLength = peopleSortedQuery.getResultList().size();
        }

        peopleSortedQuery.setFirstResult(pageNumber * pageSize);
        peopleSortedQuery.setMaxResults(pageSize);
        List<Person> peopleSortedList = peopleSortedQuery.getResultList();
        List<PersonDTO> personDTOList = PersonMapper.INSTANCE.toDTOList(peopleSortedList);

        logger.info("Successfully finished getAllPeopleWithParams");
        return new PeopleResponseDTO(personDTOList, peopleLength);
    }
    
    public Long createNewPerson(@NonNull PersonDTO personDTO) {
        logger.info("Request getAllPeopleWithParams received");

        if(isNullOrEmpty(personDTO.getFirstName()) || isNullOrEmpty(personDTO.getLastName())) {
            throw new ApiRequestException("Firstname or lastname can not to be null.");
        }

        if(Boolean.TRUE.equals(isNullOrEmpty(personDTO.getIdentityCode()))) {
            throw new ApiRequestException("Identity code is required in request body.");
        }

        if(Boolean.TRUE.equals(isNullOrEmpty(personDTO.getDateOfBirth()))) {
            throw new ApiRequestException("Birthday date should be present.");
        }

        Person person = PersonMapper.INSTANCE.toEntity(personDTO);
        person.setTimeOfRegistration(new Timestamp(System.currentTimeMillis()));
        personRepository.save(person);

        logger.info("Successfully finished createNewPerson");
        return person.getId();
    }

    public PersonDTO getPersonById(Long id) {
        return personRepository.findById(id).map(PersonMapper.INSTANCE::toDTO)
            .orElseThrow(() -> new RuntimeException("Error: No person with provided id."));
    }

    public Optional<PersonDTO> getPersonByIdentityCode(String personalIdentityCode) {
        return personRepository.findPersonByIdentityCode(personalIdentityCode).map(PersonMapper.INSTANCE::toDTO);

    }

    public PersonDTO updatePerson(Long personId, PersonDTO personDTO) {
        logger.info("Request updatePerson received");


        Person person = personRepository.findById(personId).orElseThrow(
                () -> new ApiRequestException("Person does not exist with id: " + personId));

        if(isNullOrEmpty(personDTO.getFirstName()) || isNullOrEmpty(personDTO.getLastName())) {
            throw new ApiRequestException("Firstname or lastname can not to be null.");
        }

        if(Boolean.TRUE.equals(isNullOrEmpty(personDTO.getIdentityCode()))) {
            throw new ApiRequestException("Identity code is required in request body.");
        }

        if(Boolean.TRUE.equals(isNullOrEmpty(personDTO.getDateOfBirth()))) {
            throw new ApiRequestException("Birthday date should be present.");
        }

        person.setDateOfBirth(personDTO.getDateOfBirth());
        person.setLastName(personDTO.getLastName());
        person.setFirstName(personDTO.getFirstName());
        person.setIdentityCode(personDTO.getIdentityCode());

        personRepository.save(person);

        logger.info("Successfully finished updatePerson");
        return PersonMapper.INSTANCE.toDTO(person);
    }
}
