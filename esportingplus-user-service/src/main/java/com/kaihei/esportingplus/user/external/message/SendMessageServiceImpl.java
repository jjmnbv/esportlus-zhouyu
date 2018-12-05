package com.kaihei.esportingplus.user.external.message;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.user.api.vo.SendFreeTeamMessageVo;
import com.kaihei.esportingplus.user.api.vo.SendMessageVo;
import com.kaihei.esportingplus.user.config.UserProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaozhenlin
 * @description: 发送消息实现类
 * @date: 2018/10/9 11:37
 */
@Service
public class SendMessageServiceImpl implements SendMessageService{
    private static final Logger logger = LoggerFactory.getLogger(SendMessageServiceImpl.class);
    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;
    @Autowired
    private UserProperties userProperties;

    @Override
    public void sendMessage(SendMessageVo messageVo) {
        //判断参数
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,messageVo.getReciever(),messageVo.getTemplateId(),messageVo.getData());
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("sender", "bjdj_system");//系统消息，发送者为系统
        requestParams.put("reciever", messageVo.getReciever());
        requestParams.put("template_id", messageVo.getTemplateId());
        requestParams.put("data", messageVo.getData());
        send(requestParams);
    }

    @Override
    public void SendFreeTeamMessage(SendFreeTeamMessageVo messageVo) {
        //判断参数
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,messageVo.getReciever(),messageVo.getData());
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("sender", "bjdj_system");//系统消息，发送者为系统
        requestParams.put("reciever", messageVo.getReciever());
        requestParams.put("template_id", "54");//模板Id固定
        requestParams.put("data", messageVo.getData());

        //开始发送
        send(requestParams);
    }

    /**
     * 调用python接口发送消息
     * @param requestParams
     */
    private void send(Map<String, Object> requestParams) {
        String json = JacksonUtils.toJson(requestParams);
        logger.info("cmd=SendMessageServiceImpl.send | msg={} | req={}",
                "开发发送IM", json);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(JacksonUtils.toJson(requestParams),
                headers);

        int retryTime = 0;
        while (retryTime < 3) {//发送消息,失败发送三次
            try {
                String domain = userProperties.getPythonDomain();
                if (domain != null) {
                    if (!domain.endsWith("/")) {
                        domain = domain + "/";
                    }
                }
                String url = domain + userProperties.getUserSendMessage();
                ResponsePacket result = restTemplateExtrnal.postForObject(url, formEntity, ResponsePacket.class);
                System.out.println(result);
                if(result.responseSuccess()){
                    logger.info("cmd=SendMessageServiceImpl.send | msg={} | req={}",
                            "sendMessage success", json);
                    break;
                } else {
                    retryTime++;
                    logger.info("cmd=SendMessageServiceImpl.send | msg={} | 重发次数={} | req={}",
                            "超时重发IM", retryTime, json);
                }
            } catch (Exception e) {
                logger.error("cmd=SendMessageServiceImpl.send | msg={} | req={}",
                        "sendMessage fail", json);
                retryTime++;
            }
        }
    }
}
