package vlab.server_java.model.nodes;

import javax.annotation.Nullable;
import java.util.List;

public class ValueNetNode extends AbstractNetNode<Double> {
    public ValueNetNode() {
    }

    public ValueNetNode(int layerNumber,
                        @Nullable ConvNetNode<?> prevNode,
                        @Nullable List<ConvNetNode<?>> nextNodes,
                        double value) {
        super(layerNumber, prevNode, nextNodes, value);
    }
}
