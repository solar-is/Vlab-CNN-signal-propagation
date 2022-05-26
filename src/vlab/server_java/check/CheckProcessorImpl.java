package vlab.server_java.check;

import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.PreCheckProcessor.PreCheckResult;
import rlcp.server.processor.check.PreCheckResultAwareCheckProcessor;
import vlab.server_java.JacksonHelper;
import vlab.server_java.model.solutionchecking.MatrixAnswer;
import vlab.server_java.model.solutionchecking.Solution;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Simple CheckProcessor implementation. Supposed to be changed as needed to provide
 * necessary Check method support.
 */
public class CheckProcessorImpl implements PreCheckResultAwareCheckProcessor<String> {
    private static final double MAX_POINTS = 100.0;
    private static final double[] VALID_MATRIX_POINTS = {
            20.0, 20.0,
            10.0, 10.0,
            2.5, 2.5, 2.5, 2.5,
            2.5, 2.5, 2.5, 2.5
    };
    private static final double COMPARISON_EPS = 0.01;

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

            //compare ourSolution and studentSolution
            int ourSolutionMatricesSize = ourSolution.matrices.size();
            int studentSolutionMatricesSize = studentSolution.matrices.size();

            boolean shouldContinueComparison = true;
            for (int k = 1; k < ourSolutionMatricesSize && shouldContinueComparison; k++) { //skip first matrix
                MatrixAnswer ourMatrix = ourSolution.matrices.get(k);

                if (studentSolutionMatricesSize > k) {
                    MatrixAnswer studentMatrix = studentSolution.matrices.get(k);
                    double[][] studentMatrixValue = studentMatrix.matrixValue;
                    double[][] ourMatrixValue = ourMatrix.matrixValue;

                    int ourMatrixDimension = ourMatrixValue.length;
                    int studentMatrixHeight = studentMatrixValue.length;
                    int studentMatrixWidth = studentMatrixValue[0].length;

                    if (studentMatrixHeight != ourMatrixDimension || studentMatrixWidth != ourMatrixDimension) {
                        commentBuilder.append("Ошибка в размере матрицы ").append(getNumericMatrixId(studentMatrix))
                                .append(": sys=").append(ourMatrixDimension).append("x").append(ourMatrixDimension)
                                .append(" user=").append(studentMatrixHeight).append("x").append(studentMatrixWidth);
                        break;
                    } else {
                        double oldPoints = points;
                        double validMatrixPointsAmount = VALID_MATRIX_POINTS[k - 1];
                        int matrixCellsCount = ourMatrixDimension * ourMatrixDimension;

                        for (int i = 0; i < studentMatrixHeight && shouldContinueComparison; i++) {
                            for (int j = 0; j < studentMatrixWidth; j++) {
                                double diff = Math.abs(studentMatrixValue[i][j] - ourMatrixValue[i][j]);
                                if (Double.compare(diff, COMPARISON_EPS) > 0) {
                                    if (studentMatrix.slideNumber % 2 == 0) {
                                        commentBuilder.append("Выборка");
                                    } else {
                                        commentBuilder.append("Свертка");

                                    }
                                    commentBuilder.append(" в матрице ").append(getNumericMatrixId(studentMatrix))
                                            .append(", элемент (").append(i + 1).append(",")
                                            .append(j + 1).append("): sys=").append(ourMatrixValue[i][j]).append(" user=").append(studentMatrixValue[i][j]);
                                    shouldContinueComparison = false;
                                    break;
                                } else {
                                    points += (validMatrixPointsAmount / matrixCellsCount);
                                }
                            }
                        }

                        if (shouldContinueComparison) {
                            //to avoid problems with doubles precision, update points with only one addition
                            points = oldPoints + validMatrixPointsAmount;
                        }
                    }
                } else {
                    //different amount of matrices
                    commentBuilder.append("Ошибка в количестве матриц:").append(" sys=").append(ourSolutionMatricesSize)
                            .append(" user=").append(studentSolutionMatricesSize);
                    shouldContinueComparison = false;
                }
            }

            //check mse
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

    private String getNumericMatrixId(MatrixAnswer studentMatrix) {
        //remove garbage from id
        return studentMatrix.matrixId.replaceAll("id", "").replaceAll("-", "").replaceAll("&#0045;", "");
    }

    @Override
    public void setPreCheckResult(PreCheckResult<String> preCheckResult) {
    }
}
