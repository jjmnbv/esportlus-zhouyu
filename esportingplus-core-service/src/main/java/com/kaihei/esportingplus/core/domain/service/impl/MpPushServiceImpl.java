package com.kaihei.esportingplus.core.domain.service.impl;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.core.api.params.MPPushParam;
import com.kaihei.esportingplus.core.api.params.MpFormUploadParam;
import com.kaihei.esportingplus.core.config.WxProperties;
import com.kaihei.esportingplus.core.data.manager.WxTokenCacheManager;
import com.kaihei.esportingplus.core.data.repository.WxTemplateRepository;
import com.kaihei.esportingplus.core.domain.entity.WxTemplate;
import com.kaihei.esportingplus.core.domain.service.MpPushService;
import com.kaihei.esportingplus.user.api.feign.UserBindServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/11/5 14:34
 **/
@Service
public class MpPushServiceImpl implements MpPushService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WxProperties properties;

    @Autowired
    private WxTokenCacheManager wxTokenCacheManager;

    @Autowired
    private WxTemplateRepository wxTemplateRepository;

    @Autowired
    private UserBindServiceClient userBindServiceClient;

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Boolean push(List<MPPushParam> params) {
        List<Integer> deleteIds = new ArrayList<>();
        //查询unionid
        List<String> uids = params.stream().map(MPPushParam::getTouser).collect(Collectors.toList());
        ResponsePacket unionIdByUids = userBindServiceClient.getUnionIdByUids(uids);
        if (!unionIdByUids.responseSuccess()){
            logger.error("cmd=MpPushServiceImpl.push ERROR | find unionid fail | msg = "+ unionIdByUids.getMsg());
            return  false;
        }

        Map<String, String> data = (Map)unionIdByUids.getData();

        //根据unionid 查询formid
        List<WxTemplate> wxTemplates = this.wxTemplateRepository.selectByUnioinIds(data.values());
        for (MPPushParam param : params) {
            //循环发送
            String unionid = param.getTouser();
            param.setTouser(data.get(unionid));
            Optional<WxTemplate> first = wxTemplates.stream().filter(wxTemplate -> wxTemplate.getUnionId().equals(unionid)).findFirst();
            if (!first.isPresent()){
                logger.error("cmd=MpPushServiceImpl.push ERROR | unionid {} has no formid", unionid);
                continue;
            }
            WxTemplate template = first.get();
            String formid = param.getFormid();
            if (formid == null) {
                formid = template.getFormId();
                deleteIds.add(template.getId());
            }

            param.setTouser(template.getOpenId());
            param.setFormid(template.getFormId());
            send(param, getToken());
        }

        if (!deleteIds.isEmpty()){
            wxTemplateRepository.deleteByIdBatch(deleteIds);
        }

        return true;
    }

    @Override
    public boolean upload(MpFormUploadParam param) {
        WxTemplate template = new WxTemplate();
        //日期减一天
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime date = LocalDateTime.parse(param.getTime(), formatter);
        LocalDateTime minusDays = date.minusDays(1);

        template.setExpiredDay(minusDays.format(formatter));
        template.setFormId(param.getFormId());
        template.setOpenId(param.getOpenId());
        template.setUnionId(param.getUnionId());
        int result = wxTemplateRepository.insert(template);
        return result == 1;
    }

    private boolean send(MPPushParam param, String token) {
        logger.debug("cmd=MpPushServiceImpl.send | send minipagram message | param=" + FastJsonUtils.toJson(param));
        String url = properties.getNorifyUrl();
        Map uriParma = new HashMap();
        uriParma.put("access_token", token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        String requestJson = JsonsUtils.toJson(param);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        ResponseEntity<Map> responseEntity = new RestTemplate().postForEntity(url, entity, HashMap.class, uriParma);
        Map body = responseEntity.getBody();
        return Integer.parseInt(body.get("errcode").toString()) == 0;
    }

    private String getToken() {
        try {
            logger.debug("cmd=MpPushServiceImpl.getToken | 获取微信小程序token");
            String cacheToken = wxTokenCacheManager.getToken();
            if (cacheToken != null) {
                return cacheToken;
            }

            String url = properties.getTokenUrl();
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "client_credential");
            params.put("appid", properties.getAppid());
            params.put("secret", properties.getSecret());

            ResponseEntity<HashMap> responseEntity = new RestTemplate().getForEntity(properties.getTokenUrl(), HashMap.class, params);
            HashMap body = responseEntity.getBody();
            if (Integer.parseInt(body.get("errcode").toString()) == 0) {
                //请求成功
                String token = body.get("access_token").toString();
                long expires = Long.parseLong(body.get("expires_in").toString());

                //添加到cache中
                wxTokenCacheManager.setToken(token, (int) expires);
                return token;
            }
        } catch (RestClientException e) {
            logger.error("cmd=MpPushServiceImpl.getToken | msg= " + e.getMessage());
        }

        return null;
    }
}
