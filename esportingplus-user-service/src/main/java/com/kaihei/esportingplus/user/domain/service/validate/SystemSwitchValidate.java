package com.kaihei.esportingplus.user.domain.service.validate;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.user.config.UserProperties;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @Title: SystemSwitchValidate
 * @Description: TODO
 * @date 2018/9/2021:30
 */
@Component("systemSwitchValidate")
public class SystemSwitchValidate implements Validate {

    @Autowired
    private UserProperties userProperties;

    @Override
    public void validate() {
        if (userProperties.getUserSystemSwitch()
                == MembersAuthConstants.SYSTEM_CLOSE) {
            throw new BusinessException(BizExceptionEnum.USER_SYSTEM_MAINTAINING);
        }
    }

}
