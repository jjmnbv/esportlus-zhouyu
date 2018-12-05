package com.kaihei.esportingplus.user.domain.service.validate;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.user.config.UserProperties;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.data.manager.MembersUserManager;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import com.kaihei.esportingplus.user.domain.entity.MembersUserWhiteList;
import com.kaihei.esportingplus.user.domain.service.MembersUserWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @Title: UserLoginValidate
 * @Description: TODO
 * @date 2018/9/2021:35
 */
@Component("userLoginValidate")
public class UserLoginValidate implements UserValidate {

    @Autowired
    private UserProperties userProperties;

    @Autowired
    private MembersUserWhitelistService membersUserWhitelistService;

    @Autowired
    private MembersUserManager membersUserManager;

    @Override
    public void validate(MembersUser user) {
        validateIsActive(user);
    }

    @Override
    public void validate() {
    }


    private void validateIsActive(MembersUser user) {
        //用户状态为Null或者非正常状态
        if (user.getIsActive() == null || user.getIsActive().intValue() != 1) {
            throw new BusinessException(BizExceptionEnum.USER_SYSTEM_MAINTAINING);
        }
    }


}
