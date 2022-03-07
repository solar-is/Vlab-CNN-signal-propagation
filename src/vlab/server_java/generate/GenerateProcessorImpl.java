package vlab.server_java.generate;

import org.json.JSONObject;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.generate.GenerateProcessor;

/**
 * Simple GenerateProcessor implementation. Supposed to be changed as needed to
 * provide necessary Generate method support.
 */
public class GenerateProcessorImpl implements GenerateProcessor {
    @Override
    public GeneratingResult generate(String condition) {
        //do Generate logic here
        String variantInJson = "";
        try {
            JSONObject graph = new JSONObject();
            graph.put("qwe", "qwe");
            variantInJson = graph.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new GeneratingResult("text from generateProcessor", variantInJson, "");
    }
}
