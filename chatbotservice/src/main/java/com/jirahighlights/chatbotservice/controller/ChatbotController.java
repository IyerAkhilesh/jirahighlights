package com.jirahighlights.chatbotservice.controller; 

import com.jirahighlights.chatbotservice.service.ChatbotService;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.tokenize.TokenizerModel;

import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
    private static final String CATEGORY_MODEL_PATH = "opennlp-models/category-model-jira.bin";
    private static final String TOKEN_MODEL_PATH = "opennlp-models/token-model.bin";
    private DoccatModel categoryModel;
    private TokenizerModel tokenizerModel;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
        try (InputStream categoryModelIn = new FileInputStream(CATEGORY_MODEL_PATH);
        InputStream tokenizerModelIn = new FileInputStream(TOKEN_MODEL_PATH)) {
            this.categoryModel = new DoccatModel(categoryModelIn);
            this.tokenizerModel = new TokenizerModel(tokenizerModelIn);
        } catch (IOException io) {
            io.printStackTrace();
            this.categoryModel = null;
            this.tokenizerModel = null;
        }
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