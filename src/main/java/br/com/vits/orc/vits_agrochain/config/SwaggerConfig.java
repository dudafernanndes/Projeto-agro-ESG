package br.com.vits.orc.vits_agrochain.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

public class SwaggerConfig {

    OpenAPI config() {
        return new OpenAPI()
                .info(new Info()
                        .title("Verdantis - VITS API")
                        .version("1.0.0")
                        .description("API de rastreabilidade e visualização inteligente para o agronegócio brasileiro. " +
                                "Sistema que conecta produtores, distribuidores e compradores, permitindo registro, " +
                                "visualização e certificação de cadeias produtivas agrícolas.")
                        .contact(new Contact()
                                .name("Equipe GreenCore")
                                .url("https://github.com/joaoGFG/VitsAPI")));
    }
}
