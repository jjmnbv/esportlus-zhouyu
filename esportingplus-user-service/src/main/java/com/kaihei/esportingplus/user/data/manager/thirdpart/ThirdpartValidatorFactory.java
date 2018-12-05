package com.kaihei.esportingplus.user.data.manager.thirdpart;

import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-13 15:17
 * @Description:
 */
public class ThirdpartValidatorFactory {

    public static ThirdpartValidator determineValidator(String platform) {
        if (platform != null) {
            if (platform.equals(MembersAuthConstants.PLATFORM_QQ)) {
                return new QQThirdpartValidator();
            } else if (platform.equals(MembersAuthConstants.PLATFORM_WX)) {
                return new WxThirdpartValidator();
            }
        }

        return nullValidator();
    }

    private static ThirdpartValidator nullValidator() {
        return new ThirdpartValidator() {
            @Override
            public boolean isValid(ThirdPartLoginParams params, RestTemplate template) {
                return false;
            }
        };
    }
}
