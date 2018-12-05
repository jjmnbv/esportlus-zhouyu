package com.kaihei.esportingplus.riskrating.service;

import com.kaihei.esportingplus.riskrating.api.params.ImMachineUpdateParams;
import com.kaihei.esportingplus.riskrating.api.vo.*;

/**
 * IM防骚扰及虚拟机判断Service
 * @author chenzhenjun
 */
public interface ImMachineService {

    /**
     * 获取配置
     * @return
     */
    ImMachineConfigVo getImMachineConfig();

    /**
     * 修改配置
     * @param configVo
     */
    void updateImMachineConfig(ImMachineConfigVo configVo);

    /**
     * 数美ID黑名单列表
     * @param deviceId
     * @param page
     * @param size
     * @return
     */
    PageInfo<ImMachineListVo> getDeviceBlackList(String deviceId, String page, String size);

    /**
     * 添加数美ID至黑名单
     * @param deviceId
     */
    void insertDeviceBlack(String deviceId);

    /**
     * 数美ID移出黑名单
     * @param id
     */
    void deleteDeviceBlack(long id);

    /**
     * 查询用户数美ID绑定关系
     * @param type
     * @param column
     * @param page
     * @param size
     * @return
     */
    PageInfo<ImMachineListVo> getUserDeviceBindList(String type, String column, String page, String size);

    /**
     * 用户数美ID关系解绑
     * @param id
     */
    void unbindUserDeviceRelation(long id);

    /**
     * 查询IM消息骚扰用户黑名单列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    PageInfo<ImMachineListVo> getUserImBlackList(String userId, String page, String size);

    /**
     * 将用户从IM消息骚扰黑名单移出
     * @param id
     */
    void deleteUserImBlack(long id);

    /**
     * 风控-注册检测
     * @param imMachineUpdateParams
     * @return
     */
    public FreeTeamResponse registerCheck(ImMachineUpdateParams imMachineUpdateParams);

    /**
     * 风控-登陆检测
     * @param imMachineUpdateParams
     * @return
     */
    public FreeTeamResponse loginCheck(ImMachineUpdateParams imMachineUpdateParams);

    /**
     * JAVA调用-生成uid前校验注册
     * @param deviceId
     * @param phone
     * @param version
     * @return
     */
    public FreeTeamResponse registerCheckBeforeGenerateUserId(String deviceId, String phone, String version);

    UserDeviceBehaviourRecordVo getBehaviourRecordInfo(String userId);

    void updateBehaviourRecord(UserDeviceBehaviourRecordVo vo);

}
