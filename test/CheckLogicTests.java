import org.junit.Test;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.CheckProcessor;

public class CheckLogicTests extends ServerTestBase {
    @Test
    public void testCheckSingleCondition() throws Exception {
        String studentSolution = "";
        GeneratingResult generatingResult = new GeneratingResult("", "", "");
        CheckProcessor.CheckingSingleConditionResult result = checkProcessor.checkSingleCondition(null, studentSolution,
                generatingResult);
    }
}
