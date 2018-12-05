package com.kaihei.esportingplus.user.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.UserFollow;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

/**
 * @author liuyang
 * @Description
 * @Date 2018/11/27 14:25
 **/
public interface UserFollowResponsitory extends CommonRepository<UserFollow>, InsertListMapper<UserFollow> {

    List<UserFollow> friends(String uid);

    int deleteFollows(String uid, List<String> followIds);

}
