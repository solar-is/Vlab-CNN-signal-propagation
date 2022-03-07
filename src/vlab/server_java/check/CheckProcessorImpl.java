package vlab.server_java.check;

import org.json.JSONObject;
import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.PreCheckProcessor.PreCheckResult;
import rlcp.server.processor.check.PreCheckResultAwareCheckProcessor;

import java.math.BigDecimal;

/**
 * Simple CheckProcessor implementation. Supposed to be changed as needed to provide
 * necessary Check method support.
 */

public class CheckProcessorImpl implements PreCheckResultAwareCheckProcessor<String> {
    @Override
    public CheckingSingleConditionResult checkSingleCondition(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) {
        double points = 0;
        String comment = "";

        try {
            String code = generatingResult.getCode();
            JSONObject jsonCode = new JSONObject(code);
            JSONObject jsonInstructions = new JSONObject(instructions);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CheckingSingleConditionResult(BigDecimal.valueOf(points), comment);
    }

    @Override
    public void setPreCheckResult(PreCheckResult<String> preCheckResult) {
    }
}
