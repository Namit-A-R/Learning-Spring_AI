package com.namitar.springai.controller.section2;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ChatController {

    private final ChatClient.Builder openAiChatClientBuilder;
    private final ChatClient.Builder ollamaAiChatClientBuilder;

    public ChatController(
            @Qualifier("openAiChatClientBuilder") ChatClient.Builder openAiChatClientBuilder,
            @Qualifier("ollamaAiChatClientBuilder") ChatClient.Builder ollamaAiChatClientBuilder)
    {
        String defaultSystemMessage =
                """
                     You are an internal HR assistant. Your role is to help \s
                     employees with questions related to HR policies, such as \s
                     leave policies, working hours, benefits, and code of conduct.\s
                     If a user asks for help with anything outside of these topics, \s
                     kindly inform them that you can only assist with queries related to \s
                     HR policies.
                     """;

        String defaultUserMessage = "How can you help me?";

        this.openAiChatClientBuilder = openAiChatClientBuilder.clone()
                .defaultSystem(defaultSystemMessage)
                .defaultUser(defaultUserMessage);

        this.ollamaAiChatClientBuilder = ollamaAiChatClientBuilder.clone()
                .defaultSystem(defaultSystemMessage)
                .defaultUser(defaultUserMessage);
    }


    @GetMapping("/chat")
    public String openAIChat(@RequestParam("message") String message){
        return ollamaAiChatClientBuilder
                .build()
                .prompt(message)
                .call()
                .content();
    }
}