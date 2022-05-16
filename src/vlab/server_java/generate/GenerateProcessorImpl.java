package vlab.server_java.generate;

import rlcp.generate.GeneratingResult;
import rlcp.server.processor.generate.GenerateProcessor;
import vlab.server_java.JacksonHelper;
import vlab.server_java.model.CNNGenerator;
import vlab.server_java.model.Matrix;
import vlab.server_java.model.MatrixNetNode;

import java.util.Arrays;

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

            /*variant = new Variant(
                    new MatrixNetNode(0, null, new Matrix(new double[][]{
                            {0.8, 0.5, 0.41, 0.99, 0.39, 0.48, 0.5},
                            {0.06, 0.58, 0.47, 0.55, 0.46, 0.74, 0.01},
                            {0.04, 0.52, 0.94, 0.65, 0.03, 0.52, 0.87},
                            {0.67, 0.28, 0.15, 0.93, 0.62, 0.6, 0.17},
                            {0.33, 0.67, 0.8, 0.59, 0.49, 0.62, 0.49},
                            {0.02, 0.61, 0.29, 0.44, 0.41, 0.2, 0.39},
                            {0.16, 0.88, 1, 0.24, 0.74, 0.26, 0.51}
                    })),
                    Arrays.asList(new Matrix(new double[][]{
                            {0, 1}, {-1, 0}
                    }), new Matrix(new double[][]{
                            {1, 0}, {0, 1}
                    })),
                    "Avg",
                    "ReLU"
            );*/

            /*variant = new Variant(
                    new MatrixNetNode(0, null, new Matrix(new double[][]{
                            {0.15, 0.36, 0., 0.28, 0.57, 0.62, 0.87},
                            {0.49, 0.84, 0.51, 0.48, 0.64, 0.2, 0.43},
                            {0.74, 0., 0.75, 0.46, 0.23, 0.52, 0.64},
                            {0.37, 0.67, 0.62, 0.35, 0.92, 0.68, 0.34},
                            {0.13, 0.98, 0.26, 0.94, 0.08, 0.42, 0.34},
                            {0.38, 0.73, 0.17, 0.56, 0.64, 0.89, 0.06},
                            {0.95, 0.28, 0.39, 0.66, 0.51, 0.4, 0.01}
                    })),
                    Arrays.asList(new Matrix(new double[][]{
                            {1, 0}, {0, 1}
                    }), new Matrix(new double[][]{
                            {-1, 0}, {0, -1}
                    })),
                    "Max",
                    "Linear"
            );*/

            generatedVariantInJson = JacksonHelper.toJson(variant);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GeneratingResult("Найдите значение сигнала на каждом слое сети и посчитайте MSE на выходном слое",
                generatedVariantInJson, "instructions from generateProcessor");
    }
}
