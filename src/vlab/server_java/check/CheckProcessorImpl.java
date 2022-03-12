package vlab.server_java.check;

import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.PreCheckProcessor.PreCheckResult;
import rlcp.server.processor.check.PreCheckResultAwareCheckProcessor;
import vlab.server_java.JacksonHelper;
import vlab.server_java.generate.Variant;
import vlab.server_java.model.Solution;

import java.math.BigDecimal;

/**
 * Simple CheckProcessor implementation. Supposed to be changed as needed to provide
 * necessary Check method support.
 */

public class CheckProcessorImpl implements PreCheckResultAwareCheckProcessor<String> {
    @Override
    public CheckingSingleConditionResult checkSingleCondition(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) {
        System.out.println("instructions in checkProcessor" + instructions);

        double points = 0.323;
        String comment = "GOOOOOD";

        try {
            Variant generatedVariant = JacksonHelper.fromJson(generatingResult.getCode(), Variant.class);
            Solution ourSolution = new Solution(generatedVariant);
            Solution studentSolution = new Solution(instructions);
            //compare ourSolution and studentSolution
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CheckingSingleConditionResult(BigDecimal.valueOf(points), comment);
    }

    @Override
    public void setPreCheckResult(PreCheckResult<String> preCheckResult) {
    }
}
