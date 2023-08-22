package tech.selmefy.hotel.controller.person;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.selmefy.hotel.controller.person.dto.PeopleResponseDTO;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.service.person.PersonService;

import java.util.List;
import java.util.Optional;

@Api(tags = "Person")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "person")
public class PersonController {
    public final PersonService personService;
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @GetMapping("/all")
    public List<PersonDTO> getAllPeople() {
        return personService.getAllPeople();
    }

    @GetMapping
    public PeopleResponseDTO getAllPeopleWithParams(
            @RequestParam(name="pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name="pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name="orderBy", defaultValue = "lastName") String orderBy,
            @RequestParam(name="orderType", defaultValue = "ASC") String orderType,
            @RequestParam(name = "filterBy") Optional<String> filterBy,
            @RequestParam(name = "filterValue") Optional<String> filterValue) {

        logger.info("Request getAllPeopleWithParams received");

        // Ensures that only PersonDTO fields are used for sorting/searching.
        try {
            PersonDTO.class.getDeclaredField(orderBy);
        } catch (NoSuchFieldException e) {
            throw new ApiRequestException("Cannot sort by " + orderBy);
        }

        if (filterBy.isPresent()) {
            try {
                PersonDTO.class.getDeclaredField(filterBy.get());
            } catch (NoSuchFieldException e) {
                throw new ApiRequestException("Cannot filter by " + filterBy);
            }
        }

        // This prevents requesting pages that are too big.
        int maxPageSize = 200;
        if (pageSize > maxPageSize) {
            logger.warn("maxPageSize exceeded, reverting to default value");
            pageSize = maxPageSize;
        }
        return personService.getAllPeopleWithParams(pageNumber, pageSize, orderBy, orderType, filterBy, filterValue);
    }

    @GetMapping("/public/{personalIdentityCode}")
    public Optional<PersonDTO> getPersonByIdentityCode(@PathVariable String personalIdentityCode) {
        logger.info("Request getPersonByIdentityCode received");
        return personService.getPersonByIdentityCode(personalIdentityCode);
    }

    @GetMapping("/{personId}")
    public PersonDTO getPersonById(@PathVariable Long personId) {
        logger.info("Request getPersonById received");
        return personService.getPersonById(personId);
    }

    @PostMapping("/public/")
    public void createNewPerson(@RequestBody PersonDTO personDTO) {
        logger.info("Request createNewPerson received");
        personService.createNewPerson(personDTO);
    }

    @PutMapping("/{personId}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable Long personId, @RequestBody PersonDTO personDTO) {
        logger.info("Request updatePerson received");
        return ResponseEntity.ok(personService.updatePerson(personId, personDTO));
    }
}