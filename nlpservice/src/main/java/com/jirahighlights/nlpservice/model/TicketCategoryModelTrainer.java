package com.jirahighlights.nlpservice.model;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.InputStreamFactory;

import java.io.*;

public class TicketCategoryModelTrainer {
    public static void main(String[] args) throws IOException {
        String trainingDataPath = "nlpservice\\src\\main\\resources\\ticket-category.train";
        String modelOutputPath = "nlpservice\\src\\main\\resources\\ticket-category-trained.bin";


        InputStreamFactory dataIn = new InputStreamFactory() {
            public InputStream createInputStream() throws IOException {
                return new FileInputStream(trainingDataPath);
            }
        };
        try (ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
             ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream)) {

            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 100);
            params.put(TrainingParameters.CUTOFF_PARAM, 1);

            DoccatModel model = opennlp.tools.doccat.DocumentCategorizerME.train(
                    "en", sampleStream, params, new DoccatFactory());

            try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelOutputPath))) {
                model.serialize(modelOut);
                System.out.println("Model trained and saved to " + modelOutputPath);
            }
        }
    }
}
