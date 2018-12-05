package com.kaihei.esportingplus.user.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.file.FileUploadService;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.api.params.PhoneBindParams;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import com.kaihei.esportingplus.user.api.vo.UserBindWxUnionIdVo;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.data.manager.MembersAuth3CacheManager;
import com.kaihei.esportingplus.user.data.manager.MembersAuth3Manager;
import com.kaihei.esportingplus.user.data.manager.MembersPhoneCacheManager;
import com.kaihei.esportingplus.user.data.manager.MembersUserManager;
import com.kaihei.esportingplus.user.data.pyrepository.MembersAuth3Repository;
import com.kaihei.esportingplus.user.domain.entity.MembersAuth3;
import com.kaihei.esportingplus.user.domain.service.MembersAuth3Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiekeqing
 * @Title: MembersAuth3ServiceImpl
 * @Description: 第三方绑定服务实现类
 * @date 2018/9/1120:39
 */
@Service
public class MembersAuth3ServiceImpl implements MembersAuth3Service {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MembersAuth3CacheManager membersAuth3CacheManager;

    @Autowired
    private MembersPhoneCacheManager membersPhoneCacheManager;

    @Autowired
    private MembersAuth3Manager membersAuth3Manager;

    @Autowired
    private MembersUserManager membersUserManager;

    @Autowired
    private MembersAuth3Repository membersAuth3Repository;


    @Autowired
    private FileUploadService qiNiuFileUploadService;


//    @Autowired
//    private CommonProducer commonProducer;


    @Override
    public Integer getByIdentifierOrUnionId(String platform, String identifier, String unionId) {
        Integer userId = null;
        String openid = null;
        //微信和其他第三方绑定所使用的openid不一致
        if (MembersAuthConstants.PLATFORM_WX.equalsIgnoreCase(platform)) {
            openid = unionId;
        } else {
            openid = identifier;
        }
        userId = membersAuth3CacheManager.getBindUserIdOpenid(openid);

        //缓存中无数据则再从mysql查询，并将查询出的用户id缓存到redis
        if (userId == null) {
            MembersAuth3 membersAuth3 = getMembersAuth3ByIdentifierOrUnionId(platform, identifier,
                    unionId);
            if (membersAuth3 != null) {
                userId = membersAuth3.getUserId();
                membersAuth3CacheManager.setBindUserIdOpenid(openid, userId);
            }
        }

        return userId;
    }


    private MembersAuth3 getMembersAuth3ByIdentifierOrUnionId(String platform, String identifier,
                                                              String unionId) {
        MembersAuth3 membersAuth3 = new MembersAuth3();
        membersAuth3.setPlatform(platform);

        //微信登录则使用unionid查询，其他非微信登录则使用identifier查询
        if (MembersAuthConstants.PLATFORM_WX.equalsIgnoreCase(platform)) {
            membersAuth3.setUnionid(unionId);
        } else {
            membersAuth3.setIdentifier(identifier);
        }
        //TODO 需调整为按倒序查询
        //TODO identifier未传时会将表中identifier列为空的数据查询出来
        List<MembersAuth3> list = membersAuth3Repository.select(membersAuth3);

        //TODO 可与python同事讨论该逻辑是否还需保留
        //根据unionid查询无数据时，再次使用identifier查询
        if (CollectionUtils.isEmpty(list) && MembersAuthConstants.PLATFORM_WX
                .equalsIgnoreCase(platform)) {
            membersAuth3.setUnionid(null);
            membersAuth3.setIdentifier(identifier);
            list = membersAuth3Repository.select(membersAuth3);
        }

        return CollectionUtils.isEmpty(list) ? null : list.get(0);

    }

    @Override
    public void bindAuth3(ThirdPartLoginParams params) {
        logger.info("cmd = Auth3BindServiceImpl bindAuth3 begin | 用户:{}正在绑定:{}第三方账户 ",
                UserSessionContext.getUser().getUid(), params.getPlatform());
        //查询该第三方账号是否已经绑定过
        Integer userId = getByIdentifierOrUnionId(params.getPlatform(), params.getIdentifier(),
                params.getUnionid());
        if (userId != null) {
            throw new BusinessException(BizExceptionEnum.USER_AUTH3_ACCOUNT_IS_BIND);
        }
        //校验第三方用户合法性
        if (!membersUserManager.checkLegalThirdpart(params)) {
            throw new BusinessException(BizExceptionEnum.USER_SYSTEM_THIRDPART_VALID_FAIL);
        }
        //从用户信息上下文获取user_id
        userId = UserSessionContext.getUser().getId();
        if (userId == null) {
            throw new BusinessException(BizExceptionEnum.USER_AUTH3_USERNAME_IS_INVALID);
        }
        membersAuth3Manager.createBindAuth3(params, userId);
//        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
//                MembersAuthConstants.USER_MQ_AUTH3_BIND_TOPIC_TAG_SATRACK_KEY,
//                Utils.getMessageKey("track", UserSessionContext.getUser().getUid()),
//                new Auth3BindSATrackMessageVo(UserSessionContext.getUser().getUid(), "AccountBinding", "1", params.getPlatform().equals("WX") ? "1" : "2"));
    }

    @Override
    public void bindPhone(PhoneBindParams params) {
        logger.info("cmd = Auth3BindServiceImpl bindPhone begin | 用户:{}正在绑定手机号:{} ",
                UserSessionContext.getUser().getUid(), params.getPhone());
        //校验手机号码以及验证码
        if (membersAuth3Manager.validatePhoneNumberAndCode(params.getPhone(), params.getCode())) {
            if (membersAuth3Manager.validatePhoneIsBind(params.getPhone())) {
                throw new BusinessException(BizExceptionEnum.USER_AUTH3_PHONE_IS_BIND);
            }
            //保存手机绑定信息到members_auth3，同步更新members_user中对应用户的手机号
            membersAuth3Manager.createAuth3AndUpdatePhone(params);
//            commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
//                    MembersAuthConstants.USER_MQ_AUTH3_BIND_TOPIC_TAG_SATRACK_KEY,
//                    Utils.getMessageKey("track", UserSessionContext.getUser().getUid()),
//                    new Auth3BindSATrackMessageVo(UserSessionContext.getUser().getUid(), "AccountBinding", "1", "3"));
        }
    }

    @Override
    public Map<String, String> bindList() {
        logger.info("cmd = Auth3BindServiceImpl bindList begin | 用户:{}获取绑定列表开始",
                UserSessionContext.getUser().getUid());
        Map<String, String> result = membersAuth3Manager.getBindList(UserSessionContext.getUser().getId(), UserSessionContext.getUser().getUid());
        return result;
    }

    @Override
    public boolean verifyOldPhone(PhoneBindParams params) {
        logger.info("cmd = Auth3BindServiceImpl verifyOldPhone begin | 用户:{}正在验证手机号:{},验证码:{}",
                UserSessionContext.getUser().getUid(), params.getPhone(), params.getCode());
        return membersAuth3Manager.validatePhoneNumberAndCode(params.getPhone(), params.getCode());
    }

    @Override
    public void unBindAuth3(String platform) {
        logger.info("cmd = Auth3BindServiceImpl unBindAuth3 begin | 用户:{}正在解绑{}第三方账号",
                UserSessionContext.getUser().getUid(), platform);
        Integer userId = UserSessionContext.getUser().getId();
        String uid = UserSessionContext.getUser().getUid();
        membersAuth3Manager.checkAndUnBindAuth3(userId, platform, uid);
//        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
//                MembersAuthConstants.USER_MQ_AUTH3_BIND_TOPIC_TAG_SATRACK_KEY,
//                Utils.getMessageKey("track", UserSessionContext.getUser().getUid()),
//                new Auth3BindSATrackMessageVo(UserSessionContext.getUser().getUid(), "AccountBinding", "2", platform.equals("WX") ? "1" : "2"));
    }

    @Override
    public void updatePhone(PhoneBindParams params) {
        logger.info("cmd = Auth3BindServiceImpl unBindAuth3 begin | 用户:{}正在更新手机号为:{},验证码:{}",
                UserSessionContext.getUser().getUid(), params.getPhone(), params.getCode());
        String phoneNumber = params.getPhone();
        String code = params.getCode();
        if (code.length() != 4) {
            throw new BusinessException(BizExceptionEnum.USER_AUTH3_UPDATEPHONE_CODE_LENGTH);
        }
        //校验手机号是否合法、验证码是否正确
        if (membersAuth3Manager.validatePhoneNumberAndCode(phoneNumber, code)) {
            //校验手机号是否已经绑定过暴鸡账号
            if (!membersAuth3Manager.validatePhoneIsBind(phoneNumber)) {
                throw new BusinessException(BizExceptionEnum.USER_AUTH3_PHONE_IS_BIND);
            }
            //更新redis、members_user、members_auth3
            Integer userId = UserSessionContext.getUser().getId();
            membersAuth3Manager.updatePhone(userId, phoneNumber);
//            commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
//                    MembersAuthConstants.USER_MQ_AUTH3_BIND_TOPIC_TAG_SATRACK_KEY,
//                    Utils.getMessageKey("track", UserSessionContext.getUser().getUid()),
//                    new Auth3BindSATrackMessageVo(UserSessionContext.getUser().getUid(), "AccountBinding", "3", "3"));
        }
    }

    @Override
    public Map getUnionIdByUids(List<String> uids) {
        Map<String, String> map = new HashMap<>(uids.size());
        if (uids.size() > 0) {
            List<UserBindWxUnionIdVo> list = membersAuth3Repository.getWxUnionIdByUid(uids);
            map = list.stream().collect(Collectors.toMap(UserBindWxUnionIdVo::getUid, UserBindWxUnionIdVo::getUnionid));
        }
        return map;
    }

    /**
     * 创建微信第三方
     *
     * @param userId
     * @param unionId
     */
    @Override
    public void createAuth3Wx(Integer userId, String unionId) {
        MembersAuth3 auth3 = new MembersAuth3();
        auth3.setUserId(userId);
        auth3.setUnionid(unionId);
        auth3.setPlatform(MembersAuthConstants.PLATFORM_WX);
        auth3.setPackageName(MembersAuthConstants.PACKAGE_NAME_MP);
        auth3.setIdentifier("");
        auth3.setCredential("");
        int i = membersAuth3Repository.insertSelective(auth3);
        if (i < 0) {
            logger.error("cmd=MembersAuth3Service.createAuth3Wx | msg={} | req={}",
                    "创建微信auth3失败！",
                    JSON.toJSON(auth3));
        }
    }
}
