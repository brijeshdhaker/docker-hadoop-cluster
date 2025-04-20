package org.examples.sb.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author brijeshdhaker
 */
@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:9080/api/v1");
        server.setDescription("Development");

        Server k8s_http = new Server();
        k8s_http.setUrl("http://sbhttps.sandbox.net/api/v1");
        k8s_http.setDescription("Kubernetes Http");

        Server k8s_https = new Server();
        k8s_https.setUrl("https://sbhttps.sandbox.net/api/v1");
        k8s_https.setDescription("Kubernetes Https");

        Contact myContact = new Contact();
        myContact.setName("Brijesh K Dhaker");
        myContact.setEmail("brijeshdhaker@gmail.com");

        Info information = new Info()
                .title("Sandbox Services")
                .version("1.0")
                .description("This API exposes endpoints to manage sandbox APIs.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server, k8s_http, k8s_https));
    }

}
