package pe.edu.vallegrande.beneficiary.dto;
import java.time.LocalDate;
import lombok.Data;

@Data
public class HealthDTO {
    private Integer idHealth;
    private String vaccine;
    private String vph;
    private String influenza;
    private String deworming;
    private String hemoglobin;
    private LocalDate applicationDate;
    private String condicionBeneficiary;
    private Integer personId;
}
