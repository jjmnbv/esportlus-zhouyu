package com.kaihei.esportingplus.riskrating.util;

import com.kaihei.esportingplus.common.tools.HttpUtils;
import com.kaihei.esportingplus.riskrating.api.params.ShumeiQueryParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数美API工具类
 */
public class ShumeiApiUtil {

    private static final Logger logger = LoggerFactory.getLogger(ShumeiApiUtil.class);

    public static String post(ShumeiQueryParams params) {

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("accessKey", params.getAccessKey());
        requestMap.put("appId", "default");
        requestMap.put("eventId", params.getEventType());

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("tokenId", "0");
        dataMap.put("deviceId", params.getDeviceId());
        dataMap.put("phone", params.getPhone());
        int time = (int) (new Date().getTime() / 1000);
        dataMap.put("timestamp", time);
        requestMap.put("data", dataMap);

        RestTemplate restTemplate = new RestTemplate();
        logger.debug("riskrating >> checkMaliceDevice >> 数美入参 >> " + requestMap.toString());
        String thirdResponse = restTemplate.postForObject(params.getShumeiBaseUrl(), HttpUtils.buildParam(requestMap), String.class);

        return thirdResponse;
    }

}
