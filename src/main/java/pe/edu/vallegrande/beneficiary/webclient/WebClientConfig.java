package pe.edu.vallegrande.beneficiary.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    // URL DEL MICROSERVICIO HEALTH 
    @Value("${SERVICIES_HEALTH}")
    private String healthBaseUrl;

    // URL DEL MICROSERVICIO EDUCATION
    @Value("${SERVICIES_EDUCATION}")
    private String educationBaseUrl;

    
    // PROPAGA EL TOKEN DE EDUCATION
    @Bean
    public WebClient educationServiceWebClient() {
        return WebClient.builder()
                .baseUrl(educationBaseUrl)
                .filter(authHeaderFilter())
                .build();
    }

    // PROPAGA EL TOKEN DE HEALTH
    @Bean
    public WebClient healthServiceWebClient() {
        return WebClient.builder()
                .baseUrl(healthBaseUrl)
                .filter(authHeaderFilter())
                .build();
    }

    // FILTRO DE PROPAGACION
    private ExchangeFilterFunction authHeaderFilter() {
        return (request, next) -> Mono.deferContextual(ctx -> {
            if (ctx.hasKey("Authorization")) {
                String token = ctx.get("Authorization");
                return next.exchange(
                    ClientRequest.from(request)
                        .headers(headers -> headers.setBearerAuth(token))
                        .build()
                );
            }
            return next.exchange(request);
        });
    }

    //VERIFICA EL TOKEN EN CADA PETICIÓN
    @Bean
    public ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request Headers: " + clientRequest.headers());
            return Mono.just(clientRequest);
        });
    }


}
