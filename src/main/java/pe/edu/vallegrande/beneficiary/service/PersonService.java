package pe.edu.vallegrande.beneficiary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.vallegrande.beneficiary.dto.EducationDTO;
import pe.edu.vallegrande.beneficiary.dto.HealthDTO;
import pe.edu.vallegrande.beneficiary.dto.PersonDTO;
import pe.edu.vallegrande.beneficiary.model.Person;
import pe.edu.vallegrande.beneficiary.repository.PersonRepository;
import pe.edu.vallegrande.beneficiary.webclient.PersonClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonClient personClient;

    
    public Flux<PersonDTO> getPersonsByTypeKinshipAndState(String typeKinship, String state) {
        return personRepository.findByTypeKinshipAndState(typeKinship, state)
                .map(this::convertToDTO);
    }

    public Flux<PersonDTO> getPersonsBySponsoredAndState(String sponsored, String state) {
        return personRepository.findBySponsoredAndState(sponsored, state)
                .map(this::convertToDTO)
                .filter(personDTO -> "HIJO".equals(personDTO.getTypeKinship()));
    }

    public Mono<PersonDTO> getPersonByIdWithDetails(Integer id) {
        return personRepository.findById(id)
                .flatMap(person -> {
                    PersonDTO dto = convertToDTO(person);

                    Mono<List<EducationDTO>> educationMono = personClient.getEducationByPersonId(person.getIdPerson())
                            .collectList();

                    Mono<List<HealthDTO>> healthMono = personClient.getHealthByPersonId(person.getIdPerson())
                            .collectList();

                    return Mono.zip(educationMono, healthMono)
                            .map(tuple -> {
                                dto.setEducation(tuple.getT1());
                                dto.setHealth(tuple.getT2());
                                return dto;
                            });
                });
    }

    public Mono<Void> deletePerson(Integer id) {
        return personRepository.updateStateById(id, "I")
                .then();
    }

    public Mono<Void> restorePerson(Integer id) {
        return personRepository.updateStateById(id, "A")
                .then();
    }

    public Mono<Void> updatePersonWithNewIds(PersonDTO personDTO) {
        Mono<Void> educationUpdate = Mono.empty();
        if (personDTO.getEducation() != null && !personDTO.getEducation().isEmpty()) {
            EducationDTO education = personDTO.getEducation().get(0);
            if (education.getIdEducation() == null) {
                education.setPersonId(personDTO.getIdPerson());
                educationUpdate = personClient.registerEducation(education);
            } else {
                educationUpdate = personClient.updateEducation(education);
            }
        }

        Mono<Void> healthUpdate = Mono.empty();
        if (personDTO.getHealth() != null && !personDTO.getHealth().isEmpty()) {
            HealthDTO health = personDTO.getHealth().get(0);
            if (health.getIdHealth() == null) {
                health.setPersonId(personDTO.getIdPerson());
                healthUpdate = personClient.registerHealth(health);
            } else {
                healthUpdate = personClient.updateHealth(health);
            }
        }

        return Mono.when(educationUpdate, healthUpdate).then();
    }

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

    public Mono<Void> correctEducationAndHealth(PersonDTO personDTO) {
        Mono<Void> updateEducationMono = Mono.empty();

        if (personDTO.getEducation() != null && !personDTO.getEducation().isEmpty()) {
            EducationDTO education = personDTO.getEducation().get(0);
            updateEducationMono = personClient.updateEducation(education);
        }

        Mono<Void> updateHealthMono = Mono.empty();

        if (personDTO.getHealth() != null && !personDTO.getHealth().isEmpty()) {
            HealthDTO health = personDTO.getHealth().get(0);
            updateHealthMono = personClient.updateHealth(health);
        }

        return Mono.when(updateEducationMono, updateHealthMono).then();
    }

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
                                return personClient.registerEducation(edu);
                            });

                    Flux<Mono<Void>> healthRequests = Flux.fromIterable(personDTO.getHealth())
                            .map(health -> {
                                health.setPersonId(personId);
                                return personClient.registerHealth(health);
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
