package br.com.klein.config;


import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name = "Pix klein", description = "Projeto inicial de geração de pix.")
        },
        info = @Info(
          title = "Pix",
          version = "1.0.0",
          contact = @Contact(
                  name = "Klein",
                  url = "https://www.linkedin.com/in/gabriel-klein10/",
                  email = "gabrielklein289@hotmail.com"),
        license = @License(
                name = "Klein",
                url = "https://www.linkedin.com/in/gabriel-klein10/"))
)
public class OpenAPIConfig extends Application {
}
