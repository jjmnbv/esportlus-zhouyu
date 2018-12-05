package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.esportingplus.user.api.params.PhoneRegistParam;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import com.kaihei.esportingplus.user.api.vo.*;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import com.kaihei.esportingplus.user.domain.entity.PaymentBank;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-12 17:41
 * @Description:
 */
public interface MembersUserManager {

    /**
     * 检查系统是否开启关闭模式、白名单模式
     * @param user 系统用户
     * @param forbiddenInWhiteMode
     * @return
     */
    void checkSystemSwitch(MembersUser user, boolean forbiddenInWhiteMode);

    /**
     * 调用第三方服务检验传入的第三方凭证是否合法的
     * @param params
     * @return
     */
    boolean checkLegalThirdpart(ThirdPartLoginParams params);

    /**
     * 判断该设备是否可以注册用户
     * @param deviceId 设备Id或uid
     * @return
     */
    boolean canDeviceRegist(String deviceId);

    /**
     * 判断该设备是否可以登录用户
     * @param deviceId 设备Id或uid
     * @return
     */
    boolean canDeviceLogin(String deviceId);

    /**
     * 注册创建user和auth3记录
     * @param params
     * @return
     */
    MembersUser createUserAndAuth3(ThirdPartLoginParams params);

    /**
     * 注册创建user
     * @param params
     * @return
     */
    MembersUser phoneRegistCreateUser(PhoneRegistParam params);

    /**
     * 根据用户ID更新登录设备和登录时间
     * @param userId
     * @param deviceId
     */
    void updateLoginRegistAndLoginTime(Integer userId, String deviceId);

    /**
     * 如果没有账号则创建
     * @param userId
     * @return
     */
    PaymentBank createBankIfNotExistForRegist(Integer userId);

    int updateMemberUserByUid(MembersUser user);

    /**
     * 根据uid从redis获取用户信息
     * @param uid
     * @return
     */
    UserInfoVo getUserByUid(String uid);

    /**
     * 判断用户是否在黑名单中
     * @param currentUserId     当前用户
     * @param userId            目标用户
     * @return
     */
    boolean isBlackList(int currentUserId, int userId);

    void checkUserNameForPhoneRegist(String userName);

    /**
     * 获取用户基本信息
     * @param uid
     * @return
     */
    UserVo getUser(String uid);

    /**
     * 获取用户关系信息
     * @param uid
     * @param userId
     * @return
     */
    RelationVo getRelationVo(String uid, int userId);

    /**
     * 获取暴鸡信息
     * @param uid
     * @param gameCode
     * @return
     */
    BaoJiVo getBaoJiVo(String uid, Integer gameCode);

    /**
     * 获取用户数据
     * @param uid
     * @return
     */
    UserOrderDateVo getUserOrderDate(String uid);
}
