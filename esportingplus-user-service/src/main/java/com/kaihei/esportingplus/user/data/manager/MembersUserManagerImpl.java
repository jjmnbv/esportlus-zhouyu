package com.kaihei.esportingplus.user.data.manager;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.config.RonyunUserIdGenerator;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.api.params.PhoneRegistParam;
import com.kaihei.esportingplus.user.api.params.RegistLoginBaseParam;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;
import com.kaihei.esportingplus.user.api.vo.*;
import com.kaihei.esportingplus.user.config.UserProperties;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import com.kaihei.esportingplus.user.data.manager.thirdpart.ThirdpartValidatorFactory;
import com.kaihei.esportingplus.user.data.pyrepository.BaoJiBnVerifypicRepository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersAuth3Repository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersRegisteredDeviceRepository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersUserRepository;
import com.kaihei.esportingplus.user.data.pyrepository.PaymentAccountStatementRepository;
import com.kaihei.esportingplus.user.data.pyrepository.PaymentBankRepository;
import com.kaihei.esportingplus.user.domain.entity.BaoJiBnVerifypic;
import com.kaihei.esportingplus.user.domain.entity.BaojiBaoJiTag;
import com.kaihei.esportingplus.user.domain.entity.MembersAuth3;
import com.kaihei.esportingplus.user.domain.entity.MembersRegisteredDevice;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import com.kaihei.esportingplus.user.domain.entity.PaymentAccountStatement;
import com.kaihei.esportingplus.user.domain.entity.PaymentBank;
import com.kaihei.esportingplus.user.domain.service.BaojiBaojiService;
import com.kaihei.esportingplus.user.domain.service.MembersOrderCountService;
import com.kaihei.esportingplus.user.domain.service.MembersUserWhitelistService;
import com.kaihei.esportingplus.user.domain.service.UserRelationService;
import com.kaihei.esportingplus.user.utils.Utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-12 17:41
 * @Description:
 */
@Service
public class MembersUserManagerImpl implements MembersUserManager {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MembersRegisteredDeviceRepository membersRegisteredDeviceRepository;

    @Autowired
    private MembersUserRepository membersUserRepository;

    @Autowired
    private PaymentBankRepository paymentBankRepository;

    @Autowired
    private MembersAuth3Repository membersAuth3Repository;

    @Autowired
    private MembersUserESManager membersUserESManager;

    @Autowired
    private UserProperties userProperties;

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExternal;

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Resource(name = "pythonCacheManager")
    private CacheManager pythonCacheManager;

    @Autowired
    private MembersChickenCacheManager membersChickenCacheManager;

    @Autowired
    private PaymentAccountStatementRepository paymentAccountStatementRepository;

    @Autowired
    private RonyunUserIdGenerator ronyunUserIdGenerator;

    @Autowired
    private BaojiBaojiService baojiBaojiService;

    @Autowired
    private MembersUserWhitelistService membersUserWhitelistService;

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private MembersOrderCountService membersOrderCountService;

    @Autowired
    private BaoJiBnVerifypicRepository baoJiBnVerifypicRepository;

    /**
     * 检查系统是否开启关闭模式、白名单模式
     *  @param user 系统用户
     * @param forbiddenInWhiteMode
     */
    @Override
    public void checkSystemSwitch(MembersUser user, boolean forbiddenInWhiteMode) {
        if (userProperties.getUserSystemSwitch() == null) {//1.没有进行配置，不拦截
            return;
        } else if (userProperties.getUserSystemSwitch()
                == MembersAuthConstants.SYSTEM_CLOSE) {//2.系统关闭 不让登陆
            throw new BusinessException(BizExceptionEnum.USER_SYSTEM_MAINTAINING);
        } else if (userProperties.getUserSystemSwitch()
                == MembersAuthConstants.SYSTEM_WHITE_LIST) {//3.白名单模式下，白名单用户可登录，其他拦截
            if (user != null && user.getId() != null) {//白名单用户可以登录、测试账号可登陆
                if (membersUserWhitelistService.exists(user.getId()) || userProperties.getTestPhone().contains(user.getPhone())) {
                    return;
                }
                throw new BusinessException(BizExceptionEnum.USER_SYSTEM_MAINTAINING);
            }
            if (forbiddenInWhiteMode) {
                throw new BusinessException(BizExceptionEnum.USER_SYSTEM_MAINTAINING);
            }
        }
        return;
    }

    /**
     * 调用第三方服务检验传入的第三方凭证是否合法的
     */
    @Override
    public boolean checkLegalThirdpart(ThirdPartLoginParams params) {
        return ThirdpartValidatorFactory.determineValidator(params.getPlatform())
                .isValid(params, restTemplateExternal);
    }

    /**
     * 判断该设备是否可以注册用户
     *
     * @param deviceId 设备Id或uid
     */
    @Override
    public boolean canDeviceRegist(String deviceId) {
        if (deviceId != null) {
            MembersRegisteredDevice device = new MembersRegisteredDevice();
            device.setDeviceId(deviceId);
            int registCount = membersRegisteredDeviceRepository.selectCount(device);
            if (registCount >= userProperties.getUserMaxRegistPerDevice().intValue()) {
                logger.info(
                        "cmd=MembersUserManagerImpl.canDeviceRegist | msg={} | registed count={} | max regist count={}",
                        "该设备注册次数超过限制", registCount,
                        userProperties.getUserMaxRegistPerDevice());
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 判断该设备是否可以登录用户
     *
     * @param deviceId 设备Id或uid
     */
    @Override
    public boolean canDeviceLogin(String deviceId) {
        if (deviceId != null) {
            MembersRegisteredDevice device = new MembersRegisteredDevice();
            device.setLoginDeviceId(deviceId);
            int registCount = membersRegisteredDeviceRepository.selectCount(device);
            if (registCount >= userProperties.getUserMaxLoginPerDevice().intValue()) {
                logger.info(
                        "cmd=MembersUserManagerImpl.canDeviceLogin | msg={} | registed count={} | max regist count={}",
                        "该设备登录用户数超过限制", registCount,
                        userProperties.getUserMaxLoginPerDevice());
                return false;
            }
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public MembersUser createUserAndAuth3(ThirdPartLoginParams params) {
        //1.保存user
        MembersUser user = new MembersUser();
        String unionid = getThirdpartRegistRedistKey(params.getUnionid(), getGenerateUnionid());
        params.setUnionid(unionid);
        RLock redisLock = cacheManager.redissonClient().getLock(unionid);
        try {
            redisLock.lock(5, TimeUnit.SECONDS);//python代码逻辑也是5s
            user.setUsername(censorUserName(params.getUsername()));
            checkUserName(user, params);
            params.setUsername(user.getUsername());//如果产生新名字，把新名字告知params
            convertMembersUser(user, params);
            int i = membersUserRepository.insertMemberUser(user);
            if (i < 1) {
                logger.error("cmd=MembersUserManager.createUserAndAuth3 | msg={} | req={}",
                        "创建用户失败",
                        JSON.toJSON(params));
                throw new BusinessException(BizExceptionEnum.USER_SYSTEM_THIRDPART_VALID_FAIL);
            }

            //3. 创建auth3
            MembersAuth3 auth3 = new MembersAuth3();
            auth3.setCredential(params.getCredential());
            auth3.setIdentifier(params.getIdentifier());
            auth3.setPlatform(params.getPlatform());
            auth3.setUnionid(params.getUnionid());
            auth3.setUserId(user.getId());
            auth3.setPackageName("kaihei");//默认kaihei
            int j = membersAuth3Repository.insertSelective(auth3);
            if (j < 1) {
                logger.error("cmd=MembersUserManager.createUserAndAuth3 | msg={} | req={}",
                        "创建用户MembersAuth3失败",
                        JSON.toJSON(params));
                throw new BusinessException(BizExceptionEnum.USER_SYSTEM_THIRDPART_VALID_FAIL);
            }
        } catch (Exception e) {
            logger.error("cmd=MembersUserManager.createUserAndAuth3 | msg={} | req={} exception{}",
                    "创建用户和MembersAuth3失败",
                    JSON.toJSON(params), e);
            throw new BusinessException(BizExceptionEnum.USER_SYSTEM_THIRDPART_VALID_FAIL);
        } finally {
            if (redisLock != null && redisLock.isLocked()) {
                redisLock.unlock();
            }
        }

        return user;
    }

    @Transactional
    @Override
    public MembersUser phoneRegistCreateUser(PhoneRegistParam params) {
        MembersUser user = new MembersUser();
        user.setUsername(censorUserName(params.getUsername()));
        params.setUsername(user.getUsername());//如果产生新名字，把新名字告知params
        convertMembersUser(user, params);
        int i = membersUserRepository.insertMemberUser(user);
        if (i < 1) {
            logger.error("cmd=MembersUserManager.phoneRegistCreateUser | msg={} | req={}",
                    "创建用户失败",
                    JSON.toJSON(params));
            throw new BusinessException(BizExceptionEnum.USER_SYSTEM_PHONE_REGIST_FAIL);
        }
        return user;
    }

    @Override
    public void checkUserNameForPhoneRegist(String userName) {
        MembersUser user = new MembersUser();
        user.setUsername(userName);
        if (membersUserRepository.selectCount(user) > 0) {
            throw new BusinessException(BizExceptionEnum.USER_EXIST);
        }
    }

    private void checkUserName(MembersUser user, RegistLoginBaseParam params) {
        long start = System.currentTimeMillis();
        while (membersUserRepository.selectCount(user) > 0) {
            user.setUsername(params.getUsername() + Utils.random(4));
            if (System.currentTimeMillis() - start > 2 * 1000) {//超过2s超时
                throw new BusinessException(BizExceptionEnum.USER_SYSTEM_PHONE_REGIST_FAIL);
            }
        }
    }

    /**
     * 根据用户ID更新登录设备和登录时间
     */
    @Transactional
    @Override
    public void updateLoginRegistAndLoginTime(Integer userId, String deviceId) {
        if (deviceId != null) {
            Example registDeviceEx = new Example(MembersRegisteredDevice.class);
            registDeviceEx.createCriteria().andEqualTo("userId", userId);
            MembersRegisteredDevice device = new MembersRegisteredDevice();
            device.setLoginDeviceId(deviceId);
            membersRegisteredDeviceRepository.updateByExampleSelective(device, registDeviceEx);
        }
        MembersUser user = new MembersUser();
        user.setId(userId);
        user.setLastLogin(new Date());
        membersUserRepository.updateByPrimaryKeySelective(user);
    }

    /**
     * 如果没有账号则创建
     */
    @Transactional
    @Override
    public PaymentBank createBankIfNotExistForRegist(Integer userId) {
        if (userId == null) {
            return null;
        }
        PaymentBank condition = new PaymentBank();
        condition.setUserId(userId);
        PaymentBank bank = paymentBankRepository.selectOne(condition);
        if (bank == null) {
            bank = new PaymentBank();
            bank.setUserId(userId);
            bank.setBalance(0);
            bank.setBonus(userProperties.getBankInitBonus());
            bank.setUpdateTime(new Date());
            int i = paymentBankRepository.insertSelective(bank);
            if (i < 1) {
                logger.error(
                        "cmd=MembersUserManager.createBankIfNotExistForRegist | msg={} | userId={}",
                        "创建bank失败", userId);
                return null;
            }
        }
        PaymentAccountStatement as = new PaymentAccountStatement();
        as.setUserId(userId);
        as.setTotalFee(userProperties.getBankInitBonus());
        as.setOrderType(MembersAuthConstants.BANK_BILL_TYPE_BONUS);
        as.setBillType(MembersAuthConstants.BILL_TYPE_REDISTER_BONUS);
        as.setOrderId(Utils.generateOrderId());
        as.setCreateTime(new Date());
        int j = paymentAccountStatementRepository.insertSelective(as);
        if (j < 1) {
            logger.error(
                    "cmd=MembersUserManager.createBankIfNotExistForRegist | msg={} | userId={} | account_statement={}",
                    "创建account_statement失败", userId, JSON.toJSON(as));
        }
        return bank;
    }

    @Override
    public int updateMemberUserByUid(MembersUser user) {
        //修改数据库
        int result = membersUserRepository.updateMemberUser(user);

        //用户修改的时候触发更新es 前置save 保证执行celery的时候能拿到数据
        try{
            membersUserESManager.saveMembersUserES(user);
        }catch (Exception e){
            logger.error("cmd=updateMemberUserByUid.saveMembersUserES | msg={} | MembersUser={} | Exception={}",
                    "创建account_statement失败", JSON.toJSON(user),JSON.toJSON(e));
        }
        return result;
    }

    /**
     * 获取基本信息
     * @param uid
     * @return
     */
    @Override
    public UserVo getUser(String uid){
        String key = getUserKey(uid);
        UserVo vo = cacheManager.hget(key,"userVo",UserVo.class);
        if(vo != null){
            return vo;
        }

        UserVo userVo = new UserVo();
        List<MembersUser> membersUserList = membersUserRepository.selectUserInfoIdByUid(uid);
        logger.info("cmd = getBnPictures | uid={} | membersUserList={}", uid, JSON.toJSON(membersUserList));
        if(membersUserList != null && membersUserList.size() > 0){
            //组装基本信息
            MembersUser membersUser1 = membersUserList.get(0);
            userVo.setUid(uid);
            userVo.setAge(String.valueOf(membersUser1.getAge()));
            if(membersUser1.getBirthday() != null){
                userVo.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(membersUser1.getBirthday()));
            }
            userVo.setUserId(membersUser1.getId());
            userVo.setDesc(membersUser1.getDesc());
            userVo.setName(membersUser1.getUsername());
            userVo.setSex(membersUser1.getSex());
            userVo.setRegion(membersUser1.getRegion());
            userVo.setThumbnail(membersUser1.getThumbnail());
            userVo.setChicken_id(membersUser1.getChickenId());
            userVo.setConstellation(membersUser1.getConstellation());
            //获取融云UID
            userVo.setRcUserid(getRonyunUid(uid));
            cacheManager.hset(key,"userVo",userVo);
            cacheManager.expire(key,10 * 60);
        }
        logger.info("cmd = getBnPictures | uid={} | userVo={}", uid, JSON.toJSON(userVo));
        return userVo;
    }

    /**
     * 获取用户关系信息
     * @param uid
     * @param userId
     * @return
     */
    @Override
    public RelationVo getRelationVo(String uid,int userId){
        String key = getUserKey(uid);
        RelationVo vo = cacheManager.hget(key,"relationVo",RelationVo.class);
        if(vo != null){
            logger.info("cmd=getRelationVo redis.result | uid={} | userId={} | RelationVo={}", uid,userId,JSON.toJSON(vo));
            return vo;
        }

        UserSessionContext context = UserSessionContext.getUser();
        String currentUid = context.getUid();
        int currentUserId = context.getId();
        RelationVo relationVo = new RelationVo();
        relationVo.setUid(uid);

        //查询好友数、粉丝数、关注数
        long friend = userRelationService.friendCount(uid);
        long follow = userRelationService.followsCount(uid);
        long fans = userRelationService.fansCount(uid);
        relationVo.setFriend((int)friend);
        relationVo.setFans((int)fans);
        relationVo.setFollow((int)follow);

        //判断是否在黑名单中
        boolean blackList = isBlackList(currentUserId,userId);
        relationVo.setBlackList(blackList);

        //判断关系
        int relation = userRelationship(currentUid,uid);
        relationVo.setRelationship(relation);

        logger.info("cmd=getRelationVo result | uid={} | userId={} | RelationVo={}", uid,userId,JSON.toJSON(relationVo));
        cacheManager.hset(key,"relationVo",relationVo);
        cacheManager.expire(key,10 * 60);
        return relationVo;
    }

    /**
     * 获取暴鸡信息
     * @param uid
     * @param gameCode
     * @return
     */
    @Override
    public BaoJiVo getBaoJiVo(String uid,Integer gameCode){
        String key = getUserKey(uid);
        int game = gameCode == null ? 0 : gameCode;
        BaoJiVo vo = cacheManager.hget(key,"baoJiVo:" + game,BaoJiVo.class);
        if(vo != null){
            return vo;
        }

        BaoJiVo baoJiVo = new BaoJiVo();
        baoJiVo.setUid(uid);
        int identity = baojiBaojiService.getIdentityByUid(uid);
        baoJiVo.setIdentity(identity);
        List<String> pictures = getBnPictures(uid);
        baoJiVo.setBnPictures(pictures);

        //查询暴鸡等级
        BaojiBaoJiTag baojiBaoJiTag = null;
        if (gameCode == null){
            //查询暴鸡最高等级
            baojiBaoJiTag = baojiBaojiService.getBaoJiMaxLevel(uid);
        }else{
            //根据游戏查询暴鸡等级
            baojiBaoJiTag = baojiBaojiService.getBaoJiLevelByGame(uid,gameCode);
        }
        if (baojiBaoJiTag != null){
            baoJiVo.setBaojiLevel(baojiBaoJiTag.getValue());
            baoJiVo.setLevelName(baojiBaoJiTag.getDescription());
        }
        cacheManager.hset(key,"baoJiVo:" + gameCode,baoJiVo);
        cacheManager.expire(key,10 * 60);
        return baoJiVo;
    }

    @Override
    public UserOrderDateVo getUserOrderDate(String uid) {
        String key = getUserKey(uid);
        UserOrderDateVo vo = cacheManager.hget(key,"userOrderDateVo",UserOrderDateVo.class);
        if(vo != null){
            return vo;
        }
        UserOrderDateVo userOrderDateVo = membersOrderCountService.getUserOrderDate(uid);
        cacheManager.hset(key,"userOrderDateVo",userOrderDateVo);
        cacheManager.expire(key,10 * 60);
        return userOrderDateVo;
    }

    private String getUserKey(String uid){
        if(StringUtils.isEmpty(uid)){
            return null;
        }
        String key = String.format(UserRedisKey.USER_CARD_INFO, uid);
        return key;
    }

    /**
     * 获取暴娘认证的图片
     * @param uid
     * @return
     */
    private List<String> getBnPictures(String uid){
        BaoJiBnVerifypic baoJiBnVerifypic = baoJiBnVerifypicRepository.selectPictureByUid(uid);
        logger.info("cmd = getBnPictures | uid={} | baoJiBnVerifypic={}", uid, JSON.toJSON(baoJiBnVerifypic));
        List<String> pictures = new ArrayList<>();
        if(baoJiBnVerifypic != null){
            pictures.add(baoJiBnVerifypic.getPicture1());
            pictures.add(baoJiBnVerifypic.getPicture2());
            pictures.add(baoJiBnVerifypic.getPicture3());
        }
        return pictures;
    }

    /**
     * 获取两个用户的关系
     * 0. 自己
     * 1. 没有任何关系
     * 2. 互相关注
     * 3. A关注B A是B的粉丝
     * 4. B关注A B是A的粉丝
     * @param sourceId
     * @return
     */
    private int userRelationship(String sourceId, String targetId) {
        int result = 1;
        if(sourceId == targetId){
            result = 0;
        }else{
            result = userRelationService.relation(sourceId,targetId);
        }
        return result;
    }

    /**
     * 获取融云uid
     * @param uid
     * @return
     */
    private String getRonyunUid(String uid){
        String ronyunUid = ronyunUserIdGenerator.encodeIMUser(uid);
        return ronyunUid;
    }

    private String getThirdpartRegistRedistKey(String unionid, String defaultValue) {
        String tail = unionid;
        if (tail == null) {
            tail = defaultValue;
        }
        return String.format(UserRedisKey.USER_REGIST_LOCK_KEY, tail);
    }

    private String getGenerateUnionid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void convertMembersUser(MembersUser user, RegistLoginBaseParam params) {
        Date d = new Date();
        user.setFirstName("");
        user.setEmail("");
        user.setLastName("");
        user.setDesc("");
        user.setPassword("");
        user.setThumbnail(params.getThumbnail() != null ? params.getThumbnail() : "");
        if (params instanceof ThirdPartLoginParams) {
            String registWay = ((ThirdPartLoginParams) params).getPlatform();
            user.setRegisterWay(registWay != null ? registWay : "-");
            user.setPhone(params.getUid());
        } else if (params instanceof PhoneRegistParam) {
            user.setRegisterWay(MembersAuthConstants.REGISTER_WAY_PHONE);
            user.setPhone(((PhoneRegistParam) params).getPhone());
        }
        user.setSex(getSex(params.getSex()));
        user.setRegion(params.getRegion() != null ? params.getRegion() : "");
        user.setUid(params.getUid());
        user.setIsStaff(0);
        user.setIsActive(1);
        user.setDateJoined(d);
        user.setLastLogin(d);
        user.setShowGroup(0);

        user.setIsSuperuser(0);
        user.setChickenId(membersChickenCacheManager.getAvailableChickenId());
    }

    private Integer getSex(Integer sex) {
        if (sex != null && (sex == 1 || sex == 2)) {
            return sex;
        }
        return 1;
    }

    private String censorUserName(String username) {
        //目前python代码的逻辑跟数据库最终数据表现不一致，所以先不做审查
        if (username == null || "".equals(username.trim())) {
            StringBuilder sb = new StringBuilder();
            sb.append("用户").append(getGenerateUnionid().substring(0, 8));
            username = sb.toString();
        }
        return username;
    }

    @Override
    public UserInfoVo getUserByUid(String uid){
        String key = "user_info_" + uid;
        String value = cacheManager.get(key,String.class);
        UserInfoVo userInfoVo = null;
        if(value != null){
            userInfoVo = JSON.parseObject(value,UserInfoVo.class);
        }else{
            //查询UserInfoVo信息
            userInfoVo = getUserInfo(uid);
            String valuesNew = JSON.toJSONString(userInfoVo);
            cacheManager.set(key,valuesNew);
            //设置过期时间10分钟
            cacheManager.expireAt(key,10 * 60 * 1000);
        }
        return userInfoVo;
    }

    @Override
    public boolean isBlackList(int currentUserId, int userId) {
        String key = "user:bl:" + currentUserId;
        return pythonCacheManager.sIsMember(key,String.valueOf(userId));
    }

    /**
     * 根据uid获取用户信息
     * @param uid
     * @return
     */
    private UserInfoVo getUserInfo(String uid){
        UserInfoVo userInfoVo = new UserInfoVo();
        List<MembersUser> membersUserList = membersUserRepository.selectUserInfoIdByUid(uid);
        if(membersUserList == null && membersUserList.size() <= 0){
            return null;
        }else{
            //组装基本信息
            MembersUser membersUser1 = membersUserList.get(0);
            userInfoVo.setUid(uid);
            userInfoVo.setAge(String.valueOf(membersUser1.getAge()));
            if(membersUser1.getBirthday() != null){
                userInfoVo.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(membersUser1.getBirthday()));
            }
            userInfoVo.setDesc(membersUser1.getDesc());
            userInfoVo.setName(membersUser1.getUsername());
            userInfoVo.setSex(membersUser1.getSex());
            userInfoVo.setRegion(membersUser1.getRegion());
            userInfoVo.setThumbnail(membersUser1.getThumbnail());
            userInfoVo.setChicken_id(membersUser1.getChickenId());
            userInfoVo.setConstellation(membersUser1.getConstellation());
            //userInfoVo.setIm_id("");

            //查询好友数、粉丝数、关注数
            long friend = userRelationService.friendCount(uid);
            long follow = userRelationService.followsCount(uid);
            long fans = userRelationService.fansCount(uid);
            userInfoVo.setFriend((int)friend);
            userInfoVo.setFans((int)fans);
            userInfoVo.setFollow((int)follow);

            //是否是暴鸡/暴娘
            boolean isBaoji = baojiBaojiService.isBaoji(membersUser1.getId(),uid);
            boolean isBn = baojiBaojiService.isBaoniang(uid);
            userInfoVo.setIs_baoji(isBaoji);
            userInfoVo.setIs_bn(isBn);
        }
        return userInfoVo;
    }
}