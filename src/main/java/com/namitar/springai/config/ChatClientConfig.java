package com.namitar.springai.config;

import com.namitar.springai.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){
        return ChatClient.create(openAiChatModel);
    }

    @Bean
    public ChatClient ollamaAiChatClient(OllamaChatModel ollamaChatModel){
        ChatClient.Builder chatclientBuilder =  ChatClient.builder(ollamaChatModel);
        return chatclientBuilder.build();
    }

    @Bean
    public ChatClient.Builder openAiChatClientBuilder(OpenAiChatModel openAiChatModel){
        ChatOptions openAiChatModelOptions = ChatOptions.builder().model("gpt-4o-mini")
                .maxTokens(100)
                .temperature(0.7)
                .build();

        return ChatClient.builder(openAiChatModel)
                .defaultOptions(openAiChatModelOptions)
                .defaultAdvisors(List.of(new TokenUsageAuditAdvisor(), new SimpleLoggerAdvisor()));
    }

    @Bean
    public ChatClient.Builder ollamaAiChatClientBuilder(OllamaChatModel ollamaChatModel){
        ChatOptions ollamaChatModelOptions = ChatOptions.builder().model("llama3.2")
                .maxTokens(100)
                .temperature(0.7)
                .build();

        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(List.of(new TokenUsageAuditAdvisor(), new SimpleLoggerAdvisor()));
    }
}
