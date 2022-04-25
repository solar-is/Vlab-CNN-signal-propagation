import org.junit.Test;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.CheckProcessor;

import static org.junit.Assert.assertEquals;

public class CheckLogicTests extends ServerTestBase {
    private static final String variantJson = "{\"inputNode\":{\"layerNumber\":0,\"payload\":{\"matrix\":[[0.86,0.11,0.17,0.04,0.56,0.39,0.99],[0.66,0.97,0.74,0.36,0.19,0.8,0.71],[0.62,0.11,0.6,0.09,0.14,0.7,0.47],[0.71,0.29,0.58,0.9,0.68,0.23,0.96],[0.65,0.18,0.4,0.55,0.19,0.4,0.24],[0.24,0.58,0.34,0.25,0.92,0.57,0.63],[0.88,0.87,0.74,0.68,0.86,0.5,0.98]]}},\"kernels\":[{\"matrix\":[[1.0,0.0],[0.0,1.0]]},{\"matrix\":[[-1.0,0.0],[0.0,-1.0]]}],\"subSamplingFunction\":\"Avg\",\"activationFunction\":\"ReLU\"}";

    @Test
    public void testFullValidSolution() throws Exception {
        String studentSolution = "{\"mse\":0.25,\"matrices\":[{\"matrixId\":\"id-0\",\"slideNumber\":0,\"matrixValue\":[[0.86,0.11,0.17,0.04,0.56,0.39,0.99],[0.66,0.97,0.74,0.36,0.19,0.8,0.71],[0.62,0.11,0.6,0.09,0.14,0.7,0.47],[0.71,0.29,0.58,0.9,0.68,0.23,0.96],[0.65,0.18,0.4,0.55,0.19,0.4,0.24],[0.24,0.58,0.34,0.25,0.92,0.57,0.63],[0.88,0.87,0.74,0.68,0.86,0.5,0.98]],\"linkedMatricesIds\":[\"id-1\",\"id-2\"]},{\"matrixId\":\"id-1\",\"slideNumber\":1,\"matrixValue\":[[1.83,0.85,0.53,0.23,1.36,1.1],[0.77,1.57,0.83,0.5,0.89,1.27],[0.91,0.69,1.5,0.77,0.37,1.66],[0.89,0.69,1.13,1.09,1.08,0.47],[1.23,0.52,0.65,1.47,0.76,1.03],[1.11,1.32,1.02,1.11,1.42,1.55]],\"linkedMatricesIds\":[\"id-3\"]},{\"matrixId\":\"id-2\",\"slideNumber\":1,\"matrixValue\":[[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0]],\"linkedMatricesIds\":[\"id-4\"]},{\"matrixId\":\"id-3\",\"slideNumber\":2,\"matrixValue\":[[1.26,0.52,1.16],[0.79,1.12,0.9],[1.05,1.06,1.19]],\"linkedMatricesIds\":[\"id-5\",\"id-8\"]},{\"matrixId\":\"id-4\",\"slideNumber\":2,\"matrixValue\":[[0.0,0.0,0.0],[0.0,0.0,0.0],[0.0,0.0,0.0]],\"linkedMatricesIds\":[\"id-8\",\"id-8\"]},{\"matrixId\":\"id-5\",\"slideNumber\":3,\"matrixValue\":[[2.38,1.42],[1.85,2.31]],\"linkedMatricesIds\":[\"id-9\"]},{\"matrixId\":\"id-6\",\"slideNumber\":3,\"matrixValue\":[[0.0,0.0],[0.0,0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-7\",\"slideNumber\":3,\"matrixValue\":[[0.0,0.0],[0.0,0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-8\",\"slideNumber\":3,\"matrixValue\":[[0.0,0.0],[0.0,0.0]],\"linkedMatricesIds\":[\"id-12\"]},{\"matrixId\":\"id-9\",\"slideNumber\":4,\"matrixValue\":[[1.99]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-10\",\"slideNumber\":4,\"matrixValue\":[[0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-11\",\"slideNumber\":4,\"matrixValue\":[[0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-12\",\"slideNumber\":4,\"matrixValue\":[[0.0]],\"linkedMatricesIds\":[]}]}";

        GeneratingResult generatingResult = new GeneratingResult("unused", variantJson, "unused");
        CheckProcessor.CheckingSingleConditionResult result = checkProcessor.checkSingleCondition(null, studentSolution,
                generatingResult);

        assertEquals(1.0, result.getResult().doubleValue(), 0.0000001);
        assertEquals("", result.getComment());
    }

    @Test
    public void testSolutionWithWrongMSE() throws Exception {
        String studentSolution = "{\"mse\":0.22,\"matrices\":[{\"matrixId\":\"id-0\",\"slideNumber\":0,\"matrixValue\":[[0.86,0.11,0.17,0.04,0.56,0.39,0.99],[0.66,0.97,0.74,0.36,0.19,0.8,0.71],[0.62,0.11,0.6,0.09,0.14,0.7,0.47],[0.71,0.29,0.58,0.9,0.68,0.23,0.96],[0.65,0.18,0.4,0.55,0.19,0.4,0.24],[0.24,0.58,0.34,0.25,0.92,0.57,0.63],[0.88,0.87,0.74,0.68,0.86,0.5,0.98]],\"linkedMatricesIds\":[\"id-1\",\"id-2\"]},{\"matrixId\":\"id-1\",\"slideNumber\":1,\"matrixValue\":[[1.83,0.85,0.53,0.23,1.36,1.1],[0.77,1.57,0.83,0.5,0.89,1.27],[0.91,0.69,1.5,0.77,0.37,1.66],[0.89,0.69,1.13,1.09,1.08,0.47],[1.23,0.52,0.65,1.47,0.76,1.03],[1.11,1.32,1.02,1.11,1.42,1.55]],\"linkedMatricesIds\":[\"id-3\"]},{\"matrixId\":\"id-2\",\"slideNumber\":1,\"matrixValue\":[[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0]],\"linkedMatricesIds\":[\"id-4\"]},{\"matrixId\":\"id-3\",\"slideNumber\":2,\"matrixValue\":[[1.26,0.52,1.16],[0.79,1.12,0.9],[1.05,1.06,1.19]],\"linkedMatricesIds\":[\"id-5\",\"id-8\"]},{\"matrixId\":\"id-4\",\"slideNumber\":2,\"matrixValue\":[[0.0,0.0,0.0],[0.0,0.0,0.0],[0.0,0.0,0.0]],\"linkedMatricesIds\":[\"id-8\",\"id-8\"]},{\"matrixId\":\"id-5\",\"slideNumber\":3,\"matrixValue\":[[2.38,1.42],[1.85,2.31]],\"linkedMatricesIds\":[\"id-9\"]},{\"matrixId\":\"id-6\",\"slideNumber\":3,\"matrixValue\":[[0.0,0.0],[0.0,0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-7\",\"slideNumber\":3,\"matrixValue\":[[0.0,0.0],[0.0,0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-8\",\"slideNumber\":3,\"matrixValue\":[[0.0,0.0],[0.0,0.0]],\"linkedMatricesIds\":[\"id-12\"]},{\"matrixId\":\"id-9\",\"slideNumber\":4,\"matrixValue\":[[1.99]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-10\",\"slideNumber\":4,\"matrixValue\":[[0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-11\",\"slideNumber\":4,\"matrixValue\":[[0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-12\",\"slideNumber\":4,\"matrixValue\":[[0.0]],\"linkedMatricesIds\":[]}]}";

        GeneratingResult generatingResult = new GeneratingResult("unused", variantJson, "unused");
        CheckProcessor.CheckingSingleConditionResult result = checkProcessor.checkSingleCondition(null, studentSolution,
                generatingResult);

        assertEquals(0.84, result.getResult().doubleValue(), 0.0000001);
        assertEquals("MSE отличается от правильного (0.25) больше чем на 0.01", result.getComment());
    }

    @Test
    public void testSolutionWithOnlyOneValidMatrix() throws Exception {
        String studentSolution = "{\"mse\":0.25,\"matrices\":[{\"matrixId\":\"id-0\",\"slideNumber\":0,\"matrixValue\":[[0.86,0.11,0.17,0.04,0.56,0.39,0.99],[0.66,0.97,0.74,0.36,0.19,0.8,0.71],[0.62,0.11,0.6,0.09,0.14,0.7,0.47],[0.71,0.29,0.58,0.9,0.68,0.23,0.96],[0.65,0.18,0.4,0.55,0.19,0.4,0.24],[0.24,0.58,0.34,0.25,0.92,0.57,0.63],[0.88,0.87,0.74,0.68,0.86,0.5,0.98]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-1\",\"slideNumber\":1,\"matrixValue\":[[1.83,0.85,0.53,0.23,1.36,1.1],[0.77,1.57,0.83,0.5,0.89,1.27],[0.91,0.69,1.5,0.77,0.37,1.66],[0.89,0.69,1.13,1.09,1.08,0.47],[1.23,0.52,0.65,1.47,0.76,1.03],[1.11,1.32,1.02,1.11,1.42,1.55]]," +
                "\"linkedMatricesIds\":[]}]}";

        GeneratingResult generatingResult = new GeneratingResult("unused", variantJson, "unused");
        CheckProcessor.CheckingSingleConditionResult result = checkProcessor.checkSingleCondition(null, studentSolution,
                generatingResult);

        assertEquals(0.15, result.getResult().doubleValue(), 0.0000001);
        assertEquals("Ожидаемое количество матриц в ответе - 13, актульное количество - 2", result.getComment());
    }

    @Test
    public void testSolutionWithOneValidMatrixAndInvalidSecond_1() throws Exception {
        String studentSolution = "{\"mse\":0.22,\"matrices\":[{\"matrixId\":\"id-0\",\"slideNumber\":0,\"matrixValue\":[[0.86,0.11,0.17,0.04,0.56,0.39,0.99],[0.66,0.97,0.74,0.36,0.19,0.8,0.71],[0.62,0.11,0.6,0.09,0.14,0.7,0.47],[0.71,0.29,0.58,0.9,0.68,0.23,0.96],[0.65,0.18,0.4,0.55,0.19,0.4,0.24],[0.24,0.58,0.34,0.25,0.92,0.57,0.63],[0.88,0.87,0.74,0.68,0.86,0.5,0.98]],\"linkedMatricesIds\":[\"id-1\",\"id-2\"]},{\"matrixId\":\"id-1\",\"slideNumber\":1,\"matrixValue\":[[1.83,0.85,0.53,0.23,1.36,1.1],[0.77,1.57,0.83,0.5,0.89,1.27],[0.91,0.69,1.5,0.77,0.37,1.66],[0.89,0.69,1.13,1.09,1.08,0.47],[1.23,0.52,0.65,1.47,0.76,1.03],[1.11,1.32,1.02,1.11,1.42,1.55]],\"linkedMatricesIds\":[\"id-3\"]},{\"matrixId\":\"id-2\",\"slideNumber\":1,\"matrixValue\":[[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,1.2323,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0]],\"linkedMatricesIds\":[]}]}";

        GeneratingResult generatingResult = new GeneratingResult("unused", variantJson, "unused");
        CheckProcessor.CheckingSingleConditionResult result = checkProcessor.checkSingleCondition(null, studentSolution,
                generatingResult);

        assertEquals(0.15, result.getResult().doubleValue(), 0.0000001);
        assertEquals("Матрица id-2, ячейка (3,3): ожидаемое значение - 0.0, актуальное значение - 1.2323", result.getComment());
    }

    @Test
    public void testSolutionWithOneValidMatrixAndInvalidSecond_2() throws Exception {
        String studentSolution = "{\"mse\":0.22,\"matrices\":[{\"matrixId\":\"id-0\",\"slideNumber\":0,\"matrixValue\":[[0.86,0.11,0.17,0.04,0.56,0.39,0.99],[0.66,0.97,0.74,0.36,0.19,0.8,0.71],[0.62,0.11,0.6,0.09,0.14,0.7,0.47],[0.71,0.29,0.58,0.9,0.68,0.23,0.96],[0.65,0.18,0.4,0.55,0.19,0.4,0.24],[0.24,0.58,0.34,0.25,0.92,0.57,0.63],[0.88,0.87,0.74,0.68,0.86,0.5,0.98]],\"linkedMatricesIds\":[\"id-1\",\"id-2\"]},{\"matrixId\":\"id-1\",\"slideNumber\":1,\"matrixValue\":[[1.83,0.85,0.53,0.23,1.36,1.1],[0.77,1.57,0.83,0.5,0.89,1.27],[0.91,0.69,1.5,0.77,0.37,1.66],[0.89,0.69,1.13,1.09,1.08,0.47],[1.23,0.52,0.65,1.47,0.76,1.03],[1.11,1.32,1.02,1.11,1.42,1.55]],\"linkedMatricesIds\":[\"id-3\"]},{\"matrixId\":\"id-2\",\"slideNumber\":1,\"matrixValue\":[[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,1.2323,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0]],\"linkedMatricesIds\":[]}]}";

        GeneratingResult generatingResult = new GeneratingResult("unused", variantJson, "unused");
        CheckProcessor.CheckingSingleConditionResult result = checkProcessor.checkSingleCondition(null, studentSolution,
                generatingResult);

        assertEquals(0.15, result.getResult().doubleValue(), 0.0000001);
        assertEquals("Матрица id-2 должна иметь размер 6x6, но имеет размер 5x6", result.getComment());
    }
}
