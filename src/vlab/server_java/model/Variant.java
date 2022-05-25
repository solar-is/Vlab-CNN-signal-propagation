package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import vlab.server_java.model.Matrix;
import vlab.server_java.model.MatrixNetNode;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class Variant {
    public MatrixNetNode inputNode;
    public List<Matrix> kernels;
    public String subSamplingFunction;
    public String activationFunction;

    @JsonCreator
    public Variant(@Nonnull @JsonProperty("inputNode") MatrixNetNode inputNode,
                   @Nonnull @JsonProperty("kernels") List<Matrix> kernels,
                   @Nonnull @JsonProperty("subSamplingFunction") String subSamplingFunction,
                   @Nonnull @JsonProperty("activationFunction") String activationFunction) {
        this.inputNode = requireNonNull(inputNode);
        this.kernels = requireNonNull(kernels);
        this.subSamplingFunction = requireNonNull(subSamplingFunction);
        this.activationFunction = requireNonNull(activationFunction);
    }
}