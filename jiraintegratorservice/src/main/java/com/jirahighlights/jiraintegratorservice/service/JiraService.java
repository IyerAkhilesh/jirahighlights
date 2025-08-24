package com.jirahighlights.jiraintegratorservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.jirahighlights.jiraintegratorservice.JiraApiProperties;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JiraService {

    private final WebClient webClient;
    private final JiraApiProperties jiraApiProperties;
    // ObjectMapper for JSON serialization/deserialization
    private final ObjectMapper objectMapper;

    public JiraService(WebClient.Builder webClientBuilder, JiraApiProperties jiraApiProperties, ObjectMapper objectMapper) {
        this.jiraApiProperties = jiraApiProperties;
        this.objectMapper = objectMapper;
        // Configure the WebClient with the base URL and authentication headers
        this.webClient = webClientBuilder.baseUrl(jiraApiProperties.getUrl()+"/rest/api/2")
                .defaultHeaders(header -> header.setBasicAuth(jiraApiProperties.getUsername(), jiraApiProperties.getToken()))
                .build();
    }

    /**
     * Creates a specified number of random Jira tickets asynchronously and concurrently.
     * After creating the ticket, it will add a comment if one is provided.
     *
     * @param count The number of tickets to create.
     * @param projectKey The key of the Jira project to create tickets in.
     * @param issueType The specific issue type to create (optional).
     * @param comment The comment to add to each ticket (optional).
     * @return A Mono that completes once all tickets and their comments have been created.
     */
    public Mono<Void> createRandomTickets(int count, String projectKey, String issueType, String comment) {
        final int BATCH_SIZE = 10;
        final int DELAY_SECONDS = 5;
        // Create a Flux that emits a sequence of numbers from 1 to 'count'.
        return Flux.range(1, count)
                // Buffer the emitted numbers into batches.
                .buffer(BATCH_SIZE)
                // Introduce a delay between each batch to avoid overwhelming the Jira API.
                .delayElements(Duration.ofSeconds(DELAY_SECONDS))
                // Log the start of each batch.
                .doOnNext(batch -> System.out.println("Starting a new batch " + batch + " of size " + batch.size() + " with a delay of " + DELAY_SECONDS + " seconds"))
                // Process each batch.
                .flatMap(batch -> Flux.fromIterable(batch)
                    // Use flatMap to process each number in the Flux.
                    .flatMap(i -> {
                        // Generate random data for the ticket.
                        String randomSummary = "Auto-generated Ticket " + UUID.randomUUID();
                        String finalIssueType = (issueType != null && !issueType.isEmpty()) ? issueType : getRandomIssueType();
                        
                        // Create the JSON payload for the new Jira ticket.
                        String jsonPayload = createJiraTicketJson(randomSummary, projectKey, finalIssueType);
                        System.out.println("Generated JSON Payload: " + jsonPayload);

                        // Step 1: Create the Jira ticket.
                        // The bodyToMono(JsonNode.class) returns the response, which contains the new ticket's ID.
                        return webClient.post()
                                .uri("/issue")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .body(BodyInserters.fromValue(jsonPayload))
                                .retrieve()
                                .bodyToMono(JsonNode.class)
                                // Step 2: Add the comment (if provided) using the ID from the previous step.
                                .flatMap(response -> {
                                    String issueKey = response.get("key").asText();
                                    System.out.println("Ticket " + issueKey + " created successfully.");

                                    // Only make a second API call if a comment was provided.
                                    if (comment != null && !comment.trim().isEmpty()) {
                                        return addCommentToTicket(issueKey, comment)
                                                .doOnSuccess(v -> System.out.println("Comment added to ticket " + issueKey + "."))
                                                .doOnError(e -> System.err.println("Failed to add comment to ticket " + issueKey + ": " + e.getMessage()));
                                    } else {
                                        // If no comment is provided, return an empty Mono to continue the stream.
                                        return Mono.empty();
                                    }
                                })
                                // Catch any errors in the entire sequence (create ticket or add comment).
                                .doOnError(error -> System.err.println("Failed to create ticket: " + error.getMessage()))
                                // Return an empty Mono so flatMap can continue to the next item in the Flux.
                                .onErrorResume(e -> Mono.empty());
                    })
                )
                // Use then() to return a Mono<Void> that completes after all items in the Flux have been processed.
                .then();
    }
    
    /**
     * Helper method to add a comment to an existing Jira ticket.
     * @param issueKey The key of the ticket to comment on.
     * @param comment The comment text.
     * @return A Mono that completes once the comment has been added.
     */
    private Mono<Void> addCommentToTicket(String issueKey, String comment) {
        String commentPayload = String.format("{\"body\": {\"content\": [{\"type\": \"paragraph\", \"content\": [{\"type\": \"text\", \"text\": \"%s\"}]}]}}", comment);
        
        return webClient.post()
                .uri("/issue/" + issueKey + "/comment")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(commentPayload))
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    System.err.println("Error adding comment to " + issueKey + ": " + e.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * Helper method to generate a random issue type.
     * You may need to update this based on the issue types available in your Jira project.
     * @return a random issue type name.
     */
    private String getRandomIssueType() {
        List<String> issueTypes = List.of("Bug", "Task", "Story");
        return issueTypes.get(ThreadLocalRandom.current().nextInt(issueTypes.size()));
    }

    /**
     * Helper method to construct the JSON body for creating a Jira ticket.
     *
     * @param summary The ticket summary.
     * @param projectKey The key of the Jira project.
     * @param issueType The type of the issue (e.g., "Task", "Bug").
     * @return A JSON string for the request body.
     */
    private String createJiraTicketJson(String summary, String projectKey, String issueType) {
        try {
            Map<String, Object> fields = Map.of(
                "project", Map.of("key", projectKey),
                "summary", summary,
                "issuetype", Map.of("name", issueType)
            );
            Map<String, Object> payload = Map.of("fields", fields);
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing JSON payload: " + e.getMessage());
            return "{}";
        }
    }
}
