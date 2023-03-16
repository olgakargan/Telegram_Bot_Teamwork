package pro.sky.telegrambotteamwork.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Pet Shelter Bot Api",
                description = "Shelter Telegram Bot", version = "1.1.0",
                contact = @Contact(
                        name = "Java_SkyDevelopers-10_8"
                )
        )
)
public class OpenApiConfig {
}
