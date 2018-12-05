package com.kaihei.esportingplus.auth.server.remote.rest;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.HttpUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExternalRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalRestClient.class);

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Value("${python.tokenUrl}")
    private String tokenUrl;

    @Value("${python.userDetailUrl}")
    private String userDetailUrl;

    @Value("${python.uid.mock}")
    private boolean mock;

    @Value("${python.retryInterval}")
    private String retryInterval;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取uid
     */
    public String getUidByToken(String pyToken) {
        if (mock) {
            return pyToken;
        }
        LOGGER.info("获取python UID：url={},param={}", tokenUrl, pyToken);
        Map<String, String> param = new HashMap<>(1);
        param.put("token", pyToken);

        //重试: 基于redis自增重试
        long incr;
        String[] intervals = retryInterval.split("/");
        do {
            try {
                ResponseEntity<ResponsePacket> response = restTemplateExtrnal
                        .postForEntity(tokenUrl, param, ResponsePacket.class);
                if (response.getStatusCode().value() == 200
                        && response.getBody() != null
                        && response.getBody().getCode() == CommonConstants.SUCCESS
                        && response.getBody().getData() != null
                        && StringUtils
                        .isNoneBlank(((Map) response.getBody().getData()).get("uid").toString())) {

                    LOGGER.info("请求token[{}]校验成功", pyToken);
                    //删除重试次数
                    if (redisTemplate.hasKey(RedisKey.GET_UID_RETRY + pyToken)) {
                        redisTemplate.delete(RedisKey.GET_UID_RETRY + pyToken);
                    }

                    return ((Map) response.getBody().getData()).get("uid").toString();
                } else {
                    //如果拿到了401状态码直接返回,其他状态码(如：500)重试
                    if (response.getBody() != null
                            && response.getBody().getCode() == HttpStatus.SC_UNAUTHORIZED) {
                        break;
                    }
                    LOGGER.error(FastJsonUtils.toJson(response.getBody()));
                    incr = redisTemplate.opsForValue()
                            .increment(RedisKey.GET_UID_RETRY + pyToken, 1);
                    try {
                        LOGGER.error("请求token[{}]校验失败，{}ms后,重试：{}次",
                                pyToken,
                                intervals[(int) incr - 1],
                                incr);
                        Thread.sleep(Long.valueOf(intervals[(int) incr - 1]));
                    } catch (InterruptedException e) {
                        LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(), e);
                        break;
                    }
                }
            } catch (Exception e) {
                //请求异常，可能：请求不可达，对方服务出问题，进行重试
                LOGGER.error(e.getMessage(), e);
                incr = redisTemplate.opsForValue().increment(RedisKey.GET_UID_RETRY + pyToken, 1);

                try {
                    LOGGER.error("请求token[{}]校验失败，{}ms后,重试：{}次",
                            pyToken,
                            intervals[(int) incr - 1],
                            incr);
                    Thread.sleep(Long.valueOf(intervals[(int) incr - 1]));
                } catch (InterruptedException e2) {
                    LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(), e2);
                    break;
                }
            }
        } while (incr < intervals.length);

        if (redisTemplate.hasKey(RedisKey.GET_UID_RETRY + pyToken)) {
            LOGGER.error("token[{}]重试次数已达阀值：{}，不在重试,uid都弄不到，宣布凉凉",pyToken, intervals.length);
            redisTemplate.delete(RedisKey.GET_UID_RETRY + pyToken);
        }
        throw new BusinessException(BizExceptionEnum.INVALID_PY_TOKEN);
    }

    public ResponsePacket getUserDetailByUid(String uid) {
        LOGGER.info("获取用户信息: url={},param={}", userDetailUrl, uid);

        //重试: 基于redis自增重试
        long incr;
        String[] intervals = retryInterval.split("/");
        do {
            try {
                ResponsePacket responsePacket = restTemplateExtrnal
                        .getForObject(userDetailUrl + "?uid=" + uid, ResponsePacket.class);
                if (responsePacket != null
                        && responsePacket.getCode() == BizExceptionEnum.SUCCESS.getErrCode()
                        && responsePacket.getData() != null) {

                    LOGGER.info("获取用户[{}]信息成功", uid);
                    //删除重试次数
                    if (redisTemplate.hasKey(RedisKey.GET_UERINFO_RETRY + uid)) {
                        redisTemplate.delete(RedisKey.GET_UERINFO_RETRY + uid);
                    }

                    return responsePacket;
                } else {
                    LOGGER.error(responsePacket.toString());
                    incr = redisTemplate.opsForValue()
                            .increment(RedisKey.GET_UERINFO_RETRY + uid, 1);
                    try {
                        LOGGER.error("获取用户[{}]信息失败，{}ms后,重试：{}次",
                                uid,
                                intervals[(int) incr - 1],
                                incr);
                        Thread.sleep(Long.valueOf(intervals[(int) incr - 1]));
                    } catch (InterruptedException e) {
                        LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(), e);
                        break;
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                incr = redisTemplate.opsForValue().increment(RedisKey.GET_UERINFO_RETRY + uid, 1);

                try {
                    LOGGER.error("获取用户[{}]信息失败，{}ms后,重试：{}次",
                            uid,
                            intervals[(int) incr - 1],
                            incr);
                    Thread.sleep(Long.valueOf(intervals[(int) incr - 1]));
                } catch (InterruptedException e2) {
                    LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(), e2);
                    break;
                }
            }
        } while (incr < intervals.length);

        if (redisTemplate.hasKey(RedisKey.GET_UERINFO_RETRY + uid)) {
            LOGGER.error("获取用户[{}]重试次数已达阀值：{}，不在重试",uid, intervals.length);
            redisTemplate.delete(RedisKey.GET_UERINFO_RETRY + uid);
        }

        return null;
    }

}