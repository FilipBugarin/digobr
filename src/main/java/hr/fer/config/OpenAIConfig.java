package hr.fer.config;

import hr.fer.common.OpenAIRequestConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAIConfig {

    private final OpenAIRequestConstants openAIRequestConstants;

    public OpenAIConfig(OpenAIRequestConstants openAIRequestConstants) {
        this.openAIRequestConstants = openAIRequestConstants;
    }

    @Bean
    public RestTemplate template() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openAIRequestConstants.API_KEY);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
