package com.jirahighlights.jiraintegratorservice.controller;

import com.jirahighlights.jiraintegratorservice.model.JiraTicket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/jira")
@Slf4j
public class JiraController {

    @Value("${jira.api.url}")
    private String jiraApiUrl;

    @Value("${jira.api.username}")
    private String username;
  
    @Value("${jira.api.token}")
    private String token;

    @GetMapping("/ticket/{ticketId}")
    public Mono<JiraTicket> getTicket(@PathVariable String ticketId) {
        String encodedAuth = Base64.getEncoder().encodeToString((username + ":" + token).getBytes());
        WebClient webClient = WebClient.builder()
            .baseUrl(jiraApiUrl)
            .defaultHeader("Authorization", "Basic " + encodedAuth)
            .build();

        log.debug("FETCHING Jira ticket with ID: {}", jiraApiUrl + "/" +ticketId);
        return webClient.get()
            .uri("/rest/api/2/issue/{id}", ticketId)
            .retrieve()
            .bodyToMono(JiraTicket.class);
    }
}