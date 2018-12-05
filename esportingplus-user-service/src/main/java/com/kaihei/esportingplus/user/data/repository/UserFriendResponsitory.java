package com.kaihei.esportingplus.user.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.UserFriend;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

/**
 * @author liuyang
 * @Description
 * @Date 2018/11/27 14:25
 **/
public interface UserFriendResponsitory extends CommonRepository<UserFriend>, InsertListMapper<UserFriend> {

    List<UserFriend> friends(String uid);

    Long friendCount(String uid);
}
