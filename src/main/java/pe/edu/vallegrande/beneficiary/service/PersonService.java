package pe.edu.vallegrande.beneficiary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import pe.edu.vallegrande.beneficiary.dto.PersonDTO;
import pe.edu.vallegrande.beneficiary.dto.HealthDTO;
import pe.edu.vallegrande.beneficiary.dto.EducationDTO;
import pe.edu.vallegrande.beneficiary.model.Person;
import pe.edu.vallegrande.beneficiary.repository.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {

    private static final String EDUCATION_SERVICE_BASE_URL = "https://ms-beneficiary-education.onrender.com";
    private static final String HEALTH_SERVICE_BASE_URL = "https://ms-beneficiary-health.onrender.com";


    @Autowired
    private PersonRepository personRepository;


    @Autowired
    private WebClient.Builder webClientBuilder;

 
    public Flux<PersonDTO> getPersonsByTypeKinshipAndState(String typeKinship, String state) {
        return personRepository.findByTypeKinshipAndState(typeKinship, state)
                .map(this::convertToDTO);
    }

    // LISTADO DE APADRINADOS ACTIVOS Y INACTIVOS
    public Flux<PersonDTO> getPersonsBySponsoredAndState(String sponsored, String state) {
        return personRepository.findBySponsoredAndState(sponsored, state)
                .map(this::convertToDTO);
    }

    // LISTADO COMPLETOS DE BENEFICIARIOS POR ID
    public Mono<PersonDTO> getPersonByIdWithDetails(Integer id) {
        return personRepository.findById(id)
                .flatMap(person -> {
                    PersonDTO dto = convertToDTO(person);
    
                    Mono<List<EducationDTO>> educationMono = webClientBuilder.build()
                            .get()
                            .uri(EDUCATION_SERVICE_BASE_URL + "/education/person/" + person.getIdPerson())
                            .retrieve()
                            .bodyToFlux(EducationDTO.class)
                            .collectList();
                            
    
                    Mono<List<HealthDTO>> healthMono = webClientBuilder.build()
                            .get()
                            .uri(HEALTH_SERVICE_BASE_URL + "/health/person/" + person.getIdPerson())
                            .retrieve()
                            .bodyToFlux(HealthDTO.class)
                            .collectList();
    
                    return Mono.zip(educationMono, healthMono)
                            .map(tuple -> {
                                dto.setEducation(tuple.getT1());
                                dto.setHealth(tuple.getT2());
                                return dto;
                            });
                });
    }
    

    // ELIMINADO LOGICO
    public Mono<Void> deletePerson(Integer id) {
        return personRepository.updateStateById(id, "I")
                .then();
    }

    // RESTAURADO LOGICO
    public Mono<Void> restorePerson(Integer id) {
        return personRepository.updateStateById(id, "A")
                .then();
    }

    // ACTUALIZA LOS REGISTROS DE EDUCATION Y HEALTH CON NUEVO IDS
    public Mono<Void> updatePersonWithNewIds(PersonDTO personDTO) {
        WebClient webClient = webClientBuilder.build();
    
      
        Mono<Void> educationUpdate = Mono.empty();
        if (personDTO.getEducation() != null && !personDTO.getEducation().isEmpty()) {
            EducationDTO education = personDTO.getEducation().get(0);
    
            educationUpdate = webClient.put()
                .uri(EDUCATION_SERVICE_BASE_URL + "/education//update-with-history/" + education.getIdEducation())
                .bodyValue(education)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    System.err.println("Error al registrar educación: " + e.getMessage());
                    return Mono.empty(); 
                });
        }
    
        Mono<Void> healthUpdate = Mono.empty();
        if (personDTO.getHealth() != null && !personDTO.getHealth().isEmpty()) {
                HealthDTO health = personDTO.getHealth().get(0); 
    
                healthUpdate = webClient.put()
                    .uri( HEALTH_SERVICE_BASE_URL + "/health//update-with-history/" + health.getIdHealth())
                    .bodyValue(health)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        System.err.println("Error al registrar educación: " + e.getMessage());
                        return Mono.empty(); 
                    });
        }
    
        return Mono.when( educationUpdate, healthUpdate).then();
    }
    

    // EDITAR DATOS PERSONALES SIN GENERAR NUEVO ID
    public Mono<Void> updatePersonData(PersonDTO personDTO) {
        return personRepository.updatePerson(
                personDTO.getIdPerson(),
                personDTO.getName(),
                personDTO.getSurname(),
                personDTO.getAge(),
                personDTO.getBirthdate(),
                personDTO.getTypeDocument(),
                personDTO.getDocumentNumber(),
                personDTO.getTypeKinship(),
                personDTO.getSponsored(),
                personDTO.getState(),
                personDTO.getFamilyId()).then();
    }

    // MODIFICA EDUCATION Y HEALT SIN GENERAR UN NUEVO ID
    public Mono<Void> correctEducationAndHealth(PersonDTO personDTO) {
        WebClient webClient = webClientBuilder.build();
    
        Mono<Void> updateEducationMono = Mono.empty();
    
        if (personDTO.getEducation() != null && !personDTO.getEducation().isEmpty()) {
            EducationDTO education = personDTO.getEducation().get(0); // Usamos el nombre correcto
    
            updateEducationMono = webClient.put()
                .uri(EDUCATION_SERVICE_BASE_URL + "/education/update/" + education.getIdEducation())
                .bodyValue(education)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    System.err.println("Error al registrar educación: " + e.getMessage());
                    return Mono.empty(); 
                });
        }
    
        Mono<Void> updateHealthMono = Mono.empty();
    
        if (personDTO.getHealth() != null && !personDTO.getHealth().isEmpty()) {
               HealthDTO health = personDTO.getHealth().get(0); 
    
                updateHealthMono = webClient.put()
                    .uri(HEALTH_SERVICE_BASE_URL + "/health/update/" + health.getIdHealth())
                    .bodyValue(health)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(e -> {
                        System.err.println("Error al registrar educación: " + e.getMessage());
                        return Mono.empty(); 
                    });
            
        }
    
        return Mono.when(updateEducationMono, updateHealthMono).then();
    }
    

    // REGISTRA NUEVA PERSONA CON SUS DATOS DE EDUCATION Y HEALTH
    public Mono<Void> registerPerson(PersonDTO personDTO) {
        return personRepository.insertPerson(
                personDTO.getName(),
                personDTO.getSurname(),
                personDTO.getAge(),
                personDTO.getBirthdate(),
                personDTO.getTypeDocument(),
                personDTO.getDocumentNumber(),
                personDTO.getTypeKinship(),
                personDTO.getSponsored(),
                personDTO.getState(),
                personDTO.getFamilyId())
            .then(personRepository.getLastInsertedId()) 
            .flatMap(personId -> {
    
                Flux<Mono<Void>> educationRequests = Flux.fromIterable(personDTO.getEducation())
                    .map(edu -> {
                        edu.setPersonId(personId);
                        return webClientBuilder.build()
                            .post()
                            .uri(EDUCATION_SERVICE_BASE_URL + "/education")
                            .bodyValue(edu)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .onErrorResume(e -> {
                                System.err.println("Error al registrar educación: " + e.getMessage());
                                return Mono.empty();
                            });
                    });
    
                Flux<Mono<Void>> healthRequests = Flux.fromIterable(personDTO.getHealth())
                    .map(health -> {
                        health.setPersonId(personId);
                        return webClientBuilder.build()
                            .post()
                            .uri(HEALTH_SERVICE_BASE_URL + "/health") 
                            .bodyValue(health)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .onErrorResume(e -> {
                                System.err.println("Error al registrar educación: " + e.getMessage());
                                return Mono.empty();
                            });
                            
                    });
    
                return Flux.merge(educationRequests)
                        .thenMany(Flux.merge(healthRequests))
                        .then();
            });
    }
    
    
    

    private PersonDTO convertToDTO(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setIdPerson(person.getIdPerson());
        dto.setName(person.getName());
        dto.setSurname(person.getSurname());
        dto.setAge(person.getAge());
        dto.setBirthdate(person.getBirthdate());
        dto.setTypeDocument(person.getTypeDocument());
        dto.setDocumentNumber(person.getDocumentNumber());
        dto.setTypeKinship(person.getTypeKinship());
        dto.setSponsored(person.getSponsored());
        dto.setState(person.getState());
        dto.setFamilyId(person.getFamilyId());
        return dto;
    }

}
