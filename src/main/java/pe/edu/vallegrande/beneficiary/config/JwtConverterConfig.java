package pe.edu.vallegrande.beneficiary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;

import reactor.core.publisher.Flux;

@Configuration
public class JwtConverterConfig {
    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();

        // Extraer roles del JWT y convertirlos a authorities
        converter.setJwtGrantedAuthoritiesConverter((Jwt jwt) -> {
            String role = jwt.getClaimAsString("role");
            if (role == null || role.trim().isEmpty()) {
                return Flux.empty();  // Sin rol = sin permisos
            }

            // Crear authority con prefijo ROLE_
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
            return Flux.just(authority);
        });

        return converter;
    }
}
