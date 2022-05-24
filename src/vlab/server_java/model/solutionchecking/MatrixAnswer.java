package vlab.server_java.model.solutionchecking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class MatrixAnswer {
    public String matrixId;
    public int slideNumber;
    public double[][] matrixValue;
    public List<String> linkedMatricesIds;

    @JsonCreator
    public MatrixAnswer(@Nonnull @JsonProperty("matrixId") String matrixId,
                        @JsonProperty("slideNumber") int slideNumber,
                        @JsonProperty("matrixValue") double[][] matrixValue,
                        @JsonProperty("linkedMatricesIds") List<String> linkedMatricesIds) {
        this.matrixId = matrixId;
        this.slideNumber = slideNumber;
        this.matrixValue = matrixValue;
        this.linkedMatricesIds = linkedMatricesIds;
    }

    @Override
    public String toString() {
        return "MatrixAnswer{" +
                "matrixId='" + matrixId + '\'' +
                ", slideNumber=" + slideNumber +
                ", matrixValue=" + Arrays.toString(matrixValue) +
                ", linkedMatricesIds=" + linkedMatricesIds +
                '}';
    }
}
