package pe.edu.vallegrande.beneficiary.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.beneficiary.dto.PersonDTO;
import pe.edu.vallegrande.beneficiary.model.Person;
import pe.edu.vallegrande.beneficiary.repository.PersonRepository;
import pe.edu.vallegrande.beneficiary.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    private PersonRepository personRepository;
    private WebClient.Builder webClientBuilder;
    private PersonService personService;

    @BeforeEach
    public void setUp() {
        personRepository = Mockito.mock(PersonRepository.class);
        webClientBuilder = Mockito.mock(WebClient.Builder.class);
        personService = new PersonService();

        // Inyección manual (simulando @Autowired)
        personService = new PersonService();
        personService.getClass().getDeclaredFields();

        // Usar reflexión si quieres simular inyecciones privadas
        try {
            var repoField = personService.getClass().getDeclaredField("personRepository");
            repoField.setAccessible(true);
            repoField.set(personService, personRepository);

            var webClientField = personService.getClass().getDeclaredField("webClientBuilder");
            webClientField.setAccessible(true);
            webClientField.set(personService, webClientBuilder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //LISTADO DE TODOS BENEFICIARIOS Y APADRINADOS (PARENTESCO / ESTADO)
    @Test
    public void testGetPersonsByTypeKinshipAndState() {
        Person person = new Person();
        person.setIdPerson(1);
        person.setName("Erick Junior");
        person.setSurname("Flores Lizarbe");
        person.setAge(10);
        person.setBirthdate(LocalDate.of(2015, 5, 23));
        person.setTypeDocument("DNI");
        person.setDocumentNumber("83827893");
        person.setTypeKinship("HIJO");
        person.setSponsored("NO");
        person.setState("A");
        person.setFamilyId(2);

        when(personRepository.findByTypeKinshipAndState("HIJO", "A"))
                .thenReturn(Flux.just(person));

        Flux<PersonDTO> result = personService.getPersonsByTypeKinshipAndState("HIJO", "A");

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getIdPerson() == 1 &&
                        dto.getName().equals("Erick Junior") &&
                        dto.getSurname().equals("Flores Lizarbe") &&
                        dto.getAge() == 10 &&
                        dto.getBirthdate().equals(LocalDate.of(2015, 5, 23)) &&
                        dto.getTypeDocument().equals("DNI") &&
                        dto.getDocumentNumber().equals("83827893") &&
                        dto.getTypeKinship().equals("HIJO") &&
                        dto.getSponsored().equals("NO") &&
                        dto.getState().equals("A") &&
                        dto.getFamilyId() == 2)
                .verifyComplete();
    }


    //ELIMINADO LOGICO POR ID (BENEFICIARIO / APADRINADO)
    @Test
    public void testDeletePerson() {
        when(personRepository.updateStateById(1, "I"))
                .thenReturn(Mono.just(1));

        Mono<Void> result = personService.deletePerson(1);

        StepVerifier.create(result)
                .verifyComplete();
    }

    //RESTAURACION LOGICO POR ID (BENEFICIARIO / APADRINADO)
    @Test
    public void testRestorePerson() {
        when(personRepository.updateStateById(1, "A"))
                .thenReturn(Mono.just(1));

        Mono<Void> result = personService.restorePerson(1);

        StepVerifier.create(result)
                .verifyComplete();
    }

    //ACTUALIZACION DE DATOS DE BENEFICIARIOS / APADRINADOS
    @Test
    public void testUpdatePersonData() {
        PersonDTO dto = new PersonDTO();
        dto.setIdPerson(1);
        dto.setName("Luis");
        dto.setSurname("Tasayco");
        dto.setAge(20);
        dto.setBirthdate(LocalDate.of(2004, 1, 15));
        dto.setTypeDocument("DNI");
        dto.setDocumentNumber("12345678");
        dto.setTypeKinship("Hijo");
        dto.setSponsored("NO");
        dto.setState("A");
        dto.setFamilyId(1);

        when(personRepository.updatePerson(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(1));

        Mono<Void> result = personService.updatePersonData(dto);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
