package com.kaihei.esportingplus.user.data.pyrepository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MembersUserRepository extends CommonRepository<MembersUser> {


    int insertMemberUser(MembersUser membersUser);

    MembersUser selectByUserId(Integer userId);

    MembersUser selectByPhone(String phone);

    List<String> selectAllHavingUsedChickenIds(@Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 根据uid查找
     */
    Integer selectUserIdByUid(String uid);

    List<MembersUser> selectUserInfoIdByUid(String uid);

    MembersUser selectByAuth3Leftjoin(@Param("unionid") String unionid, @Param("platform") String platform);

    /**
     * 根据userId更新电话号码
     * */
    int updateUserPhoneAndSexByUserId(@Param("phone") String phone,@Param("sex") int sex,@Param("userId") Integer userId);

    /**
     * 根据username查询userId
     *
     * @param userName 用户名
     * @return 返回查询到的userId
     * */
    Integer getUserIdByUserName(String userName);


    /**
     * 更新User对象
     * @param user
     * @return
     */
    int updateMemberUser(MembersUser user);

    /**
     * 根据userId更新User对象信息
     * @param user
     * @return
     */
    int updateMemberUserById(MembersUser user);

    /**
     * 查询最大用户ID
     *
     * @return 已自增的最大用户ID
     */
    int selectMaxUserId();

}