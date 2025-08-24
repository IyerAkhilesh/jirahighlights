package com.jirahighlights.service;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

@Service
public class NLPService {
    private DocumentCategorizerME documentCategorizerME;
    private TokenizerME tokenCategorizerME;

    /**
     * Initializes the NLP models for categorization and tokenization.
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception{
        // Load the document categorizer model
        try(InputStream categoryModelIn = getClass().getResourceAsStream("opennlp-mode/category-model.bin")){
            if (categoryModelIn == null){
                throw new IllegalStateException("Document categorization model file not found");
            }
            else{
                documentCategorizerME = new DocumentCategorizerME(new DoccatModel(categoryModelIn));
            }
        }

        // Load the tokenizer model
        try(InputStream tokenModelIn = getClass().getResourceAsStream("opennlp-mode/token-model.bin")){
            if (tokenModelIn == null){
                throw new IllegalStateException("Tokenization model file not found");
            }
            else{
                tokenCategorizerME = new TokenizerME(new TokenizerModel(tokenModelIn));
            }
        }
    }

    public String processJiraData(String jiraData){
        // Tokenize the input text
        String[] tokens = tokenCategorizerME.tokenize(jiraData);

        // Categorize the input text
        double[] outcomes = documentCategorizerME.categorize(tokens);
        String category = documentCategorizerME.getBestCategory(outcomes);

        //PLACEHOLDER FOR SUMMARIZATION AND ANALYSIS CODE

        // For simplicity, returning the category as the processed response
        return "The ticket appears to be about " + category + " and is highly relevant.";
    }

}
