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
    ConvNetNode<?> getPrevNode();

    @Nullable
    List<ConvNetNode<?>> getNextNodes();

    @Nonnull
    T getPayload();
}
