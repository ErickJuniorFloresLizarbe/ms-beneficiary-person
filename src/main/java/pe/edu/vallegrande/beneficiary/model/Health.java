package pe.edu.vallegrande.beneficiary.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("health")
public class Health {
    @Id
    @Column("id_health")
    private Integer idHealth;

    @Column("vaccine_schemes")
    private String vaccine;

    @Column("vph")
    private String vph;

    @Column("influenza")
    private String influenza;

    @Column("deworming")
    private String deworming;

    @Column("hemoglobin")
    private String hemoglobin;

    @Column("person_id_person")
    private Integer personId;
}
