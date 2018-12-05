package com.kaihei.esportingplus.user.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.user.api.vo.PythonFreeteamChancesInfoVo;
import com.kaihei.esportingplus.user.config.UserProperties;
import javax.annotation.Resource;

import com.kaihei.esportingplus.user.domain.service.FreeTeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-09 18:14
 * @Description:
 */
@Component
public class FreeTeamServiceImpl implements FreeTeamService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Autowired
    private UserProperties userProperties;

    /**
     * 调用python接口为用户添加免费车队次数
     */
    @Override
    public boolean callPythonToIncreFreeTimes(String uid, int freeCount) {
        JSONObject json = new JSONObject();
        json.put("uid", uid);
        json.put("count", freeCount);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity(json.toJSONString(), headers);
        String domain = userProperties.getPythonDomain();
        if (domain != null) {
            if (!domain.endsWith("/")) {
                domain = domain + "/";
            }
        }
        String url = domain + userProperties.getFreeCountUrl();
        logger.info(
                "cmd=FreeTeamService.callPythonToIncreFreeTimes | msg=开始调用python添加免费车队次数接口 | uid={} | freeCount={} | url={}",
                uid, freeCount, url);
        long start = System.currentTimeMillis();
        ResponsePacket resp = null;
        try {
            resp = restTemplateExtrnal.postForObject(url, entity, ResponsePacket.class);
        } catch (Exception e) {
            logger.error(
                    "cmd=FreeTeamService.callPythonToIncreFreeTimes | msg=调用python添加免费车队次数接口抛出异常| uid={} | resp={} | cost={}ms",
                    uid, JsonsUtils.toJson(resp), System.currentTimeMillis() - start, e);
        }
        if (resp == null || !resp.responseSuccess()) {
            logger.info(
                    "cmd=FreeTeamService.callPythonToIncreFreeTimes | msg=调用python添加免费车队次数接口返回失败 | uid={} | freeCount={} | resp={} | cost={}ms",
                    uid, freeCount, JsonsUtils.toJson(resp), System.currentTimeMillis() - start);
            return false;
        }
        logger.info(
                "cmd=FreeTeamService.callPythonToIncreFreeTimes | msg=结束调用python添加免费车队次数接口 | uid={} | freeCount={} | cost={}ms",
                uid, freeCount, System.currentTimeMillis() - start);
        return true;
    }

    /**
     * 调用python接口查询免费车队免单统计信息
     */
    @Override
    public PythonFreeteamChancesInfoVo callPythonToGetFreeChancesInfo(Integer userId) {
        String domain = userProperties.getPythonDomain();
        if (domain != null) {
            if (!domain.endsWith("/")) {
                domain = domain + "/";
            }
        }
        String url = domain + userProperties.getFreeChancesInfo();
        if (url != null) {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length()-1);
            }
            url = url + "?user_id=" + userId;
        }
        logger.info(
                "cmd=FreeTeamService.callPythonToGetFreeChancesInfo | msg=请求用户免单详情接口开始 | userId={} | url={}",
                userId, url);
        long start = System.currentTimeMillis();
        JSONObject resp = null;
        try {
            resp = restTemplateExtrnal.getForObject(url, JSONObject.class);
        } catch (Exception e) {
            logger.error(
                    "cmd=FreeTeamService.callPythonToGetFreeChancesInfo | msg=请求用户免单详情接口抛出异常| userId={} | resp={} | cost={}ms",
                    userId, JsonsUtils.toJson(resp), System.currentTimeMillis() - start, e);
        }
        if (resp == null || resp.getInteger("code") != 0) {
            logger.error(
                    "cmd=FreeTeamService.callPythonToGetFreeChancesInfo | msg=请求用户免单详情接口返回失败 | userId={} | resp={} | cost={}ms",
                    userId, JsonsUtils.toJson(resp), System.currentTimeMillis() - start);
            return null;
        }
        PythonFreeteamChancesInfoVo vo = JacksonUtils
                .toBeanWithSnake(resp.getJSONObject("data").toJSONString(), PythonFreeteamChancesInfoVo.class);
        logger.info(
                "cmd=FreeTeamService.callPythonToGetFreeChancesInfo | msg=结束请求用户免单详情接口 | userId={} | cost={}ms",
                userId, System.currentTimeMillis() - start);
        return vo;
    }
}
