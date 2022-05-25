package vlab.server_java.model;

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
    private static final List<Matrix> POSSIBLE_CONVOLUTION_KERNELS = Arrays.asList(
            new Matrix(new double[][]{{0, 1}, {1, 0}}),
            new Matrix(new double[][]{{0, 1}, {-1, 0}}),
            new Matrix(new double[][]{{0, -1}, {1, 0}}),
            new Matrix(new double[][]{{1, 0}, {0, 1}}),
            new Matrix(new double[][]{{1, 0}, {0, -1}})
    );

    private static final int DEFAULT_INPUT_MATRIX_SIZE = 7;
    private static final int DEFAULT_CONVOLUTION_KERNELS_CNT = 2;
    private final int inputMatrixSize;
    private final int convKernelsCnt;

    private static final Random RANDOM = new Random();

    public CNNGenerator() {
        this(DEFAULT_INPUT_MATRIX_SIZE, DEFAULT_CONVOLUTION_KERNELS_CNT);
    }

    public CNNGenerator(int inputMatrixSize, int convKernelsCnt) {
        if (inputMatrixSize <= 0 || convKernelsCnt <= 0) {
            throw new IllegalArgumentException("Matrices dimensions or kernels count can't be negative");
        }
        this.inputMatrixSize = inputMatrixSize;
        this.convKernelsCnt = convKernelsCnt;
    }

    public String generateActivationFunction() {
        int idx = RANDOM.nextInt(POSSIBLE_ACTIVATION_FUNCTIONS.size());
        return POSSIBLE_ACTIVATION_FUNCTIONS.get(idx);
    }

    public String generateSubSamplingFunction() {
        int idx = RANDOM.nextInt(POSSIBLE_SUBSAMPLING_FUNCTIONS.size());
        return POSSIBLE_SUBSAMPLING_FUNCTIONS.get(idx);
    }

    public MatrixNetNode generateInputNode() {
        return new MatrixNetNode(0, null, Matrix.randomWithSize(inputMatrixSize));
    }

    public List<Matrix> generateConvolutionKernels() {
        List<Matrix> kernels = new ArrayList<>();
        int kernelsCnt = POSSIBLE_CONVOLUTION_KERNELS.size();
        int idx = RANDOM.nextInt(kernelsCnt);
        for (int i = 0; i < convKernelsCnt; ++i) {
            Matrix kernel = POSSIBLE_CONVOLUTION_KERNELS.get(idx >= kernelsCnt ? idx % kernelsCnt : idx);
            kernels.add(kernel);
            idx++;
        }
        return kernels;
    }
}
