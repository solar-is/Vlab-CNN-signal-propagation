import org.junit.Test;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.CheckProcessor;
import vlab.server_java.JacksonHelper;
import vlab.server_java.generate.Variant;
import vlab.server_java.model.Matrix;
import vlab.server_java.model.MatrixNetNode;

import java.util.Arrays;

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
    public void testSolutionWithDashesCodes() throws Exception {
        String variantJson = "{\"inputNode\":{\"layerNumber\":0,\"payload\":{\"matrix\":[[0.19,0.33,0.53,0.18,0.52,0.73,0.56],[0.44,0.23,0.67,0.07,0.91,0.12,0.55],[0.86,0.16,0.09,0.04,0.86,0.45,0.82],[0.11,0.88,0.02,0.45,0.55,0.7,0.56],[0.86,0.29,0.3,0.61,0.73,0.96,0.28],[1.0,0.23,0.99,0.04,0.7,0.3,0.49],[0.88,0.08,0.56,0.48,0.68,0.6,0.8]]}},\"kernels\":[{\"matrix\":[[0.0,-1.0],[1.0,0.0]]},{\"matrix\":[[0.0,-1.0],[-1.0,0.0]]}],\"subSamplingFunction\":\"Avg\",\"activationFunction\":\"Linear\"}";
        String studentSolutionWithDashesCodes = "{\"matrices\":[{\"matrixId\":\"id&#0045;0\",\"slideNumber\":0,\"matrixValue\":[[0.19,0.33,0.53,0.18,0.52,0.73,0.56],[0.44,0.23,0.67,0.07,0.91,0.12,0.55],[0.86,0.16,0.09,0.04,0.86,0.45,0.82],[0.11,0.88,0.02,0.45,0.55,0.7,0.56],[0.86,0.29,0.3,0.61,0.73,0.96,0.28],[1,0.23,0.99,0.04,0.7,0.3,0.49],[0.88,0.08,0.56,0.48,0.68,0.6,0.8]],\"linkedMatricesIds\":[\"id&#0045;1\",\"id&#0045;2\"]},{\"matrixId\":\"id&#0045;1\",\"slideNumber\":1,\"matrixValue\":[[0.11,&#0045;0.3,0.49,&#0045;0.45,0.18,&#0045;0.44],[0.63,&#0045;0.51,0.02,&#0045;0.87,0.74,&#0045;0.1],[&#0045;0.05,0.79,&#0045;0.02,&#0045;0.41,0.1,&#0045;0.12],[&#0045;0.02,0.27,&#0045;0.15,0.06,0.03,0.4],[0.71,&#0045;0.07,0.38,&#0045;0.69,&#0045;0.26,0.02],[0.65,&#0045;0.91,0.52,&#0045;0.22,0.38,0.11]],\"linkedMatricesIds\":[\"id&#0045;3\"]},{\"matrixId\":\"id&#0045;2\",\"slideNumber\":1,\"matrixValue\":[[&#0045;0.77,&#0045;0.76,&#0045;0.85,&#0045;0.59,&#0045;1.64,&#0045;0.68],[&#0045;1.09,&#0045;0.83,&#0045;0.16,&#0045;0.95,&#0045;0.98,&#0045;1],[&#0045;0.27,&#0045;0.97,&#0045;0.06,&#0045;1.31,&#0045;1,&#0045;1.52],[&#0045;1.74,&#0045;0.31,&#0045;0.75,&#0045;1.16,&#0045;1.43,&#0045;1.52],[&#0045;1.29,&#0045;0.53,&#0045;1.6,&#0045;0.77,&#0045;1.66,&#0045;0.58],[&#0045;1.11,&#0045;1.07,&#0045;0.6,&#0045;1.18,&#0045;0.98,&#0045;1.09]],\"linkedMatricesIds\":[\"id&#0045;4\"]},{\"matrixId\":\"id&#0045;3\",\"slideNumber\":2,\"matrixValue\":[[&#0045;0.02,&#0045;0.2,0.1],[0.25,&#0045;0.13,0.1],[0.1,0,0.06]],\"linkedMatricesIds\":[\"id&#0045;5\",\"id&#0045;6\"]},{\"matrixId\":\"id&#0045;4\",\"slideNumber\":2,\"matrixValue\":[[&#0045;0.86,&#0045;0.64,&#0045;1.08],[&#0045;0.82,&#0045;0.82,&#0045;1.37],[&#0045;1,&#0045;1.04,&#0045;1.08]],\"linkedMatricesIds\":[\"id&#0045;7\",\"id&#0045;8\"]},{\"matrixId\":\"id&#0045;5\",\"slideNumber\":3,\"matrixValue\":[[0.45,&#0045;0.23],[0.23,&#0045;0.1]],\"linkedMatricesIds\":[\"id&#0045;9\"]},{\"matrixId\":\"id&#0045;6\",\"slideNumber\":3,\"matrixValue\":[[&#0045;0.05,0.03],[0.03,&#0045;0.1]],\"linkedMatricesIds\":[\"id&#0045;10\"]},{\"matrixId\":\"id&#0045;7\",\"slideNumber\":3,\"matrixValue\":[[&#0045;0.18,0.26],[&#0045;0.18,0.33]],\"linkedMatricesIds\":[\"id&#0045;11\"]},{\"matrixId\":\"id&#0045;8\",\"slideNumber\":3,\"matrixValue\":[[1.46,1.9],[1.82,2.41]],\"linkedMatricesIds\":[\"id&#0045;12\"]},{\"matrixId\":\"id&#0045;9\",\"slideNumber\":4,\"matrixValue\":[[0.09]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id&#0045;10\",\"slideNumber\":4,\"matrixValue\":[[&#0045;0.02]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id&#0045;11\",\"slideNumber\":4,\"matrixValue\":[[0.06]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id&#0045;12\",\"slideNumber\":4,\"matrixValue\":[[1.9]],\"linkedMatricesIds\":[]}],\"mse\":0.21}";
        GeneratingResult generatingResult = new GeneratingResult("unused", variantJson, "unused");
        CheckProcessor.CheckingSingleConditionResult result = checkProcessor.checkSingleCondition(null, studentSolutionWithDashesCodes,
                generatingResult);

        assertEquals(1.0, result.getResult().doubleValue(), 0.0000001);
        assertEquals("", result.getComment());
    }

    @Test
    public void testFullValidSolutionWithGarbageIds() throws Exception {
        String studentSolution = "{\"mse\":0.25,\"matrices\":[{\"matrixId\":\"id-23\",\"slideNumber\":0,\"matrixValue\":[[0.86,0.11,0.17,0.04,0.56,0.39,0.99],[0.66,0.97,0.74,0.36,0.19,0.8,0.71],[0.62,0.11,0.6,0.09,0.14,0.7,0.47],[0.71,0.29,0.58,0.9,0.68,0.23,0.96],[0.65,0.18,0.4,0.55,0.19,0.4,0.24],[0.24,0.58,0.34,0.25,0.92,0.57,0.63],[0.88,0.87,0.74,0.68,0.86,0.5,0.98]],\"linkedMatricesIds\":[\"id-11\",\"id-24\"]},{\"matrixId\":\"id-541\",\"slideNumber\":1,\"matrixValue\":[[1.83,0.85,0.53,0.23,1.36,1.1],[0.77,1.57,0.83,0.5,0.89,1.27],[0.91,0.69,1.5,0.77,0.37,1.66],[0.89,0.69,1.13,1.09,1.08,0.47],[1.23,0.52,0.65,1.47,0.76,1.03],[1.11,1.32,1.02,1.11,1.42,1.55]],\"linkedMatricesIds\":[\"id-354\"]},{\"matrixId\":\"id-123\",\"slideNumber\":1,\"matrixValue\":[[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0]],\"linkedMatricesIds\":[\"id-4g\"]},{\"matrixId\":\"id-35\",\"slideNumber\":2,\"matrixValue\":[[1.26,0.52,1.16],[0.79,1.12,0.9],[1.05,1.06,1.19]],\"linkedMatricesIds\":[\"id-51\",\"id-82\"]},{\"matrixId\":\"id-4123\",\"slideNumber\":2,\"matrixValue\":[[0.0,0.0,0.0],[0.0,0.0,0.0],[0.0,0.0,0.0]],\"linkedMatricesIds\":[\"id-82\",\"id-855\"]},{\"matrixId\":\"id-51\",\"slideNumber\":3,\"matrixValue\":[[2.38,1.42],[1.85,2.31]],\"linkedMatricesIds\":[\"id-9\"]},{\"matrixId\":\"id-6\",\"slideNumber\":3,\"matrixValue\":[[0.0,0.0],[0.0,0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-7\",\"slideNumber\":3,\"matrixValue\":[[0.0,0.0],[0.0,0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-8\",\"slideNumber\":3,\"matrixValue\":[[0.0,0.0],[0.0,0.0]],\"linkedMatricesIds\":[\"id-12\"]},{\"matrixId\":\"id-9\",\"slideNumber\":4,\"matrixValue\":[[1.99]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-10\",\"slideNumber\":4,\"matrixValue\":[[0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-11\",\"slideNumber\":4,\"matrixValue\":[[0.0]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-12\",\"slideNumber\":4,\"matrixValue\":[[0.0]],\"linkedMatricesIds\":[]}]}";

        GeneratingResult generatingResult = new GeneratingResult("unused", variantJson, "unused");
        CheckProcessor.CheckingSingleConditionResult result = checkProcessor.checkSingleCondition(null, studentSolution,
                generatingResult);

        assertEquals(1.0, result.getResult().doubleValue(), 0.0000001);
        assertEquals("", result.getComment());
    }

    @Test
    public void testManual() throws Exception {
        String variantJson = JacksonHelper.toJson(new Variant(
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
        ));

        String studentSolution = "{\"matrices\":[{\"matrixId\":\"id-0\",\"slideNumber\":0,\"matrixValue\":[[0.15,0.36,0,0.28,0.57,0.62,0.87],[0.49,0.84,0.51,0.48,0.64,0.2,0.43],[0.74,0,0.75,0.46,0.23,0.52,0.64],[0.37,0.67,0.62,0.35,0.92,0.68,0.34],[0.13,0.98,0.26,0.94,0.08,0.42,0.34],[0.38,0.73,0.17,0.56,0.64,0.89,0.06],[0.95,0.28,0.39,0.66,0.51,0.4,0.01]],\"linkedMatricesIds\":[\"id-1\",\"id-2\"]},{\"matrixId\":\"id-1\",\"slideNumber\":1,\"matrixValue\":[[0.99,0.87,0.48,0.92,0.77,1.05],[0.49,1.59,0.97,0.71,1.16,0.84],[1.41,0.62,1.1,1.38,0.91,0.86],[1.35,0.93,1.56,0.43,1.34,1.02],[0.86,1.15,0.82,1.58,0.97,0.48],[0.66,1.12,0.83,1.07,1.04,0.9]],\"linkedMatricesIds\":[\"id-3\"]},{\"matrixId\":\"id-2\",\"slideNumber\":1,\"matrixValue\":[[-0.99,-0.87,-0.48,-0.92,-0.77,-1.05],[-0.49,-1.59,-0.97,-0.71,-1.16,-0.84],[-1.41,-0.62,-1.1,-1.38,-0.91,-0.86],[-1.35,-0.93,-1.56,-0.43,-1.34,-1.02],[-0.86,-1.15,-0.82,-1.58,-0.97,-0.48],[-0.66,-1.12,-0.83,-1.07,-1.04,-0.9]],\"linkedMatricesIds\":[\"id-4\"]},{\"matrixId\":\"id-3\",\"slideNumber\":2,\"matrixValue\":[[1.59,0.97,1.16],[1.41,1.56,1.34],[1.15,1.58,1.04]],\"linkedMatricesIds\":[\"id-5\",\"id-6\"]},{\"matrixId\":\"id-4\",\"slideNumber\":2,\"matrixValue\":[[-0.49,-0.48,-0.77],[-0.62,-0.43,-0.86],[-0.66,-0.82,-0.48]],\"linkedMatricesIds\":[\"id-7\",\"id-8\"]},{\"matrixId\":\"id-5\",\"slideNumber\":3,\"matrixValue\":[[3.15,2.31],[2.99,2.6]],\"linkedMatricesIds\":[\"id-9\"]},{\"matrixId\":\"id-6\",\"slideNumber\":3,\"matrixValue\":[[-3.15,-2.31],[-2.99,-2.6]],\"linkedMatricesIds\":[\"id-10\"]},{\"matrixId\":\"id-7\",\"slideNumber\":3,\"matrixValue\":[[-0.92,-1.34],[-1.44,-0.91]],\"linkedMatricesIds\":[\"id-11\"]},{\"matrixId\":\"id-8\",\"slideNumber\":3,\"matrixValue\":[[0.92,1.34],[1.44,0.91]],\"linkedMatricesIds\":[\"id-12\"]},{\"matrixId\":\"id-9\",\"slideNumber\":4,\"matrixValue\":[[3.15]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-10\",\"slideNumber\":4,\"matrixValue\":[[-2.31]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-11\",\"slideNumber\":4,\"matrixValue\":[[-0.91]],\"linkedMatricesIds\":[]},{\"matrixId\":\"id-12\",\"slideNumber\":4,\"matrixValue\":[[1.44]],\"linkedMatricesIds\":[]}],\"mse\":2.75}";
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
        assertEquals("Матрица 2, ячейка (3,3): ожидаемое значение - 0.0, актуальное значение - 1.2323", result.getComment());
    }

    @Test
    public void testSolutionWithOneValidMatrixAndInvalidSecond_2() throws Exception {
        String studentSolution = "{\"mse\":0.22,\"matrices\":[{\"matrixId\":\"id-0\",\"slideNumber\":0,\"matrixValue\":[[0.86,0.11,0.17,0.04,0.56,0.39,0.99],[0.66,0.97,0.74,0.36,0.19,0.8,0.71],[0.62,0.11,0.6,0.09,0.14,0.7,0.47],[0.71,0.29,0.58,0.9,0.68,0.23,0.96],[0.65,0.18,0.4,0.55,0.19,0.4,0.24],[0.24,0.58,0.34,0.25,0.92,0.57,0.63],[0.88,0.87,0.74,0.68,0.86,0.5,0.98]],\"linkedMatricesIds\":[\"id-1\",\"id-2\"]},{\"matrixId\":\"id-1\",\"slideNumber\":1,\"matrixValue\":[[1.83,0.85,0.53,0.23,1.36,1.1],[0.77,1.57,0.83,0.5,0.89,1.27],[0.91,0.69,1.5,0.77,0.37,1.66],[0.89,0.69,1.13,1.09,1.08,0.47],[1.23,0.52,0.65,1.47,0.76,1.03],[1.11,1.32,1.02,1.11,1.42,1.55]],\"linkedMatricesIds\":[\"id-3\"]},{\"matrixId\":\"id-2\",\"slideNumber\":1,\"matrixValue\":[[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,1.2323,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0],[0.0,0.0,0.0,0.0,0.0,0.0]],\"linkedMatricesIds\":[]}]}";

        GeneratingResult generatingResult = new GeneratingResult("unused", variantJson, "unused");
        CheckProcessor.CheckingSingleConditionResult result = checkProcessor.checkSingleCondition(null, studentSolution,
                generatingResult);

        assertEquals(0.15, result.getResult().doubleValue(), 0.0000001);
        assertEquals("Матрица 2 должна иметь размер 6x6, но имеет размер 5x6", result.getComment());
    }
}
