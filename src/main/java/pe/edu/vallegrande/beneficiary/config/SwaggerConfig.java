package pe.edu.vallegrande.beneficiary.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .servers(Collections
                        .singletonList(new Server().url("https://shiny-pancake-vxvg696x476fx696-8085.app.github.dev/")))
                .info(new Info()

                        .title("TRADUCTOR")
                        .description("AS222S5_T03_be")
                        .version("1.0.0")

                );
    }
}