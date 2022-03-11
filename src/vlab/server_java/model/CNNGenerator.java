package vlab.server_java.model;

import vlab.server_java.model.nodes.MatrixNetNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Assuming valid input: alternating of SS and CONV layers(CONV layer is first one), symmetric between each layer, proper matrix sizes.
 * Hardcoded params: dimensionality reduction during SS is 1/2.
 */
public class CNNGenerator {
    public static final CNNGenerator DEFAULT = new CNNGenerator();

    private static final List<String> POSSIBLE_ACTIVATION_FUNCTIONS = Arrays.asList("ReLU", "Linear");
    private static final List<String> POSSIBLE_SUBSAMPLING_FUNCTIONS = Arrays.asList("Max", "Avg");

    private static final int DEFAULT_INPUT_MATRIX_SIZE = 7;
    private static final int DEFAULT_CONVOLUTION_KERNELS_SIZES = 2;
    private final int inputMatrixSize;
    private final int convKernelsSizes;

    private static final Random RANDOM = new Random();

    public CNNGenerator() {
        this.inputMatrixSize = DEFAULT_INPUT_MATRIX_SIZE;
        this.convKernelsSizes = DEFAULT_CONVOLUTION_KERNELS_SIZES;
    }

    public CNNGenerator(int inputMatrixSize, int convKernelsSizes) {
        this.inputMatrixSize = inputMatrixSize;
        this.convKernelsSizes = convKernelsSizes;
    }

    public String generateActivationFunction() {
        return POSSIBLE_ACTIVATION_FUNCTIONS.get(RANDOM.nextInt(POSSIBLE_ACTIVATION_FUNCTIONS.size()));
    }

    public String generateSubSamplingFunction() {
        return POSSIBLE_SUBSAMPLING_FUNCTIONS.get(RANDOM.nextInt(POSSIBLE_SUBSAMPLING_FUNCTIONS.size()));
    }

    //todo complete real generating logic
    public MatrixNetNode generateInputNode() {
        return new MatrixNetNode(0, null, null, Matrix.randomWithSize(inputMatrixSize));
    }

    //todo complete real generating logic
    public List<Matrix> generateConvolutionKernels() {
        List<Matrix> kernels = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            kernels.add(Matrix.randomWithSize(convKernelsSizes));
        }
        return kernels;
    }

}
