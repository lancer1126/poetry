package fun.lance.poetry.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeilisearchConfig {

    @Value("${meilisearch.host-url}")
    private String hostUrl;

    @Value("${meilisearch.api-key}")
    private String apiKey;

    @Bean
    @ConditionalOnMissingBean
    public Client initMeiliCLient() {
        return new Client(new Config(hostUrl, apiKey));
    }

}
