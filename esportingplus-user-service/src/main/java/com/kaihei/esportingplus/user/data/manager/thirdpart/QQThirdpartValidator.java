package com.kaihei.esportingplus.user.data.manager.thirdpart;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-13 15:25
 * @Description:
 */
public class QQThirdpartValidator implements ThirdpartValidator {

    private final Logger logger = LoggerFactory.getLogger(QQThirdpartValidator.class);

    @Override
    public boolean isValid(ThirdPartLoginParams params, RestTemplate template) {
        ResponseEntity<String> responseEntity = template.getForEntity(
                String.format(QQThirdpartValidator.PLATFORM_QQ_CHECK_LEGAL_ACCESSTOKEN,
                        params.getCredential()), String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                String result = responseEntity.getBody();
                //因为qq验证接口返回的不是json格式的字符串,需要对字符串进行截取,
                // 类似callback( {"error":100016,"error_description":"access token check failed"} )
                result = result.substring(result.lastIndexOf("(") + 1, result.lastIndexOf(")"));
                JSONObject jsonObject = JSONObject.parseObject(result);
                String openId = jsonObject.getString("openid") == null ? ""
                        : jsonObject.getString("openid");
                //首先比较qq返回的openid与传过来的identifier是否一致，如果一致，则将qq返回的unionid赋值给param中的unionid
                if (openId.equals(params.getIdentifier())) {
                    params.setUnionid(jsonObject.getString("unionid"));
                    return true;
                }
            } catch (Exception e) {
                logger.error("QQThirdpartValidator error:{},access_token={}", e.getMessage(),
                        params.getCredential());
                return false;
            }
        }
        logger.error("cmd=QQThirdpartValidator return false | responseStatusCode:{} | responseText: {}",responseEntity.getStatusCode(),responseEntity.getBody());
        return false;
    }
}
