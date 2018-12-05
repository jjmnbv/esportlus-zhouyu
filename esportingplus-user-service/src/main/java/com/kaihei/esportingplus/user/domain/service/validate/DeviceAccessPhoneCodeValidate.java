package com.kaihei.esportingplus.user.domain.service.validate;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.user.api.vo.PhoneLoginContext;
import com.kaihei.esportingplus.user.config.UserProperties;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @Title: DeviceAccessPhoneCodeValidate
 * @Description: TODO
 * @date 2018/9/2114:31
 */
@Component("deviceAccessPhoneCodeValidate")
public class DeviceAccessPhoneCodeValidate implements DeviceAccessValidate {

    @Resource(name = "pythonCacheManager")
    private CacheManager pythonCacheManager;

    @Autowired
    private UserProperties userProperties;

    @Override
    public void validate(PhoneLoginContext phoneLoginContext) {
        //苹果测试手机不校验
        if (!userProperties.getUserAppleTestPhone()
                .contains(phoneLoginContext.getParams().getPhone())) {
            String key = String
                    .format(UserRedisKey.PHONECODE, phoneLoginContext.getParams().getPhone());
            String code = pythonCacheManager.get(key, String.class);
            //验证码失效或输入的验证码错误
            if (StringUtils.isBlank(code) || !code
                    .equals(phoneLoginContext.getParams().getCode())) {
                throw new BusinessException(BizExceptionEnum.USER_PHONE_CODE_FAIL);
            }
        }
    }

    @Override
    public void validate() {

    }
}
