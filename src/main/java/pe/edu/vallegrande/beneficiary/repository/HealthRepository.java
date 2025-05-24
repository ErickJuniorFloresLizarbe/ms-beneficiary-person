package pe.edu.vallegrande.beneficiary.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
//import org.springframework.data.repository.query.Param;

import pe.edu.vallegrande.beneficiary.model.Health;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HealthRepository extends R2dbcRepository<Health, Integer> {
    Flux<Health> findByPersonId(Integer personId);

    // EDITA EDUCATION Y HEALT SIN GENERAR UN NUEVO ID
    
    @Modifying
    @Query("UPDATE health SET vaccine_schemes = :vaccine, vph = :vph, influenza = :influenza, "
           + "deworming = :deworming, hemoglobin = :hemoglobin WHERE id_health = :id")
    Mono<Integer> updateHealth(Integer id, 
                               String vaccine, 
                               String vph, 
                               String influenza,
                               String deworming, 
                               String hemoglobin);
    

    
     
    // INSERTA NUEVO REGISTRO CUANDO SE EDITA Y CREA
    @Modifying
    @Query("INSERT INTO health (vaccine_schemes, vph, influenza, deworming, hemoglobin, person_id_person) " +
            "VALUES (:vaccine, :vph, :influenza, :deworming, :hemoglobin, :personId)")
    Mono<Integer> insertHealth(String vaccine, String vph, String influenza, String deworming, String hemoglobin,
            Integer personId);
}