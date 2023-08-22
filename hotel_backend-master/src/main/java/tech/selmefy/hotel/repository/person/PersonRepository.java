package tech.selmefy.hotel.repository.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(value = "SELECT * FROM Person WHERE person.identity_code = :identityCode",
            nativeQuery = true)
    Optional<Person> findPersonByIdentityCode(@Param("identityCode") String identityCode);

    Boolean existsPersonByIdentityCode(String identityCode);
}
