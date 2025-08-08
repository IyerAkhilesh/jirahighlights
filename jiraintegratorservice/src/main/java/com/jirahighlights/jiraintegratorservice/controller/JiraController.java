package com.jirahighlights.jiraintegratorservice.controller;

import com.jirahighlights.jiraintegratorservice.model.JiraTicket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import com.jirahighlights.jiraintegratorservice.JiraApiProperties;

import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/jira")
@Slf4j
public class JiraController {

    @Autowired
    private JiraApiProperties jiraApiProperties;

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
}