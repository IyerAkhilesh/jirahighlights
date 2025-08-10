package com.jirahighlights.jiraintegratorservice.controller;

import com.jirahighlights.jiraintegratorservice.model.JiraTicket;
import com.jirahighlights.jiraintegratorservice.JiraApiProperties;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

// import com.jirahighlights.jiraintegratorservice.service.JiraService;
// import com.jirahighlights.jiraintegratorservice.model.TicketRequest;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import jakarta.validation.Valid;
// import java.util.Map;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/jira")
@Slf4j
public class JiraController {

   

    @Autowired
    private JiraApiProperties jiraApiProperties;

    // private final JiraService jiraService;
    // public JiraController(JiraService jiraService) {
    //     this.jiraService = jiraService;
    // }

    @GetMapping("/ticket/{ticketId}")
    public Mono<JiraTicket> getTicket(@PathVariable String ticketId) {
        String encodedAuth = Base64.getEncoder().encodeToString((jiraApiProperties.getUsername() + ":" + jiraApiProperties.getToken()).getBytes());
        WebClient webClient = WebClient.builder()
            .baseUrl(jiraApiProperties.getUrl())
            .defaultHeader("Authorization", "Basic " + encodedAuth)
            .build();

        return webClient.get()
            .uri("/rest/api/2/issue/{id}", ticketId)
            .retrieve()
            .bodyToMono(JiraTicket.class);
    }

    /*
     * Endpoint to generate random Jira tickets.
     */

    // @PostMapping("/generate-tickets")
    // public Mono<Map<String,String>> generateTickets(@Valid @RequestBody TicketRequest request) {
    //     // Logic to generate tickets
    //     if (request.getCount() <= 0 || request.getCount() > 1000) {
    //         return Mono.just(Map.of("error", "Count must be between 1 and 1000"));
    //     }
        
    //     return jiraService.createRandomTickets(request.getCount(), request.getProjectKey(), request.getIssueType(), request.getComment())
    //             .thenReturn(Map.of(
    //                     "message", "Successfully started creating " + request.getCount() + " random Jira tickets.",
    //                     "jira_project_key", request.getProjectKey()
    //             ));
    // }
}