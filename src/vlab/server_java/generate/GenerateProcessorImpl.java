package vlab.server_java.generate;

import rlcp.generate.GeneratingResult;
import rlcp.server.processor.generate.GenerateProcessor;
import vlab.server_java.JacksonHelper;
import vlab.server_java.model.CNNGenerator;
import vlab.server_java.model.Matrix;
import vlab.server_java.model.nodes.MatrixNetNode;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.Objects.requireNonNull;

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

        return new GeneratingResult("text from generateProcessor", generatedVariantInJson, "instructions");
    }

    private static final class Variant {
        //fields should be public for serialization to json without aux annotations
        public MatrixNetNode inputNode;
        public List<Matrix> kernels;
        public String subSamplingFunction;
        public String activationFunction;

        public Variant(@Nonnull MatrixNetNode inputNode,
                       @Nonnull List<Matrix> kernels,
                       @Nonnull String subSamplingFunction,
                       @Nonnull String activationFunction) {
            this.inputNode = requireNonNull(inputNode);
            this.kernels = requireNonNull(kernels);
            this.subSamplingFunction = requireNonNull(subSamplingFunction);
            this.activationFunction = requireNonNull(activationFunction);
        }
    }
}
