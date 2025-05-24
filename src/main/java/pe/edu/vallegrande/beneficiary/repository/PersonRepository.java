package pe.edu.vallegrande.beneficiary.repository;

import java.time.LocalDate;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import pe.edu.vallegrande.beneficiary.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository extends R2dbcRepository<Person, Integer> {

    //LISTADO DE BENEFICIARIOS
    Flux<Person> findByTypeKinshipAndState(String typeKinship, String state);
    
    //LISTADO DE APADRINADOS
    Flux<Person> findBySponsoredAndState(String sponsored, String state);

    //LISTA POR ID LOS BENEFICIARIOS
    @Modifying
    @Query("UPDATE person SET state = :state WHERE id_person = :id")
    Mono<Integer> updateStateById(Integer id, String state);

    //EDITA LOS DATOS DE PERSON SIN GENERERAR NUEVO ID
    @Modifying
    @Query("UPDATE person SET name = :name, surname = :surname, age = :age, birthdate = :birthdate, " +
        "type_document = :typeDocument, document_number = :documentNumber, type_kinship = :typeKinship, " +
        "sponsored = :sponsored, state = :state, family_id_family = :familyId WHERE id_person = :id")
    Mono<Integer> updatePerson(Integer id, String name, String surname, Integer age, LocalDate birthdate,
                            String typeDocument, String documentNumber, String typeKinship,
                            String sponsored, String state, Integer familyId);

    @Modifying
    @Query("INSERT INTO person (name, surname, age, birthdate, type_document, document_number, type_kinship, sponsored, state, family_id_family) " +
        "VALUES (:name, :surname, :age, :birthdate, :typeDocument, :documentNumber, :typeKinship, :sponsored, :state, :familyId)")
    Mono<Integer> insertPerson(String name, String surname, Integer age, LocalDate birthdate, 
                            String typeDocument, String documentNumber, String typeKinship, 
                            String sponsored, String state, Integer familyId);

    @Query("SELECT id_person FROM person ORDER BY id_person DESC LIMIT 1")
    Mono<Integer> getLastInsertedId();

}