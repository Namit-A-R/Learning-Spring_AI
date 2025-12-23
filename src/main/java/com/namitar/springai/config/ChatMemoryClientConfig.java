package com.namitar.springai.config;

import com.namitar.springai.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatMemoryClientConfig {

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository){
        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
    }

    @Bean("openAiChatMemoryChatClientBuilder")
    public ChatClient.Builder openAiChatClientBuilder(OpenAiChatModel openAiChatModel,
                                                      ChatMemory chatMemory,
                                                      @Qualifier("openAiVectorStore")
                                                      VectorStore vectorStore){

        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        ChatOptions openAiChatModelOptions = ChatOptions.builder().model("gpt-4o-mini")
                .maxTokens(100)
                .temperature(0.7)
                .build();


        return ChatClient.builder(openAiChatModel)
                .defaultOptions(openAiChatModelOptions)
                .defaultAdvisors(List.of(
                        new TokenUsageAuditAdvisor(),
                        new SimpleLoggerAdvisor(),
                        memoryAdvisor,
                        retrievalAugmentationAdvisor(vectorStore)));
    }

    @Bean("ollamaAiChatMemoryChatClientBuilder")
    public ChatClient.Builder ollamaAiChatClientBuilder(OllamaChatModel ollamaChatModel,
                                                        ChatMemory chatMemory,
                                                        @Qualifier("ollamaVectorStore")
                                                        VectorStore vectorStore){

        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(List.of(
                        new TokenUsageAuditAdvisor(),
                        new SimpleLoggerAdvisor(),
                        memoryAdvisor,
                        retrievalAugmentationAdvisor(vectorStore)));
    }

    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore){
        return RetrievalAugmentationAdvisor
                .builder()
                    .documentRetriever(
                            VectorStoreDocumentRetriever.builder()
                                    .vectorStore(vectorStore)
                                    .topK(3)
                                    .similarityThreshold(0.5)
                                    .build()
                    ).build();
    }
}
