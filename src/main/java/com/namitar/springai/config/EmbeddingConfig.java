package com.namitar.springai.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfig {

    @Bean
    public ModelManagementOptions ollamaModelManagementOptions() {
        return ModelManagementOptions.builder().build();
    }

    @Bean
    public ObservationRegistry observationRegistry(){
        return ObservationRegistry.create();
    }

    @Bean("openAiEmbedding")
    public EmbeddingModel openAiEmbeddingModel(OpenAiApi openAiApi) {
        return new OpenAiEmbeddingModel(openAiApi);
    }

    @Bean("ollamaEmbedding")
    public EmbeddingModel ollamaEmbeddingModel(
            OllamaApi ollamaApi,
            ObservationRegistry observationRegistry,
            ModelManagementOptions modelManagementOptions
    ) {
        OllamaEmbeddingOptions options = OllamaEmbeddingOptions.builder()
                .model("nomic-embed-text")
                .build();

        return new OllamaEmbeddingModel(
                ollamaApi,
                options,
                observationRegistry,
                modelManagementOptions
        );
    }

}
