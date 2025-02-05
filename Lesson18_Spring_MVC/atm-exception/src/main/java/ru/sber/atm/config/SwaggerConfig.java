package ru.sber.atm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "ATM Terminal", version = "1.0",
        description = "ATM проверочный API", contact = @Contact(
        name = "Michail K", email = "t9034049980@gmail.com"
), license = @License(name = "MIT Licence",
        url = "https://opensource.org/licenses/mit-license.php")))
public class SwaggerConfig {
    // http://localhost:8080/swagger-ui/index.html#/
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI();
    }
}
