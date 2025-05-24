package pe.edu.vallegrande.beneficiary.dto;
import java.time.LocalDate;

import lombok.Data;

@Data
public class EducationDTO {
    private Integer idEducation;
    private String degreeStudy;
    private String gradeBook;
    private int gradeAverage;
    private String fullNotebook;
    private String assistance;
    private String schollName;
    private LocalDate entryDate;
    private String tutorials;
    private Integer personId;
}
