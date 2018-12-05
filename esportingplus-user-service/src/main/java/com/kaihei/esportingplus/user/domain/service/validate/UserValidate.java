package com.kaihei.esportingplus.user.domain.service.validate;

import com.kaihei.esportingplus.user.domain.entity.MembersUser;

/**
 * @author xiekeqing
 * @Title: UserValidate
 * @Description: TODO
 * @date 2018/9/2021:36
 */
public interface UserValidate extends Validate{

    public void validate(MembersUser user);

}
