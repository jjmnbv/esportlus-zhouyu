package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.user.api.params.RegistLoginBaseParam;
import com.kaihei.esportingplus.user.api.vo.*;
import com.kaihei.esportingplus.user.domain.entity.MembersRegisteredDevice;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;

/**
 * @Description: 终端用户服务接口
 * @Author: xiekeqing
 * @Date: 2018年9月11日
 */
public interface MembersUserService {

    /**
     * 新增注册设备记录
     */
    MembersRegisteredDevice saveDevice(RegistLoginBaseParam params, Integer userId,
            String rToken);

    /**
     * 更新RegisterDevice融云token
     * @param rToken
     * @param userId
     * @return
     */
    boolean updateDeviceRtoken(String rToken, Integer userId);

    MembersUser getUserByAuth3(String unionid, String platform);

    MembersRegisteredDevice getOrCreateDevice(MembersUser user);

    /**
     * 根据用户id从缓存查询用户
     *
     * @return MembersUser
     */
    public MembersUser getMembersUserById(Integer id);

    /**
     * 随机生成uid
     *
     * @return String
     */
    public String getRandomUid();


    /**
     * 根据uid获取对应的userId
     *
     * @param uid 用户uid
     * @return Integer 用户ID
     */
    public Integer getUserIdByUid(String uid);

    /**
     * 修改用户昵称
     * @param uid
     * @param username
     * @return
     */
    public int updateUsername(String uid,String username);

    /**
     * 修改地区
     * @param uid
     * @param region
     * @return
     */
    public int updateRegion(String uid, String region);

    /**
     * 修改个人说明
     * @param uid
     * @param desc
     * @return
     */
    public int updateDesc(String uid, String desc);

    /**
     * 修改生日
     * @param uid
     * @param birthdayData
     * @return
     */
    public int updateBirthDay(String uid, String birthdayData);

    /**
     * 修改圈子状态
     * @param uid
     * @param showgroup
     * @return
     */
    public int updateShowGroup(String uid, Boolean showgroup);

    /**
     * 获取IM信息
     * @param imUid
     * @return
     */
    public ImmemberVo getImMenber(String imUid);

    /**
     * 根据用户uid查询用户信息
     *
     * @param uid 用户uid
     * @return MembersUser
     */
    public MembersUser getMembersUserByUid(String uid);

    /**
     * 通过手机号码查询用户
     * @param phone
     * @return
     */
    MembersUser getUserByPhone(String phone);

    /**
     * 通过userId判断有没有绑定wx
     * @param userId
     * @param platform
     * @return
     */
    boolean userHasBindAuth(Integer userId, String platform);

    /** 修改照片
     * @param url
     * @param action
     * @param id
     * @return
     */
    public PicturesVo updatePicture(Integer userId, String url, String action, Integer id);

    /**
     * 修改头像
     * @param userId
     * @param avatar
     * @return
     */
    Integer updateAvatar(int userId, String avatar);

    /**
     * 小程序创建用户
     * @param info
     * @param phone
     * @return
     */
    MembersUser mpCreateUser(WxUserInfo info, String phone);

    /**
     * 审核照片
     * @param message
     */
    public void verifyPicture(UserUrlMessageVo message);

    /**
     * uid查用户信息
     * @param uid
     * @return
     */
    public UserInfoVo getUserInfo(String uid);

    /**
     * 获取用户的个人信息卡
     * @param uid
     * @param game
     * @return
     */
    public UserCardVo getUserCard(String uid, Integer game);

    /**
     * 获取用户基本信息
     * @param uid
     * @return
     */
    public UserVo getUser(String uid);

    /**
     * 获取用户关系信息
     * @param uid
     * @param userId
     * @return
     */
    public RelationVo getRelationVo(String uid,int userId);

    /**
     * 获取暴鸡信息
     * @param uid
     * @param gameCode
     * @return
     */
    public BaoJiVo getBaoJiVo(String uid,Integer gameCode);
}
