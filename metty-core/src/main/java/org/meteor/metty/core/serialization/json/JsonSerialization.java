package org.meteor.metty.core.serialization.json;

import com.alibaba.fastjson.JSON;
import org.meteor.metty.core.serialization.Serialization;
import org.meteor.metty.core.exception.SerializeException;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: JsonSerialization
 * @Created Time: 2024-04-02 17:12
 **/
public class JsonSerialization implements Serialization {

    @Override
    public <T> byte[] serialize(T object) {
        try{
            String jsonString = JSON.toJSONString(object);
            return jsonString.getBytes();
        }catch (Exception e){
            throw new SerializeException("Fastjson serialization failed ");
        }
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        String jsonString = new String(bytes);
        try{
            return JSON.parseObject(jsonString,clazz);
        }catch (Exception e){
            throw new SerializeException("Fastjson deserialization failed ");
        }
    }

    public static void main(String[] args) {

    }
}
