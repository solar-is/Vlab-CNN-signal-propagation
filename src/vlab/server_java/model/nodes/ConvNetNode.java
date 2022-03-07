package vlab.server_java.model.nodes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @param <T> type of payload
 */
public interface ConvNetNode<T> {
    int getLayerNumber();

    @Nullable
    ConvNetNode<?> getPreviousConnectedNode();

    @Nullable
    List<ConvNetNode<?>> getNextConnectedNodes();

    @Nonnull
    T getPayload();
}
