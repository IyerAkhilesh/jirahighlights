package com.jirahighlights.chatbotservice.service;

import com.jirahighlights.chatbotservice.model.ConversationHistory;
import com.jirahighlights.chatbotservice.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ChatbotService {

    private final ConversationRepository conversationRepository;
    private final WebClient jiraWebClient;
    private final WebClient nlpWebClient;

    public ChatbotService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
        this.jiraWebClient = WebClient.builder().baseUrl("http://jiraintegratorservice").build();
        this.nlpWebClient = WebClient.builder().baseUrl("http://nlpservice").build();
    }

    public Mono<String> processQuery(String userQuery) {
        // Simple logic to extract ticket ID from a user query (e.g., "summarize PROJ-123")
        String ticketId = userQuery.split(" ")[1];

        return jiraWebClient.get().uri("/api/jira/ticket/{id}", ticketId)
            .retrieve()
            .bodyToMono(String.class) // Assuming Jira returns a simple text for now
            .flatMap(jiraData -> nlpWebClient.post().uri("/api/nlp/sentences").bodyValue(jiraData).retrieve().bodyToMono(String.class))
            .doOnSuccess(processedResponse -> {
                ConversationHistory history = new ConversationHistory();
                history.setUserQuery(userQuery);
                history.setBotResponse(processedResponse);
                history.setTimestamp(LocalDateTime.now());
                conversationRepository.save(history);
            });
    }
}