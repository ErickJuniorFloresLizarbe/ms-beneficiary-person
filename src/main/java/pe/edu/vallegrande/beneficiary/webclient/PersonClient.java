package pe.edu.vallegrande.beneficiary.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.beneficiary.dto.EducationDTO;
import pe.edu.vallegrande.beneficiary.dto.HealthDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PersonClient {

    @Autowired
    private WebClient educationServiceWebClient;

    @Autowired
    private WebClient healthServiceWebClient;

    public Flux<EducationDTO> getEducationByPersonId(Integer personId) {
        return educationServiceWebClient.get()
                .uri("/education/person/{id}", personId)
                .retrieve()
                .bodyToFlux(EducationDTO.class);
    }

    public Flux<HealthDTO> getHealthByPersonId(Integer personId) {
        return healthServiceWebClient.get()
                .uri("/health/person/{id}", personId)
                .retrieve()
                .bodyToFlux(HealthDTO.class);
    }

    public Mono<Void> registerEducation(EducationDTO education) {
        return educationServiceWebClient.post()
                .uri("/education")
                .bodyValue(education)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> registerHealth(HealthDTO health) {
        return healthServiceWebClient.post()
                .uri("/health")
                .bodyValue(health)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> updateEducation(EducationDTO education) {
        return educationServiceWebClient.put()
                .uri("/education/update/{id}", education.getIdEducation())
                .bodyValue(education)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> updateHealth(HealthDTO health) {
        return healthServiceWebClient.put()
                .uri("/health/update/{id}", health.getIdHealth())
                .bodyValue(health)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
