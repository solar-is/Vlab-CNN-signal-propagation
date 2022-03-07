package vlab.server_java.model.nodes;

import vlab.server_java.model.Matrix;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MatrixNetNode extends AbstractNetNode<Matrix> {
    public MatrixNetNode() {
    }

    public MatrixNetNode(int layerNumber,
                         @Nullable ConvNetNode<?> prevNode,
                         @Nullable List<ConvNetNode<?>> nextNodes,
                         @Nonnull Matrix matrix) {
        super(layerNumber, prevNode, nextNodes, matrix);
    }
}
