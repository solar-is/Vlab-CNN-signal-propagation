package vlab.server_java.generate;

import rlcp.generate.GeneratingResult;
import rlcp.server.processor.generate.GenerateProcessor;
import vlab.server_java.JacksonHelper;
import vlab.server_java.model.CNNGenerator;

/**
 * Simple GenerateProcessor implementation. Supposed to be changed as needed to
 * provide necessary Generate method support.
 */
public class GenerateProcessorImpl implements GenerateProcessor {
    private static final CNNGenerator generator = CNNGenerator.DEFAULT;

    @Override
    public GeneratingResult generate(String condition) {
        String generatedVariantInJson = "";
        try {
            Variant variant = new Variant(
                    generator.generateInputNode(),
                    generator.generateConvolutionKernels(),
                    generator.generateSubSamplingFunction(),
                    generator.generateActivationFunction()
            );
            generatedVariantInJson = JacksonHelper.toJson(variant);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GeneratingResult("text from generateProcessor", generatedVariantInJson, "instructions from generateProcessor");
    }
}
