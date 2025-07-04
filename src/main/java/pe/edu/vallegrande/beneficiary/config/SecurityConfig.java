package pe.edu.vallegrande.beneficiary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/actuator/**").permitAll()
                        
                         // API endpoints con autenticación
                        // GET: accesible por USER y ADMIN
                        .pathMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER")

                        // POST, PUT, DELETE: solo ADMIN
                        .pathMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource))
                .build();
    }
}