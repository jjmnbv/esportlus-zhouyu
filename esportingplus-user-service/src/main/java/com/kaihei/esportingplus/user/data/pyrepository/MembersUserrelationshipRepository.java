package com.kaihei.esportingplus.user.data.pyrepository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersUserrelationship;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MembersUserrelationshipRepository extends CommonRepository<MembersUserrelationship> {

    List<MembersUserrelationship> selectUserRelationship(@Param("userId") int userId, @Param("followId") int followId);

    /**
     * 关注数量
     * @param userId
     * @return
     */
    int selectFollowCount(int userId);

    /**
     * 粉丝数量
     * @param userId
     * @return
     */
    int selectFansCount(int userId);

    /**
     * 好友数量
     * @param userId
     * @return
     */
    int selectFriendCount(int userId);
}