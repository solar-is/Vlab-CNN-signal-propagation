package vlab.server_java.check;

import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.PreCheckProcessor.PreCheckResult;
import rlcp.server.processor.check.PreCheckResultAwareCheckProcessor;
import vlab.server_java.JacksonHelper;
import vlab.server_java.model.solutionchecking.MatrixAnswer;
import vlab.server_java.model.solutionchecking.Solution;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple CheckProcessor implementation. Supposed to be changed as needed to provide
 * necessary Check method support.
 */
public class CheckProcessorImpl implements PreCheckResultAwareCheckProcessor<String> {
    private static final double MAX_POINTS = 100.0;
    private static final double[] VALID_MATRIX_POINTS = {40.0, 20.0, 10.0, 10.0};
    private static final int[] VALID_MATRIX_CELLS_COUNTS = {72, 18, 16, 4};

    private static final double COMPARISON_EPS = 0.009;

    @Override
    public CheckingSingleConditionResult checkSingleCondition(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) {
        System.out.println("instructions in checkProcessor: " + instructions);
        instructions = instructions.replaceAll("&#0045;", "-");
        System.out.println("instructions in checkProcessor after dash code replacement: " + instructions);

        double points = 0.0;
        StringBuilder commentBuilder = new StringBuilder();

        try {
            Solution studentSolution = JacksonHelper.fromJson(instructions, Solution.class);
            Solution ourSolution = JacksonHelper.fromJson(generatingResult.getInstructions(), Solution.class);
            System.out.println("student solution: " + studentSolution);
            System.out.println("our solution: " + ourSolution);

            int idx = 0;
            for (MatrixAnswer matrix : studentSolution.matrices) {
                matrix.matrixId = String.valueOf(idx++);
            }

            int currentLayerNumber = 1;
            boolean shouldContinueComparison = true;
            while (currentLayerNumber <= 4 && shouldContinueComparison) {
                List<MatrixAnswer> studentMatrices = getMatricesFromLayer(studentSolution, currentLayerNumber);
                List<MatrixAnswer> ourMatrices = getMatricesFromLayer(ourSolution, currentLayerNumber);

                if (studentMatrices.size() != ourMatrices.size()) {
                    commentBuilder.append("Ошибка в количестве матриц на слое ").append(currentLayerNumber).append(":").append(" sys=").append(ourMatrices.size())
                            .append(" user=").append(studentMatrices.size());
                    break;
                }
                if (!validDimensions(studentMatrices, ourMatrices, commentBuilder)) {
                    break;
                }

                int validCellsCnt = 0;
                for (int k = 0; k < ourMatrices.size(); k++) {
                    MatrixAnswer studentMatrixAnswer = studentMatrices.get(k);
                    double[][] studentMatrix = studentMatrixAnswer.matrixValue;
                    double[][] ourMatrix = ourMatrices.get(k).matrixValue;

                    for (int i = 0; i < studentMatrix.length; i++) {
                        for (int j = 0; j < studentMatrix[0].length; j++) {
                            double ourMatrixCellValue = ourMatrix[i][j];
                            double studentMatrixCellValue = studentMatrix[i][j];

                            double diff = Math.abs(studentMatrixCellValue - ourMatrixCellValue);
                            if (Double.compare(diff, COMPARISON_EPS) > 0) {
                                if (studentMatrixAnswer.slideNumber % 2 == 0) {
                                    commentBuilder.append("Выборка");
                                } else {
                                    commentBuilder.append("Свертка");
                                }
                                commentBuilder.append(" в матрице ").append(getNumericMatrixId(studentMatrixAnswer))
                                        .append(", элемент (").append(i + 1).append(",")
                                        .append(j + 1).append("): sys=").append(ourMatrixCellValue).append(" user=").append(studentMatrixCellValue)
                                        .append("; ");
                                shouldContinueComparison = false;
                            } else {
                                validCellsCnt++;
                            }
                        }
                    }
                }

                int totalCellsCnt = VALID_MATRIX_CELLS_COUNTS[currentLayerNumber - 1];
                if (validCellsCnt == totalCellsCnt) {
                    points += VALID_MATRIX_POINTS[currentLayerNumber - 1];
                } else {
                    points += VALID_MATRIX_POINTS[currentLayerNumber - 1] * (validCellsCnt * 1.0 / totalCellsCnt);
                }
                currentLayerNumber++;
            }

            //check mse if there were no errors found earlier
            if (commentBuilder.length() == 0) {
                double mseDiff = Math.abs(studentSolution.mse - ourSolution.mse);
                if (Double.compare(mseDiff, COMPARISON_EPS) > 0) {
                    commentBuilder.append("Ошибка в MSE: ").append("sys=").append(ourSolution.mse)
                            .append(" user=").append(studentSolution.mse);
                } else {
                    points = MAX_POINTS;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CheckingSingleConditionResult(
                BigDecimal.valueOf(points / 100)
                        .setScale(2, RoundingMode.HALF_DOWN),
                commentBuilder.toString()
        );
    }

    private boolean validDimensions(@Nonnull List<MatrixAnswer> studentMatrices,
                                    @Nonnull List<MatrixAnswer> ourMatrices,
                                    @Nonnull StringBuilder commentBuilder) {
        for (int i = 0; i < ourMatrices.size(); i++) {
            MatrixAnswer studentMatrixAnswer = studentMatrices.get(i);
            double[][] studentMatrix = studentMatrixAnswer.matrixValue;
            double[][] ourMatrix = ourMatrices.get(i).matrixValue;

            if (studentMatrix.length != ourMatrix.length ||
                    studentMatrix[0].length != ourMatrix[0].length) {
                commentBuilder.append("Ошибка в размере матрицы ").append(getNumericMatrixId(studentMatrixAnswer))
                        .append(": sys=").append(ourMatrix.length).append("x").append(ourMatrix[0].length)
                        .append(" user=").append(studentMatrix.length).append("x").append(studentMatrix[0].length);
                return false;
            }
        }
        return true;
    }

    @Nonnull
    private List<MatrixAnswer> getMatricesFromLayer(@Nonnull Solution solution,
                                                    int currentLayerNumber) {
        return solution.matrices
                .stream()
                .filter(m -> m.slideNumber == currentLayerNumber)
                .collect(Collectors.toList());
    }

    @Nonnull
    private String getNumericMatrixId(@Nonnull MatrixAnswer studentMatrix) {
        //remove garbage from id
        return studentMatrix.matrixId.replaceAll("id", "").replaceAll("-", "").replaceAll("&#0045;", "");
    }

    @Override
    public void setPreCheckResult(PreCheckResult<String> preCheckResult) {
    }
}
