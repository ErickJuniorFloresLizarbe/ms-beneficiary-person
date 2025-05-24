package pe.edu.vallegrande.beneficiary.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("education")
public class Education {
    @Id
    @Column("id_education")
    private Integer idEducation;

    @Column("degree_study")
    private String degreeStudy;

    @Column("grade_book")
    private String gradeBook;

    @Column("grade_average")
    private int gradeAverage;

    @Column("full_notebook")
    private String fullNotebook;

    @Column("educational_assistance")
    private String assistance;

    @Column("academic_tutorials")
    private String tutorials;

    @Column("person_id_person")
    private Integer personId;
}
