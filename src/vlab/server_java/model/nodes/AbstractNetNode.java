package vlab.server_java.model.nodes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MatrixNetNode.class, name = "MatrixNode"),
        @JsonSubTypes.Type(value = ValueNetNode.class, name = "ValueNode")
}
)
public class AbstractNetNode<T> implements ConvNetNode<T> {
    public int layerNumber;
    public ConvNetNode<?> prevNode;
    public List<ConvNetNode<?>> nextNodes;
    public T payload;

    public AbstractNetNode() {
    }

    public AbstractNetNode(int layerNumber,
                           @Nullable ConvNetNode<?> prevNode,
                           @Nullable List<ConvNetNode<?>> nextNodes,
                           @Nonnull T payload) {
        this.layerNumber = layerNumber;
        this.prevNode = prevNode;
        this.nextNodes = nextNodes;
        this.payload = payload;
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    @Nullable
    public ConvNetNode<?> getPrevNode() {
        return prevNode;
    }

    @Nullable
    public List<ConvNetNode<?>> getNextNodes() {
        return nextNodes;
    }

    @Nonnull
    public T getPayload() {
        return payload;
    }

    public void setLayerNumber(int layerNumber) {
        this.layerNumber = layerNumber;
    }

    public void setPrevNode(ConvNetNode<?> prevNode) {
        this.prevNode = prevNode;
    }

    public void setNextNodes(List<ConvNetNode<?>> nextNodes) {
        this.nextNodes = nextNodes;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
