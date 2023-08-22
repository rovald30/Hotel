package tech.selmefy.hotel.repository.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Person implements Serializable {
    /*
    We need to implement Serializable for Person because non-PK column identityCode is used as foreign key elsewhere.
    See discussion at: https://hibernate.atlassian.net/browse/HHH-7668
    */

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true, nullable = false)
    private String identityCode;

    @NonNull
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;

    @NonNull
    @NotBlank
    private String country;

    private String phoneNumber;

    @NonNull
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private Timestamp timeOfRegistration = new Timestamp(System.currentTimeMillis());
}
