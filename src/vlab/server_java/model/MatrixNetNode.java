package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatrixNetNode {
    public int layerNumber;
    public List<MatrixNetNode> nextNodes;
    public Matrix payload;

    public MatrixNetNode() {
    }

    public MatrixNetNode(int layerNumber,
                         @Nullable List<MatrixNetNode> nextNodes,
                         @Nonnull Matrix payload) {
        this.layerNumber = layerNumber;
        this.nextNodes = nextNodes;
        this.payload = payload;
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    @Nullable
    public List<MatrixNetNode> getNextNodes() {
        return nextNodes;
    }

    @Nonnull
    public Matrix getPayload() {
        return payload;
    }

    public void setLayerNumber(int layerNumber) {
        this.layerNumber = layerNumber;
    }


    public void setNextNodes(List<MatrixNetNode> nextNodes) {
        this.nextNodes = nextNodes;
    }

    public void setPayload(Matrix payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatrixNetNode that = (MatrixNetNode) o;
        return layerNumber == that.layerNumber &&
                Objects.equals(nextNodes, that.nextNodes) &&
                payload.equals(that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layerNumber, nextNodes, payload);
    }

    @Override
    public String toString() {
        return "MatrixNetNode{" +
                "layerNumber=" + layerNumber +
                ", nextNodes=" + nextNodes +
                ", payload=" + payload +
                '}';
    }
}
