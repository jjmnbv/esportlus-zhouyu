package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.user.api.params.UserGameRaidCodeQueryParams;
import com.kaihei.esportingplus.user.api.params.UserSingeRoleQueryParams;
import com.kaihei.esportingplus.user.api.vo.CertRoleWithJoinRaidVo;
import com.kaihei.esportingplus.user.api.vo.UserGameAboardVo;
import com.kaihei.esportingplus.user.api.vo.UserGameBaseRoleInfoVo;
import com.kaihei.esportingplus.user.api.vo.UserGameDetailRoleInfoVo;
import com.kaihei.esportingplus.user.api.params.UserGameQueryBaseParams;
import com.kaihei.esportingplus.user.api.params.UserGameRoleAcrossQueryParams;
import com.kaihei.esportingplus.user.api.vo.UserGameRoleSimpleVo;
import com.kaihei.esportingplus.user.api.vo.UserSingleRoleDetailInfoVo;
import java.util.List;

public interface UserGameService {

    /**
     * 获取用户基本信息，不包含认证信息
     *
     * @param userId 用户id
     * @param gameCode 游戏代码
     */
    List<UserGameBaseRoleInfoVo> getUserAllBaseRoles(String userId, Integer gameCode);

    /**
     * 获取用户游戏角色，如果有认证信息则附带认证信息
     *
     * @param userId 用户id
     * @param gameCode 游戏代码
     * @param params 可选参数
     */
    List<UserGameDetailRoleInfoVo> getAllUserGameRoles(String userId, Integer gameCode,
            UserGameQueryBaseParams params);

    /**
     * 获取用户游戏角色认证，如果没有认证则不输出
     *
     * @param userId 用户id
     * @param gameCode 游戏代码
     * @param params 可选参数
     */
    List<UserGameDetailRoleInfoVo> getUserCredentialsRoles(String userId, Integer gameCode,
            UserGameRaidCodeQueryParams params);

    /**
     * @param userId 用户id
     * @param vo 创建的角色对象
     */
    void addUserGameRole(String userId, UserGameRoleSimpleVo vo);

    /**
     * @param userId 用户id
     * @param roleId 需要删除的角色Id
     */
    void deleteUserGameRole(String userId, Long roleId);

    /**
     * 获取上车角色列表
     */
    List<UserGameAboardVo> getAboardGameRole(UserGameRoleAcrossQueryParams params);

    /**
     * 获取对应的身份角色信息
     */
    public UserSingleRoleDetailInfoVo getUserIdentityRoleInfo(UserSingeRoleQueryParams params);

    /**
     * 暴鸡创建车队时获取角色认证列表及其对应副本
     * @param userId
     * @param gameCode
     * @return
     */
    public List<CertRoleWithJoinRaidVo> getUserCredentialsAndRaidRoles(String userId,
            Integer gameCode);


}
