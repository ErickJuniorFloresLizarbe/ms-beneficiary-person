package pe.edu.vallegrande.beneficiary.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;

@Data
@Table("person")
public class Person {

    @Id
    @Column("id_person")
    private Integer idPerson;

    @Column("name")
    private String name;

    @Column("surname")
    private String surname;

    @Column("age")
    private Integer age;

    @Column("birthdate")
    private LocalDate birthdate;

    @Column("type_document")
    private String typeDocument;

    @Column("document_number")
    private String documentNumber;

    @Column("type_kinship")
    private String typeKinship;

    @Column("sponsored")
    private String sponsored;

    @Column("state")
    private String state;

    @Column("family_id_family")
    private Integer familyId;

}
