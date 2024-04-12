package org.meteor.metty.core.serialization;

import org.meteor.metty.core.enums.SerializationType;
import org.meteor.metty.core.serialization.json.JsonSerialization;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: SerializationFactory
 * @Created Time: 2024-04-06 20:49
 **/
public class SerializationFactory {
    public static Serialization getSerialization(SerializationType enumType) {
        switch (enumType) {
            case JSON:
                return new JsonSerialization();
            default:
                throw new IllegalArgumentException(String.format("The serialization type %s is illegal.",
                        enumType.name()));
        }
    }
}
