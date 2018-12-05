package com.kaihei.esportingplus.user.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.api.params.UserGameQueryBaseParams;
import com.kaihei.esportingplus.user.api.params.UserGameRoleAcrossDataQueryParams;
import com.kaihei.esportingplus.user.domain.entity.UserGameRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface UserGameRoleRepository extends CommonRepository<UserGameRole> {

    List<UserGameRole> selectByUserIdAndGameId(@Param("uid") String uid, @Param("gameCode") Integer gameCode);

    List<UserGameRole> selectByUserIdAndGameIdWithCondition(@Param("uid") String uid, @Param("gameCode") Integer gameCode, @Param("conditon") UserGameQueryBaseParams params);

    List<UserGameRole> selectByUserIAndGameCodeAndSmallCodeList(@Param("params")
                                                                        UserGameRoleAcrossDataQueryParams params);
}