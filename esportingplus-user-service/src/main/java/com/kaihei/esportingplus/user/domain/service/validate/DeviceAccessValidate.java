package com.kaihei.esportingplus.user.domain.service.validate;


import com.kaihei.esportingplus.user.api.vo.PhoneLoginContext;

/**
 * @author xiekeqing
 * @Title: DeviceValidate
 * @Description: TODO
 * @date 2018/9/2021:53
 */
public interface DeviceAccessValidate extends Validate{
    public void validate(PhoneLoginContext phoneLoginContext);
}
