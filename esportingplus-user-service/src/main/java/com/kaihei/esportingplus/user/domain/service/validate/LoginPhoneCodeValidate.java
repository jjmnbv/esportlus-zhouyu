package com.kaihei.esportingplus.user.domain.service.validate;

import com.kaihei.esportingplus.user.api.params.UserPhoneLoginParams;
import com.kaihei.esportingplus.user.api.vo.PhoneLoginContext;
import org.springframework.stereotype.Service;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-29 16:04
 * @Description:
 */
@Service
public class LoginPhoneCodeValidate extends DeviceAccessPhoneCodeValidate {

    public void checkPhoneCode(String phone, String code) {
        PhoneLoginContext c = new PhoneLoginContext();
        UserPhoneLoginParams p = new UserPhoneLoginParams();
        p.setPhone(phone);
        p.setCode(code);
        c.setParams(p);
        super.validate(c);
    }
}
