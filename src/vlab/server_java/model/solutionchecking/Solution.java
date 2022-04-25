package vlab.server_java.model.solutionchecking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.util.Pair;
import vlab.server_java.generate.Variant;
import vlab.server_java.model.Matrix;
import vlab.server_java.model.MatrixNetNode;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {
    public double mse;
    public List<MatrixAnswer> matrices;

    @JsonCreator
    public Solution(@JsonProperty("mse") double mse,
                    @Nonnull @JsonProperty("matrices") List<MatrixAnswer> matrices) {
        this.mse = mse;
        this.matrices = matrices;
    }

    public Solution(@Nonnull Variant generatedVariant) {
        List<MatrixNetNode> calculatedNodes = new ArrayList<>();
        calculateNodes(generatedVariant, calculatedNodes);

        //set fields
        calculateMseValue(calculatedNodes);
        calculateMatrices(calculatedNodes);
    }

    private void calculateMatrices(@Nonnull List<MatrixNetNode> calculatedNodes) {
        //converting to MatrixAnswer's
        Map<MatrixNetNode, String> idByMatrices = new HashMap<>();
        Map<String, MatrixAnswer> matricesById = new HashMap<>();
        assignIdsAndFillMapsWithMatrices(calculatedNodes, idByMatrices, matricesById);

        idByMatrices.forEach((node, id) -> {
            MatrixAnswer matrixAnswer = matricesById.get(id);
            List<MatrixNetNode> nextNodes = node.getNextNodes();
            if (nextNodes != null) {
                //fill linkedMatricesIds with all needed nodes
                matrixAnswer.linkedMatricesIds.addAll(
                        nextNodes
                                .stream()
                                .map(idByMatrices::get)
                                .collect(Collectors.toList())
                );
            }
        });

        this.matrices = matricesById.values().stream().sorted((o1, o2) -> {
            //compare by id, ascending
            String matrixId1 = o1.matrixId;
            String matrixId2 = o2.matrixId;
            Integer integer1 = Integer.valueOf(matrixId1.substring(matrixId1.indexOf('-') + 1));
            Integer integer2 = Integer.valueOf(matrixId2.substring(matrixId2.indexOf('-') + 1));
            return integer1.compareTo(integer2);
        }).collect(Collectors.toList());
    }

    private void assignIdsAndFillMapsWithMatrices(@Nonnull List<MatrixNetNode> calculatedNodes,
                                                  @Nonnull Map<MatrixNetNode, String> idByMatrices,
                                                  @Nonnull Map<String, MatrixAnswer> matricesById) {
        int matrixIdCounter = 0;
        for (MatrixNetNode nodeToConvert : calculatedNodes) {
            //generate id
            String id = "id-" + matrixIdCounter++;
            MatrixAnswer matrixAnswer = new MatrixAnswer(
                    id,
                    nodeToConvert.getLayerNumber(),
                    nodeToConvert.getPayload().getMatrix(),
                    new ArrayList<>()
            );

            //fill map
            idByMatrices.put(nodeToConvert, id);
            matricesById.put(id, matrixAnswer);
        }
    }

    private void calculateMseValue(@Nonnull List<MatrixNetNode> calculatedNodes) {
        double mseSum = calculatedNodes
                .stream()
                .filter(node -> node.getNextNodes() == null) //output neurons
                .mapToDouble(node -> node.getPayload().getMatrix()[0][0])
                .map(operand -> {
                    //calculating MSE as sum of ('output' - 'output*')^2, where 'output*'=1 for 'output'>0.5 and 'output*'=0 otherwise
                    if (Double.compare(operand, 0.5) > 0) {
                        return Math.pow(operand - 1, 2);
                    } else {
                        return Math.pow(operand, 2);
                    }
                })
                .sum();
        long outputNeuronsCount = calculatedNodes.stream().filter(node -> node.getNextNodes() == null).count();

        //rounded to 2 decimals
        this.mse = BigDecimal.valueOf(mseSum / outputNeuronsCount)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private void calculateNodes(@Nonnull Variant generatedVariant, @Nonnull List<MatrixNetNode> calculatedNodes) {
        //queue is using to process nodes in ascending order
        Queue<Pair<MatrixNetNode, LayerType>> queue = new ArrayDeque<>();
        //first layer is always convolution one
        queue.offer(new Pair<>(generatedVariant.inputNode, LayerType.CONVOLUTION));
        fillResultWithNodes(generatedVariant.activationFunction, generatedVariant.subSamplingFunction, generatedVariant.kernels, queue, calculatedNodes);
    }


    private void fillResultWithNodes(@Nonnull String activationFunction,
                                     @Nonnull String subSamplingFunction,
                                     @Nonnull List<Matrix> kernels,
                                     @Nonnull Queue<Pair<MatrixNetNode, LayerType>> queue,
                                     @Nonnull List<MatrixNetNode> result) {
        while (!queue.isEmpty()) {
            Pair<MatrixNetNode, LayerType> nodeToProcess = queue.poll();
            MatrixNetNode currentNode = nodeToProcess.getKey();

            double[][] matrix = currentNode.getPayload().getMatrix();
            if (matrix.length == 1 && matrix[0].length == 1) {
                //output neuron, nothing to evaluate, just add to result
                result.add(currentNode);
                continue;
            }

            LayerType layerType = nodeToProcess.getValue();
            if (layerType == LayerType.CONVOLUTION) {
                List<Matrix> nextNodesMatrices = doConvolutionFor(matrix, kernels, activationFunction);
                List<MatrixNetNode> newNodes = nextNodesMatrices
                        .stream()
                        .map(nextNodeMatrix -> new MatrixNetNode(currentNode.layerNumber + 1, null, nextNodeMatrix))
                        .collect(Collectors.toList());
                //create linked nodes and add them to queue
                newNodes.forEach(node -> queue.offer(new Pair<>(node, LayerType.SUBSAMPLING)));
                //add finalized node to 'result'
                currentNode.setNextNodes(newNodes);
                result.add(currentNode);
            } else {
                //subsampling layer
                Matrix nextNodeMatrix = doSubSamplingFor(matrix, subSamplingFunction);
                //create linked node and add it to queue
                MatrixNetNode matrixNetNode = new MatrixNetNode(currentNode.layerNumber + 1, null, nextNodeMatrix);
                queue.offer(new Pair<>(matrixNetNode, LayerType.CONVOLUTION));
                //add finalized node to 'result'
                currentNode.setNextNodes(Collections.singletonList(matrixNetNode));
                result.add(currentNode);
            }
        }
    }

    private Matrix doSubSamplingFor(double[][] matrix, @Nonnull String subSamplingFunction) {
        if (matrix.length % 2 != 0 ||
                matrix[0].length % 2 != 0) {
            throw new IllegalArgumentException("Trying to perform subsampling for matrix with non divisible sizes (not even)");
        }

        double[][] result = new double[matrix.length / 2][matrix[0].length / 2];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = BigDecimal.valueOf(calculateSubSamplingFor(matrix, i, j, subSamplingFunction))
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
            }
        }

        return new Matrix(result);
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    private double calculateSubSamplingFor(double[][] matrix, int i, int j, @Nonnull String subSamplingFunction) {
        List<Double> interestedNumbers = new ArrayList<>();

        for (int k = i * 2; k < i * 2 + 2; k++) {
            for (int l = j * 2; l < j * 2 + 2; l++) {
                interestedNumbers.add(matrix[k][l]);
            }
        }

        if ("Avg".equals(subSamplingFunction)) {
            return interestedNumbers.stream().mapToDouble(d -> d).average().getAsDouble();
        } else if ("Max".equals(subSamplingFunction)) {
            return interestedNumbers.stream().mapToDouble(d -> d).max().getAsDouble();
        } else {
            throw new IllegalArgumentException("Unrecognized subSampling function: " + subSamplingFunction);
        }
    }

    private List<Matrix> doConvolutionFor(double[][] matrix, @Nonnull List<Matrix> kernels, @Nonnull String activationFunction) {
        List<Matrix> result = new ArrayList<>();
        for (Matrix kernel : kernels) {
            double[][] kernelMatrix = kernel.getMatrix();
            int height = matrix.length - kernelMatrix.length + 1;
            int width = matrix[0].length - kernelMatrix[0].length + 1;

            double[][] convolutedMatrix = new double[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    convolutedMatrix[i][j] = calculateConvolutionFor(matrix, kernelMatrix, i, j, activationFunction);
                }
            }

            result.add(new Matrix(convolutedMatrix));
        }
        return result;
    }

    private double calculateConvolutionFor(double[][] matrix, double[][] kernelMatrix, int i, int j, @Nonnull String activationFunction) {
        double res = 0.;
        for (int k = i; k < i + kernelMatrix.length; k++) {
            for (int l = j; l < j + kernelMatrix[0].length; l++) {
                res += (matrix[k][l] * kernelMatrix[k - i][l - j]);
            }
        }
        return roundAndApplyActivationFunctionFor(res, activationFunction);
    }

    private double roundAndApplyActivationFunctionFor(double convolutionResult, @Nonnull String activationFunction) {
        convolutionResult = BigDecimal.valueOf(convolutionResult)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        if ("Linear".equals(activationFunction)) {
            return convolutionResult;
        } else if ("ReLU".equals(activationFunction)) {
            return Math.max(0.0, convolutionResult);
        } else {
            throw new IllegalArgumentException("Unrecognized activation function: " + activationFunction);
        }
    }

    private enum LayerType {
        CONVOLUTION,
        SUBSAMPLING
    }
}
