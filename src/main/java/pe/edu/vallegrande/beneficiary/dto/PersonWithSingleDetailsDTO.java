package pe.edu.vallegrande.beneficiary.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonWithSingleDetailsDTO {
    private Integer idPerson;
    private String name;
    private String surname;
    private Integer age;
    private LocalDate birthdate;
    private String typeDocument;
    private String documentNumber;
    private String typeKinship;
    private String sponsored;
    private String state;
    private Integer familyId;
    private EducationDTO education;
    private HealthDTO health;
}
