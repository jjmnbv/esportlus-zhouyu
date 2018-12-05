package com.kaihei.esportingplus.user.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.UserFans;
import com.kaihei.esportingplus.user.domain.entity.UserFollow;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

/**
 * @author liuyang
 * @Description
 * @Date 2018/11/27 14:25
 **/
public interface UserFansResponsitory extends CommonRepository<UserFans>, InsertListMapper<UserFans> {

    List<UserFans> fans(String uid);

    List<UserFans> fansIn(@Param("uid") String uid , @Param("fans") List<String> fans);

}
