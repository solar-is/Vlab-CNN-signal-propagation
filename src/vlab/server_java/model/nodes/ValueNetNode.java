package vlab.server_java.model.nodes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ValueNetNode extends AbstractNetNode<Double> {
    private final double value;

    public ValueNetNode(int layerNumber,
                        @Nullable ConvNetNode<?> prevNode,
                        @Nullable List<ConvNetNode<?>> nextNodes,
                        double value) {
        super(layerNumber, prevNode, nextNodes);
        this.value = value;
    }

    @Nonnull
    @Override
    public Double getPayload() {
        return value;
    }
}
