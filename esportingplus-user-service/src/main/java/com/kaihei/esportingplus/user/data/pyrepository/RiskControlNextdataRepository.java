package com.kaihei.esportingplus.user.data.pyrepository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.RiskControlNextdata;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RiskControlNextdataRepository extends CommonRepository<RiskControlNextdata> {

    /**
     * 通过uid及device_id查询风控详情
     * @param uid
     * @param deviceId
     * @return
     */
    RiskControlNextdata selectByUidAndDeviceId(@Param("uid") String uid, @Param("deviceId") String deviceId);

    /**
     * 通过uids查询deviceids
     * @param uids
     * @return
     */
    List<RiskControlNextdata> selectByUids(@Param("uids") List<String> uids);

}
