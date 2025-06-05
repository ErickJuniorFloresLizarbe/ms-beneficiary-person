package pe.edu.vallegrande.beneficiary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class AuthContextWebFilter {

    @Bean
    public WebFilter jwtTokenPropagationFilter() {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                return chain.filter(exchange).contextWrite(ctx -> ctx.put("Authorization", token));
            }
            return chain.filter(exchange);
        };
    }

}