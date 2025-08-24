package com.jirahighlights.nlpservice.controller;
import com.jirahighlights.service.NLPService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/nlp")
public class NLPController {

    private NLPService nlpService;

    @Autowired
    public NLPController(NLPService nlpService){
        this.nlpService = nlpService;
    }

    /**
     * Processes the Jira data and returns a summary, as expected by the chatbot service.
     * This endpoint is designed to be called by the `ChatbotService` and
     * accepts the entire JIRA ticket data as a String.
     * @param jiraData text data from the JIRA ticket
     * @return a processed response of String type
     */
    @PostMapping("/sentences")
    public String processSentences(@RequestBody String jiraData) {
        return nlpService.processJiraData(jiraData);
    }
}