package pe.edu.vallegrande.beneficiary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.vallegrande.beneficiary.dto.PersonDTO;
import pe.edu.vallegrande.beneficiary.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/persons")
@CrossOrigin(origins = "*")
public class PersonController {

    @Autowired
    private PersonService personService;

    //LISTADO DE ACTIVOS Y INACTIVOS BENEFICIARIOS Y APADRINADOS
    @GetMapping("/filter")
    public Flux<PersonDTO> getPersonsByTypeKinshipAndState(
            @RequestParam String typeKinship,
            @RequestParam String state) {
        return personService.getPersonsByTypeKinshipAndState(typeKinship, state);
    }


    //LISTADO DE SOLO APADRINADOS
    @GetMapping("/filter-sponsored")
    public Flux<PersonDTO> getPersonsBySponsoredAndState(
            @RequestParam String sponsored,
            @RequestParam String state) {
        return personService.getPersonsBySponsoredAndState(sponsored, state);
    }

    //LISTADO POR ID DE LOS BENEFICIARIOS
    @GetMapping("/{id}/details")
    public Mono<PersonDTO> getPersonByIdWithDetails(@PathVariable Integer id) {
        return personService.getPersonByIdWithDetails(id);
    }

    //ELIMINADO LOGICO
    @DeleteMapping("/{id}/delete")
    public Mono<Void> deletePerson(@PathVariable Integer id) {
        return personService.deletePerson(id);
    }

    //RESTAURADO LOGICO
    @PutMapping("/{id}/restore")
    public Mono<Void> restorePerson(@PathVariable Integer id) {
        return personService.restorePerson(id);
    }

    //EDITA LOS REGISTROS DE EDUCATION Y HEALTH CON NUEVO IDS
    @PutMapping("/{id}/update")
    public Mono<Void> updatePerson(@PathVariable Integer id, @RequestBody PersonDTO personDTO) {
        return personService.updatePersonWithNewIds(personDTO);
    }
    
    //EDITAR DATOS PERSONALES SIN GENERAR NUEVO ID
    @PutMapping("/{id}/update-person")
    public Mono<Void> updatePersonData(@PathVariable Integer id, @RequestBody PersonDTO personDTO) {
        return personService.updatePersonData(personDTO);
    }

    //MODIFICA EDUCATION Y HEALT SIN GENERAR UN NUEVO ID
    @PutMapping("/{id}/correct-education-health")
    public Mono<Void> correctEducationAndHealth(@PathVariable Integer id, @RequestBody PersonDTO personDTO) {
        return personService.correctEducationAndHealth(personDTO);
    }

    @PostMapping("/register")
    public Mono<Void> registerPerson(@RequestBody PersonDTO personDTO) {
        return personService.registerPerson(personDTO);
    }
}