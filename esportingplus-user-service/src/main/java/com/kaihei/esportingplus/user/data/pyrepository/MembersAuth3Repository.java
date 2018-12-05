package com.kaihei.esportingplus.user.data.pyrepository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.api.vo.BindListVo;
import com.kaihei.esportingplus.user.api.vo.UserBindWxUnionIdVo;
import com.kaihei.esportingplus.user.domain.entity.MembersAuth3;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MembersAuth3Repository extends CommonRepository<MembersAuth3> {
    /**
     * 根据userId以及uid获取用户绑定列表信息
     *
     * @param userId
     * @param uid
     * @return BindListVo
     */
    BindListVo getBindList(@Param("userId") Integer userId, @Param("uid") String uid);

    /**
     * 根据userId更新绑定的电话号码
     *
     * @param phoneNumber 更换的电话号码
     * @param userId
     * @return
     */
    int updatePhoneNumByUserId(@Param("phoneNumber") String phoneNumber, @Param("userId") Integer userId);

    /**
     * 根据userId获取绑定的旧的电话号码
     *
     * @param userId
     * @return 旧的电话号码
     */
    String getOldPhoneNumberByUserId(@Param("userId") Integer userId);

    /**
     * 根据uid获取该用户绑定的微信unionid
     *
     * @param uids
     * @return List<UserBindWxUnionIdVo>
     */
    List<UserBindWxUnionIdVo> getWxUnionIdByUid(@Param("uids") List<String> uids);
}