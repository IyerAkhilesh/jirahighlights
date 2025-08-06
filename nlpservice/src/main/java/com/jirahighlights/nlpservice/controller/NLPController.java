package com.jirahighlights.nlpservice.controller;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/nlp")
public class NlpController {

    private SentenceDetectorME sentenceDetector;

    public NlpController() throws IOException {
        InputStream modelIn = new ClassPathResource("models/en-sent.bin").getInputStream();
        SentenceModel model = new SentenceModel(modelIn);
        sentenceDetector = new SentenceDetectorME(model);
    }

    @PostMapping("/sentences")
    public List<String> getSentences(@RequestBody String text) {
        String[] sentences = sentenceDetector.sentDetect(text);
        return Arrays.asList(sentences);
    }
}