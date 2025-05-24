package pe.edu.vallegrande.beneficiary.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import pe.edu.vallegrande.beneficiary.model.Education;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EducationRepository extends R2dbcRepository<Education, Integer> {
    Flux<Education> findByPersonId(Integer personId);

    //EDITA EDUCATION Y HEALT SIN GENERAR UN NUEVO ID
    @Modifying
    @Query("UPDATE education SET degree_study = :degreeStudy, grade_book = :gradeBook, grade_average = :gradeAverage, " +
        "full_notebook = :fullNotebook, educational_assistance = :assistance, academic_tutorials = :tutorials " +
        "WHERE id_education = :id")
    Mono<Integer> updateEducation(Integer id, String degreeStudy, String gradeBook, int gradeAverage, 
                                String fullNotebook, String assistance, String tutorials);

    //INSERTA NUEVO REGISTRO CUANDO SE EDITA Y CREA
    @Modifying
    @Query("INSERT INTO education (degree_study, grade_book, grade_average, full_notebook, educational_assistance, academic_tutorials, person_id_person) " +
           "VALUES (:degreeStudy, :gradeBook, :gradeAverage, :fullNotebook, :assistance, :tutorials, :personId)")
    Mono<Integer> insertEducation(String degreeStudy, String gradeBook, int gradeAverage, String fullNotebook, String assistance, String tutorials, Integer personId);
}