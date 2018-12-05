package com.kaihei.esportingplus.user.domain.service.impl;

import com.google.common.base.Splitter;
import com.kaihei.esportingplus.user.api.enums.ShumeiActionType;
import com.kaihei.esportingplus.user.api.vo.UserRiskVo;
import com.kaihei.esportingplus.user.data.pyrepository.RiskControlNextdataRepository;
import com.kaihei.esportingplus.user.domain.entity.RiskControlNextdata;
import com.kaihei.esportingplus.user.domain.service.UserRiskService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserRiskServiceImpl<UserRiskNextdataRe> implements UserRiskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRiskServiceImpl.class);

    @Autowired
    private RiskControlNextdataRepository nextdataRepository;

    @Override
    public int countDeviceId(String uid, String deviceId) {
        RiskControlNextdata nextdata = new RiskControlNextdata();
        nextdata.setUid(uid);
        nextdata.setDeviceId(deviceId);
        nextdata.setAction(ShumeiActionType.ACTION_REGISTER.getCode());
        int count = nextdataRepository.selectCount(nextdata);
        return count;
    }

    @Override
    public UserRiskVo getEntityByUidAndDeviceId(String uid, String deviceId) {
        UserRiskVo riskVo = new UserRiskVo();

        RiskControlNextdata returnNextdata = nextdataRepository.selectByUidAndDeviceId(uid,deviceId);
        if (null != returnNextdata) {
            BeanUtils.copyProperties(returnNextdata, riskVo);
        }
        LOGGER.debug(" >> 查询用户风险详情信息，为riskVo = {}", riskVo);
        return riskVo;
    }

    @Override
    public List<UserRiskVo> getDeviceIdsByUids(String uids) {
        List<UserRiskVo> voList = new ArrayList<>();
        List<String> idList = Splitter.on(",").trimResults().splitToList(uids);

        List<RiskControlNextdata> nextdataList = nextdataRepository.selectByUids(idList);
        if (CollectionUtils.isNotEmpty(nextdataList)) {
            for (RiskControlNextdata data : nextdataList) {
                UserRiskVo vo = new UserRiskVo();
                vo.setId(data.getId());
                vo.setUid(data.getUid());
                vo.setDeviceId(data.getDeviceId());
                voList.add(vo);
            }
        }

        return voList;
    }
}
