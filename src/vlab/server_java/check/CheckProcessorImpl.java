package vlab.server_java.check;

import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.PreCheckProcessor.PreCheckResult;
import rlcp.server.processor.check.PreCheckResultAwareCheckProcessor;
import vlab.server_java.JacksonHelper;
import vlab.server_java.generate.Variant;
import vlab.server_java.model.solutionchecking.MatrixAnswer;
import vlab.server_java.model.solutionchecking.Solution;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.valueOf;

/**
 * Simple CheckProcessor implementation. Supposed to be changed as needed to provide
 * necessary Check method support.
 */

public class CheckProcessorImpl implements PreCheckResultAwareCheckProcessor<String> {
    private static final double MSE_EPS = 0.01;
    private static final double MATRIX_VALUE_EPS = 0.01;

    private static final double MSE_DIFF_PENALTY_COEFFICIENT = 10;
    private static final double MATRICES_COUNT_DIFF_PENALTY_COEFFICIENT = 20;
    private static final double MATRICES_VALUE_DIFF_PENALTY_COEFFICIENT = 1;

    @Override
    public CheckingSingleConditionResult checkSingleCondition(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) {
        System.out.println("instructions in checkProcessor" + instructions);

        BigDecimal points = BigDecimal.valueOf(1.0);
        StringBuilder commentBuilder = new StringBuilder();

        try {
            Variant generatedVariant = JacksonHelper.fromJson(generatingResult.getCode(), Variant.class);

            Solution studentSolution = JacksonHelper.fromJson(instructions, Solution.class);
            Solution ourSolution = new Solution(generatedVariant);

            //compare ourSolution and studentSolution
            points = compareMSE(studentSolution, ourSolution, commentBuilder, points);
            points = compareMatricesCount(studentSolution, ourSolution, commentBuilder, points);
            points = compareMatricesValues(studentSolution, ourSolution, commentBuilder, points);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CheckingSingleConditionResult(
                Double.compare(points.doubleValue(), 0.0) > 0
                        ? points : BigDecimal.valueOf(0.0),
                commentBuilder.toString()
        );
    }

    private BigDecimal compareMatricesValues(@Nonnull Solution studentSolution,
                                             @Nonnull Solution ourSolution,
                                             @Nonnull StringBuilder commentBuilder,
                                             @Nonnull BigDecimal points) {
        for (MatrixAnswer studentMatrix : studentSolution.matrices) {
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
                    points = points.subtract(valueOf(0.2));
                } else {
                    for (int i = 0; i < h; i++) {
                        for (int j = 0; j < w; j++) {
                            double diff = Math.abs(studentMatrixValue[i][j] - ourMatrixValue[i][j]);
                            if (Double.compare(diff, MATRIX_VALUE_EPS) > 0) {
                                commentBuilder.append("Матрица ").append(ourMatrix.matrixId).append(", ячейка (").append(i).append(",")
                                        .append(j).append("): ожидаемое значение - ").append(ourMatrixValue[i][j]).append(", актуальное значение - ").append(studentMatrixValue[i][j]);
                                points = points.subtract(valueOf(diff * MATRICES_VALUE_DIFF_PENALTY_COEFFICIENT));
                            }
                        }
                    }
                }
            }
        }
        return points;
    }

    private BigDecimal compareMatricesCount(@Nonnull Solution studentSolution,
                                            @Nonnull Solution ourSolution,
                                            @Nonnull StringBuilder commentBuilder,
                                            @Nonnull BigDecimal points) {
        int size = ourSolution.matrices.size();
        int matricesCntDiff = Math.abs(studentSolution.matrices.size() - size);
        if (matricesCntDiff != 0) {
            commentBuilder.append("Количество результирующих матриц отличается от правильного (").append(size).append(") на ").append(matricesCntDiff).append(";\n");
            return points.subtract(valueOf(matricesCntDiff * MATRICES_COUNT_DIFF_PENALTY_COEFFICIENT));
        }
        return points;
    }

    private BigDecimal compareMSE(@Nonnull Solution studentSolution,
                                  @Nonnull Solution ourSolution,
                                  @Nonnull StringBuilder commentBuilder,
                                  @Nonnull BigDecimal points) {
        double mseDiff = Math.abs(studentSolution.mse - ourSolution.mse);
        if (Double.compare(mseDiff, MSE_EPS) > 0) {
            commentBuilder.append("MSE отличается от правильного (").append(ourSolution.mse).append(") больше чем на ").append(MSE_EPS).append(";\n");
            return points.subtract(valueOf(mseDiff * MSE_DIFF_PENALTY_COEFFICIENT));
        } else {
            return points;
        }
    }

    @Override
    public void setPreCheckResult(PreCheckResult<String> preCheckResult) {
    }
}
