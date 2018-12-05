package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.user.api.vo.UserRiskVo;

import java.util.List;

/**
 * 用户风控
 * @author chenzhenjun
 */
public interface UserRiskService {

    public int countDeviceId(String uid, String deviceId);

    public UserRiskVo getEntityByUidAndDeviceId(String uid, String deviceId);

    public List<UserRiskVo> getDeviceIdsByUids(String uids);

}
