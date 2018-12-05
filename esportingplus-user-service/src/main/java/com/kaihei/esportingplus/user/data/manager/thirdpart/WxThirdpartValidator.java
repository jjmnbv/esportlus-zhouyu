package com.kaihei.esportingplus.user.data.manager.thirdpart;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-13 15:28
 * @Description:
 */
public class WxThirdpartValidator implements ThirdpartValidator {

    private final Logger logger = LoggerFactory.getLogger(WxThirdpartValidator.class);

    @Override
    public boolean isValid(ThirdPartLoginParams params, RestTemplate template) {
        ResponseEntity<String> responseEntity = template.getForEntity(
                String.format(WxThirdpartValidator.PLATFORM_WX_CHECK_LEGAL_ACCESSTOKEN,
                        params.getCredential(), params.getIdentifier()), String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                String result = responseEntity.getBody();
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject.getString("errcode").equals("0")) {
                    return true;
                }
            } catch (Exception e) {
                logger.error("WxThirdpartValidator error:{},access_token={},openid={}",
                        e.getMessage(), params.getCredential(), params.getIdentifier());
                return false;
            }
        }
        logger.error("cmd=WxThirdpartValidator return false | responseStatusCode:{} | responseText: {}",responseEntity.getStatusCode(),responseEntity.getBody());
        return false;
    }
}
