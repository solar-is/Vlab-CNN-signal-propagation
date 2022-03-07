package vlab.server_java.model.nodes;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.annotation.Nullable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractNetNode<T> implements ConvNetNode<T> {
    private final int layerNumber;
    private final ConvNetNode<?> prevNode;
    private final List<ConvNetNode<?>> nextNodes;

    public AbstractNetNode(int layerNumber,
                           @Nullable ConvNetNode<?> prevNode,
                           @Nullable List<ConvNetNode<?>> nextNodes) {
        this.layerNumber = layerNumber;
        this.prevNode = prevNode;
        this.nextNodes = nextNodes;
    }

    @Override
    public int getLayerNumber() {
        return layerNumber;
    }

    @Nullable
    @Override
    public ConvNetNode<?> getPreviousConnectedNode() {
        return prevNode;
    }

    @Nullable
    @Override
    public List<ConvNetNode<?>> getNextConnectedNodes() {
        return nextNodes;
    }
}
