package com.kaihei.esportingplus.user.data.manager.thirdpart;

import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-13 15:19
 * @Description:
 */
public interface ThirdpartValidator {

    String PLATFORM_WX_CHECK_LEGAL_ACCESSTOKEN = "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s",
            PLATFORM_QQ_CHECK_LEGAL_ACCESSTOKEN = "https://graph.qq.com/oauth2.0/me?access_token=%s&unionid=1";

    boolean isValid(ThirdPartLoginParams params, RestTemplate template);
}
