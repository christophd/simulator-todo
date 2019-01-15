package io.syndesis.simulator;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Christoph Deppisch
 */
@Configuration
public class SimulatorConfig {

    @Bean
    public HttpClient todoClient() {
        return CitrusEndpoints.http()
                .client()
                .requestUrl("http://localhost:8080/todo/api")
                .build();
    }
}
