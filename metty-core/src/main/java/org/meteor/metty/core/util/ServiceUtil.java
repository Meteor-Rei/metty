package org.meteor.metty.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.meteor.metty.core.common.ServiceInfo;

import java.util.Collections;
import java.util.Map;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: ServiceUtil
 * @Created Time: 2024-04-08 23:27
 **/
public class ServiceUtil {


    public static String serviceKey(String serverName, String version) {

        return String.join("-", serverName, version);
    }



    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map toMap(ServiceInfo serviceInfo) {
        if (serviceInfo == null) {
            return Collections.emptyMap();
        }

        Map map = JSONObject.parseObject(JSONObject.toJSONString(serviceInfo), Map.class);
        map.put("port", serviceInfo.getPort().toString());
        return map;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ServiceInfo toServiceInfo(Map map) {
        map.put("port", Integer.parseInt(map.getOrDefault("port", "0").toString()));
        return JSON.parseObject(new JSONObject(map).toJSONString(), ServiceInfo.class);

    }
}
