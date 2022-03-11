package vlab.server_java;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Helper class to perform serialization/deserialization to/from JSON using Jackson library.
 *
 * @author mprosolovich
 * @since 07.03.2022
 */
public final class JacksonHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JacksonHelper() {
    }

    /**
     * Serialize object to JSON.
     * Does not throw checked exception (it is converted to {@code UncheckedIOException}).
     *
     * @param object object to serialize
     * @return serialization form of object in JSON string
     */
    public static String toJson(@Nonnull Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Deserialize object from JSON string.
     * Does not throw checked exception (it is converted to {@code UncheckedIOException}).
     *
     * @param json  serialization form of object in JSON string
     * @param clazz class of object
     * @param <T>   object class
     * @return deserialized object
     */
    public static <T> T fromJson(@Nonnull String json, @Nonnull Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
