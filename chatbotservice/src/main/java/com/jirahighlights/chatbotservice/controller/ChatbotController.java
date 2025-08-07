package com.jirahighlights.chatbotservice.controller; 

import com.jirahighlights.chatbotservice.service.ChatbotService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping("/message")
    public String getMessage(@RequestParam String responseMessage) {
        return "The answer to your question is as follows - \n" + responseMessage;
    }

    @PostMapping("/message")
    public Mono<String> processMessage(@RequestBody String userMessage) {
        return chatbotService.processQuery(userMessage);
    }
}