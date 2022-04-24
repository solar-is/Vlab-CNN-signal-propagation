import org.junit.Test;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.CheckProcessor;
import vlab.server_java.JacksonHelper;
import vlab.server_java.generate.Variant;
import vlab.server_java.model.CNNGenerator;

public class CheckLogicTests extends ServerTestBase {
    private static Variant generateVariant() {
        CNNGenerator generator = CNNGenerator.DEFAULT;
        return new Variant(
                generator.generateInputNode(),
                generator.generateConvolutionKernels(),
                generator.generateSubSamplingFunction(),
                generator.generateActivationFunction()
        );
    }

    @Test
    public void testCheckSingleCondition() throws Exception {
        String studentSolution = "studentSolutionJson";
        Variant variant = generateVariant();
        String variantJson = JacksonHelper.toJson(variant);
        GeneratingResult generatingResult = new GeneratingResult("unused", variantJson, "unused");
        CheckProcessor.CheckingSingleConditionResult result = checkProcessor.checkSingleCondition(null, studentSolution,
                generatingResult);
    }
}
