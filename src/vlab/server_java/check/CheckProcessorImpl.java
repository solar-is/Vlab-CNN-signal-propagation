package vlab.server_java.check;

import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.PreCheckProcessor.PreCheckResult;
import rlcp.server.processor.check.PreCheckResultAwareCheckProcessor;
import vlab.server_java.JacksonHelper;
import vlab.server_java.generate.Variant;
import vlab.server_java.model.solutionchecking.MatrixAnswer;
import vlab.server_java.model.solutionchecking.Solution;

import java.math.BigDecimal;

/**
 * Simple CheckProcessor implementation. Supposed to be changed as needed to provide
 * necessary Check method support.
 */
public class CheckProcessorImpl implements PreCheckResultAwareCheckProcessor<String> {
    private static final double MAX_POINTS = 100.0;
    private static final double[] VALID_MATRIX_POINTS = {15.0, 15.0, 9.0, 9.0, 6.0, 6.0, 6.0, 6.0, 3.0, 3.0, 3.0, 3.0};
    private static final double MSE_VALID_POINTS = 16.0;
    private static final double COMPARISON_EPS = 0.01;

    @Override
    public CheckingSingleConditionResult checkSingleCondition(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) {
        System.out.println("instructions in checkProcessor: " + instructions);
        instructions = instructions.replaceAll("&#0045;", "-");
        System.out.println("instructions in checkProcessor after dash code replacement: " + instructions);

        double points = 0.0;
        StringBuilder commentBuilder = new StringBuilder();

        try {
            Variant generatedVariant = JacksonHelper.fromJson(generatingResult.getCode(), Variant.class);
            Solution studentSolution = JacksonHelper.fromJson(instructions, Solution.class);
            Solution ourSolution = new Solution(generatedVariant);
            System.out.println("our solution: " + ourSolution);

            //compare ourSolution and studentSolution
            int ourSolutionMatricesSize = ourSolution.matrices.size();
            int studentSolutionMatricesSize = studentSolution.matrices.size();

            boolean shouldContinue = true;
            for (int k = 1; k < ourSolutionMatricesSize; k++) { //skip first matrix
                MatrixAnswer ourMatrix = ourSolution.matrices.get(k);

                if (studentSolutionMatricesSize > k) {
                    MatrixAnswer studentMatrix = studentSolution.matrices.get(k);
                    double[][] studentMatrixValue = studentMatrix.matrixValue;
                    double[][] ourMatrixValue = ourMatrix.matrixValue;

                    int h = ourMatrixValue.length;
                    int w = ourMatrixValue[0].length;
                    if (studentMatrixValue.length != h || studentMatrixValue[0].length != w) {
                        commentBuilder.append("Матрица ").append(getNumericMatrixId(studentMatrix))
                                .append(" должна иметь размер ")
                                .append(h).append("x").append(w).append(", но имеет размер ").append(studentMatrixValue.length).append("x").append(studentMatrixValue[0].length);
                        break;
                    } else {
                        for (int i = 0; i < h && shouldContinue; i++) {
                            for (int j = 0; j < w; j++) {
                                double diff = Math.abs(studentMatrixValue[i][j] - ourMatrixValue[i][j]);
                                if (Double.compare(diff, COMPARISON_EPS) > 0) {
                                    commentBuilder.append("Матрица ").append(getNumericMatrixId(studentMatrix))
                                            .append(", ячейка (").append(i + 1).append(",")
                                            .append(j + 1).append("): ожидаемое значение - ").append(ourMatrixValue[i][j]).append(", актуальное значение - ").append(studentMatrixValue[i][j]);
                                    shouldContinue = false;
                                    break;
                                }
                            }
                        }
                        if (!shouldContinue) {
                            break;
                        }
                        points += VALID_MATRIX_POINTS[k - 1];
                    }
                } else {
                    //different amount of matrices
                    commentBuilder.append("Ожидаемое количество матриц в ответе - ").append(ourSolutionMatricesSize)
                            .append(", актульное количество - ").append(studentSolutionMatricesSize);
                    break;
                }
            }

            //check mse
            if (commentBuilder.length() == 0 &&
                    Double.compare(points, MAX_POINTS - MSE_VALID_POINTS) == 0) {
                double mseDiff = Math.abs(studentSolution.mse - ourSolution.mse);
                if (Double.compare(mseDiff, COMPARISON_EPS) > 0) {
                    commentBuilder.append("MSE=").append(studentSolution.mse).append(" отличается от правильного (").append(ourSolution.mse).append(") больше чем на ").append(COMPARISON_EPS);
                } else {
                    points = MAX_POINTS; //points += MSE_VALID_POINTS
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CheckingSingleConditionResult(
                BigDecimal.valueOf(points / 100),
                commentBuilder.toString()
        );
    }

    private String getNumericMatrixId(MatrixAnswer studentMatrix) {
        //remove garbage from id
        return studentMatrix.matrixId.replaceAll("id", "").replaceAll("-", "").replaceAll("&#0045;", "");
    }

    @Override
    public void setPreCheckResult(PreCheckResult<String> preCheckResult) {
    }
}
