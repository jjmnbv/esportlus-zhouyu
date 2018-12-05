package com.kaihei.esportingplus.user.domain.service.validate;

import com.kaihei.esportingplus.user.api.vo.PhoneLoginContext;
import com.kaihei.esportingplus.user.data.manager.MembersUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @Title: DeviceLoginValidate
 * @Description: TODO
 * @date 2018/9/2021:59
 */
@Component("deviceAccessTimesValidate")
public class DeviceAccessTimesValidate implements DeviceAccessValidate {

    @Autowired
    private MembersUserManager membersUserManager;

    @Override
    public void validate(PhoneLoginContext phoneLoginContext) {
        //设备登录次数校验
//        validateDeviceLogin(phoneLoginContext);
        //数美登录次数校验
        validateSM(phoneLoginContext);
    }

    private void validateDeviceLogin(PhoneLoginContext phoneLoginContext) {
        membersUserManager.canDeviceLogin(phoneLoginContext.getParams().getDeviceId());
    }

    private void validateSM(PhoneLoginContext phoneLoginContext) {
    }

    @Override
    public void validate() {
    }

}
