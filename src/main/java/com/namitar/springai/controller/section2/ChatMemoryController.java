package com.namitar.springai.controller.section2;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RequestMapping("/api")
@RestController
public class ChatMemoryController {

    private final ChatClient.Builder openAiChatClientBuilder;
    private final ChatClient.Builder ollamaAiChatClientBuilder;

    public ChatMemoryController(
            @Qualifier("openAiChatMemoryChatClientBuilder") ChatClient.Builder openAiChatClientBuilder,
            @Qualifier("ollamaAiChatMemoryChatClientBuilder") ChatClient.Builder ollamaAiChatClientBuilder)
    {

        this.openAiChatClientBuilder = openAiChatClientBuilder;
        this.ollamaAiChatClientBuilder = ollamaAiChatClientBuilder;

    }

    @GetMapping("/chatMemory")
    public String openAIChat(@RequestParam("message") String message, @RequestParam("username") String username){
        return ollamaAiChatClientBuilder
                .build()
                .prompt()
                .user(message)
                .advisors(
                        advisorSpec -> advisorSpec.param(CONVERSATION_ID, username)
                )
                .call()
                .content();
    }

}
