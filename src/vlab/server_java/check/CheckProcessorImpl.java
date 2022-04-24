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
import java.util.List;
import java.util.Optional;

/**
 * Simple CheckProcessor implementation. Supposed to be changed as needed to provide
 * necessary Check method support.
 */
public class CheckProcessorImpl implements PreCheckResultAwareCheckProcessor<String> {
    private static final double[] VALID_MATRIX_POINTS = {15.0, 15.0, 9.0, 9.0, 6.0, 6.0, 6.0, 6.0, 3.0, 3.0, 3.0, 3.0};
    private static final double MSE_VALID_POINTS = 16.0;
    private static final double COMPARISON_EPS = 0.01;

    @Override
    public CheckingSingleConditionResult checkSingleCondition(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) {
        System.out.println("instructions in checkProcessor" + instructions);

        double points = 0.0;
        StringBuilder commentBuilder = new StringBuilder();

        try {
            Variant generatedVariant = JacksonHelper.fromJson(generatingResult.getCode(), Variant.class);

            Solution studentSolution = JacksonHelper.fromJson(instructions, Solution.class);
            Solution ourSolution = new Solution(generatedVariant);

            //compare ourSolution and studentSolution from the very beginning to the end
            List<MatrixAnswer> matrices = studentSolution.matrices;
            for (int k = 0; k < matrices.size(); k++) {
                MatrixAnswer studentMatrix = matrices.get(k);
                Optional<MatrixAnswer> ourMatrixOptional = ourSolution.matrices
                        .stream()
                        .filter(ourMatrix -> ourMatrix.matrixId.equals(studentMatrix.matrixId))
                        .findFirst();

                if (ourMatrixOptional.isPresent()) {
                    MatrixAnswer ourMatrix = ourMatrixOptional.get();
                    double[][] studentMatrixValue = studentMatrix.matrixValue;
                    double[][] ourMatrixValue = ourMatrix.matrixValue;

                    int h = ourMatrixValue.length;
                    int w = ourMatrixValue[0].length;
                    if (studentMatrixValue.length != h || studentMatrixValue[0].length != w) {
                        commentBuilder.append("Матрица ").append(ourMatrix.matrixId).append(" должна иметь размер ")
                                .append(h).append("x").append(w).append(", но имеет размер ").append(studentMatrixValue.length).append("x").append(studentMatrixValue[0].length).append(";\n");
                        break;
                    } else {
                        for (int i = 0; i < h; i++) {
                            for (int j = 0; j < w; j++) {
                                double diff = Math.abs(studentMatrixValue[i][j] - ourMatrixValue[i][j]);
                                if (Double.compare(diff, COMPARISON_EPS) > 0) {
                                    commentBuilder.append("Матрица ").append(ourMatrix.matrixId).append(", ячейка (").append(i).append(",")
                                            .append(j).append("): ожидаемое значение - ").append(ourMatrixValue[i][j]).append(", актуальное значение - ").append(studentMatrixValue[i][j]);
                                    break;
                                }
                            }
                        }
                        points += VALID_MATRIX_POINTS[k];
                    }
                } else {
                    //should not happen
                    throw new IllegalStateException("Can't find matching matrix for matrix with id=" + studentMatrix.matrixId);
                }
            }

            double mseDiff = Math.abs(studentSolution.mse - ourSolution.mse);
            if (Double.compare(mseDiff, COMPARISON_EPS) > 0) {
                commentBuilder.append("MSE отличается от правильного (").append(ourSolution.mse).append(") больше чем на ").append(COMPARISON_EPS).append(";\n");
            } else {
                points += MSE_VALID_POINTS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CheckingSingleConditionResult(
                Double.compare(points, 1.0) > 0
                        ? BigDecimal.valueOf(1.0) : BigDecimal.valueOf(points),
                commentBuilder.toString()
        );
    }

    @Override
    public void setPreCheckResult(PreCheckResult<String> preCheckResult) {
    }
}
