package pe.edu.vallegrande.beneficiary.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class JwtDecoderConfig {
    @Value("${JWK_SET_URI}")
        private String jwkSetUriVar;

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        String jwkSetUri = jwkSetUriVar;
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
