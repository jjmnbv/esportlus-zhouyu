package com.kaihei.esportingplus.user.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.config.RonyunUserIdGenerator;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.ThumbnailVerifyResultType;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.file.WordFilterService;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.core.api.feign.QiniuManageServiceClient;
import com.kaihei.esportingplus.core.api.vo.QiniuImageCheckVo;
import com.kaihei.esportingplus.user.api.enums.ThumbnailVerifyStatusType;
import com.kaihei.esportingplus.user.api.params.RegistLoginBaseParam;
import com.kaihei.esportingplus.user.api.vo.BaoJiVo;
import com.kaihei.esportingplus.user.api.vo.ImmemberVo;
import com.kaihei.esportingplus.user.api.vo.PicturesVo;
import com.kaihei.esportingplus.user.api.vo.RelationVo;
import com.kaihei.esportingplus.user.api.vo.UserCardVo;
import com.kaihei.esportingplus.user.api.vo.UserInfoVo;
import com.kaihei.esportingplus.user.api.vo.UserOrderDateVo;
import com.kaihei.esportingplus.user.api.vo.UserUrlMessageVo;
import com.kaihei.esportingplus.user.api.vo.UserVo;
import com.kaihei.esportingplus.user.api.vo.WxUserInfo;
import com.kaihei.esportingplus.user.bulider.RedisKeySegmentBuilder;
import com.kaihei.esportingplus.user.bulider.RedisKeySegmentType;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import com.kaihei.esportingplus.user.data.manager.MembersAlbumpictureManager;
import com.kaihei.esportingplus.user.data.manager.MembersChickenCacheManager;
import com.kaihei.esportingplus.user.data.manager.MembersUserManager;
import com.kaihei.esportingplus.user.data.pyrepository.BaoJiBnVerifypicRepository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersAuth3Repository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersRegisteredDeviceRepository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersUserRepository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersUserThumbnailRecordRepository;
import com.kaihei.esportingplus.user.data.pyrepository.MembersUserrelationshipRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersAuth3;
import com.kaihei.esportingplus.user.domain.entity.MembersRegisteredDevice;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import com.kaihei.esportingplus.user.domain.entity.MembersUserThumbnailRecord;
import com.kaihei.esportingplus.user.domain.entity.MembersUserrelationship;
import com.kaihei.esportingplus.user.domain.service.BaojiBaojiService;
import com.kaihei.esportingplus.user.domain.service.MembersOrderCountService;
import com.kaihei.esportingplus.user.domain.service.MembersUserService;
import com.kaihei.esportingplus.user.domain.service.UserRelationService;
import com.kaihei.esportingplus.user.utils.ConversionUtil;
import com.kaihei.esportingplus.user.utils.DateUtil;
import com.kaihei.esportingplus.user.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiekeqing
 * @Title: MembersUserServiceImpl
 * @Description: 终端用户服务实现类
 * @date 2018/9/1120:31
 */
@Service
public class MembersUserServiceImpl implements MembersUserService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final int UID_LENGTH = 8;
    //"设定不需要七牛鉴黄值 大于该值的时候就不需要进行鉴黄(分数越高越靠谱)
    private static final float QINIU_PIC_VERIFY_YELLOW_SCORE_DIVID = 0.85f;
    //设定不需要七牛鉴恐值 大于该值的时候就不需要进行鉴恐(分数越高越靠谱)
    private static final float QINIU_PIC_VERIFY_TERROR_SCORE_DIVID = 0.85f;

    @Autowired
    private MembersRegisteredDeviceRepository membersRegisteredDeviceRepository;

    @Autowired
    private MembersUserRepository membersUserRepository;

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private MembersUserManager membersUserManager;

    @Autowired
    private MembersAuth3Repository membersAuth3Repository;

    @Autowired
    private MembersAlbumpictureManager membersAlbumpictureManager;

    @Autowired
    private MembersUserThumbnailRecordRepository membersUserThumbnailRecordRepository;

    @Autowired
    private MembersUserrelationshipRepository membersUserrelationshipRepository;

    @Autowired
    private MembersChickenCacheManager membersChickenCacheManager;

    @Autowired
    private WordFilterService aliyunGreenWordFilterService;

    @Autowired
    private RonyunUserIdGenerator ronyunUserIdGenerator;

    @Autowired
    private QiniuManageServiceClient qiniuManageServiceClient;

    @Autowired
    private DictionaryClient dictionaryClient;

    @Autowired
    private BaojiBaojiService baojiBaojiService;

    @Autowired
    private BaoJiBnVerifypicRepository baoJiBnVerifypicRepository;

    @Autowired
    private MembersOrderCountService membersOrderCountService;

    @Autowired
    private UserRelationService userRelationService;

    /**
     * 新增注册设备记录
     */
    @Override
    public MembersRegisteredDevice saveDevice(RegistLoginBaseParam params, Integer userId,
            String rToken) {
        MembersRegisteredDevice device = new MembersRegisteredDevice();
        device.setDeviceId(params.getDevice_id());
        device.setLoginDeviceId(params.getDevice_id());
        device.setChannel(params.getChannel());
        //默认1000
        device.setPlatform(1000);
        device.setUserAgent(params.getUser_agent());
        device.setUserId(userId);
        device.setRcloudToken(rToken);
        int i = membersRegisteredDeviceRepository.insertSelective(device);
        if (i < 1) {
            logger.error(
                    "cmd=MembersAuth3Service.saveDevice | msg={} | req={} | userId={} | rToken={}",
                    "创建用户失败", JSON.toJSON(params), userId, rToken);
        }
        return device;
    }

    /**
     * 更新RegisterDevice融云token
     */
    @Override
    public boolean updateDeviceRtoken(String rToken, Integer userId) {
        MembersRegisteredDevice condition = new MembersRegisteredDevice();
        condition.setUserId(userId);
        MembersRegisteredDevice record = membersRegisteredDeviceRepository.selectOne(condition);
        if (record == null) {
            logger.info(
                    "cmd=MembersUserService.updateDeviceRtoken | msg=没有该记录 | rToken={} | userId={}",
                    rToken, userId);
            return false;
        }
        record.setRcloudToken(rToken);
        int i = membersRegisteredDeviceRepository.updateByPrimaryKeySelective(record);
        if (i < 0) {
            logger.error(
                    "cmd=MembersUserService.updateDeviceRtoken | msg=修改融云token失败 | rToken={} | record",
                    rToken, JacksonUtils.toJson(record));
        }
        return true;
    }

    @Override
    public MembersUser getUserByAuth3(String unionid, String platform) {
        return membersUserRepository.selectByAuth3Leftjoin(unionid, platform);
    }

    @Override
    public MembersRegisteredDevice getOrCreateDevice(MembersUser user) {
        MembersRegisteredDevice condition = new MembersRegisteredDevice();
        condition.setUserId(user.getId());
        MembersRegisteredDevice device = membersRegisteredDeviceRepository
                .selectOne(condition);
        if (device == null) {
            device = new MembersRegisteredDevice();
            device.setUserId(user.getId());
            device.setDeviceId(user.getUid());
            device.setLoginDeviceId(user.getUid());
            int i = membersRegisteredDeviceRepository.insertSelective(device);
            if (i < 0) {
                logger.error(
                        "cmd=MembersAuth3Service.getOrCreate | msg={} | req={}",
                        "创建RegistDevice失败", JSON.toJSON(device));
            }
        }
        return device;
    }

    @Override
    public MembersUser getMembersUserById(Integer id) {
        String key = RedisKeySegmentBuilder
                .bulid(RedisKeySegmentType.INT_PREFIX, id.toString(), UserRedisKey.USER_BASE_INFO);
        MembersUser membersUser = cacheManager.hget(key, id.toString(), MembersUser.class);
        if (membersUser == null) {
            membersUser = membersUserRepository.selectByUserId(id);
            if (membersUser != null) {
                cacheManager.hset(key, id.toString(), membersUser);
            }
        }
        return membersUser;
    }

    @Override
    public String getRandomUid() {

        long num = cacheManager.incr(UserRedisKey.USER_UID_INCR_KEY);
        // 初始化缓存自增值
        if (num <= 1) {
            RLock rlock = null;
            try {
                rlock = cacheManager.redissonClient().getLock(UserRedisKey.USER_UID_INCR_LOCK_KEY);
                rlock.lock(1000, TimeUnit.MILLISECONDS);
                num = initIncrUid();
            } catch (Exception e) {
                //TODO
            } finally {
                if (rlock != null) {
                    rlock.unlock();
                }
            }
        }

        return ConversionUtil.encode(num, UID_LENGTH);
    }

    private long initIncrUid() {
        long num = membersUserRepository.selectMaxUserId();
        cacheManager.set(UserRedisKey.USER_UID_INCR_KEY, String.valueOf(num));
        return num;
    }

    @Override
    public Integer getUserIdByUid(String uid) {
        String key = RedisKeySegmentBuilder.bulid(RedisKeySegmentType.HASH, uid,
                UserRedisKey.USER_UID_MAPPING_USERID);
        Integer userId = cacheManager.hget(key, uid.toString(), Integer.class);

        if (userId == null || userId.intValue() <= 0) {
            userId = membersUserRepository.selectUserIdByUid(uid);
            if (userId != null && userId.intValue() > 0) {
                cacheManager.hset(key, uid, userId.intValue());
            }
        }

        return userId;
    }

    @Override
    public int updateUsername(String uid, String username) {
        logger.info("cmd=updateUsername.param | uid={} |username={}", uid,username);
        //空判断
        if (StringUtils.isEmpty(username) || username.length() > 50) {
            logger.error("cmd=updateUsername.param | username={} | msg={}", username,
                    BizExceptionEnum.PARAM_ENTRY_ERROR);
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }

        //敏感字判断
        try {
            if (!aliyunGreenWordFilterService.checkWord(username)) {
                logger.error("cmd=updateUsername.aliyunCheckText | username={} | msg={}",
                        username, BizExceptionEnum.USER_VALIDATE_SENSITIVE_WORD);
                throw new BusinessException(BizExceptionEnum.USER_VALIDATE_SENSITIVE_WORD);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //存在与否判断
        Integer result = membersUserRepository.getUserIdByUserName(username);
        if (result != null && result > 0) {
            logger.error("cmd=updateUsername.select | username={} | msg={}",
                    username, BizExceptionEnum.USER_EXIST);
            throw new BusinessException(BizExceptionEnum.USER_EXIST);
        }

        //修改数据库中用户名
        MembersUser membersUserUpdate = new MembersUser();
        membersUserUpdate.setUsername(username);
        membersUserUpdate.setUid(uid);
        return membersUserManager.updateMemberUserByUid(membersUserUpdate);
    }

    @Override
    public int updateRegion(String uid, String region) {
        if (StringUtils.isEmpty(region)) {
            logger.error("cmd=updateRegion.param | uid={} |region={} | msg={}", uid,region,
                    BizExceptionEnum.PARAM_ENTRY_ERROR);
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }
        MembersUser membersUser = new MembersUser();
        membersUser.setUid(uid);
        membersUser.setRegion(region);
        logger.info("cmd=updateRegion.param | uid={} |region={}", uid,region);
        return membersUserManager.updateMemberUserByUid(membersUser);
    }

    @Override
    public int updateDesc(String uid, String desc) {
        logger.info("cmd=updateDesc.param | uid={} |desc={}", uid,desc);
        if(desc == null || desc.length() > 52){
            logger.error("cmd=updateDesc.param | uid={} |desc={} | msg={}", uid,desc,
                    BizExceptionEnum.PARAM_ENTRY_ERROR);
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }

        //空判断
        if (StringUtils.isNotEmpty(desc)) {
            //敏感字判断
            try {
                if (!aliyunGreenWordFilterService.checkWord(desc)) {
                    logger.error("cmd=updateDesc.aliyunCheckText | desc={} | msg={}", desc,
                            BizExceptionEnum.PARAM_ENTRY_ERROR);
                    throw new BusinessException(BizExceptionEnum.USER_VALIDATE_DESC_SENSITIVE_WORD);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        MembersUser membersUser = new MembersUser();
        membersUser.setUid(uid);
        membersUser.setDesc(desc);
        logger.info("cmd=updateDesc.param | uid={} |desc={}", uid,desc);
        return membersUserManager.updateMemberUserByUid(membersUser);
    }

    @Override
    public int updateBirthDay(String uid, String birthdayData) {
        //空判断
        logger.info("cmd=updateBirthDay.param | uid={} |birthdayData={}", uid,birthdayData);
        if (StringUtils.isEmpty(birthdayData)) {
            logger.error("cmd=updateBirthDay.updateBirthDay | birthdayData={} | msg={}",
                    birthdayData, BizExceptionEnum.PARAM_ENTRY_ERROR);
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }
        MembersUser membersUser = new MembersUser();
        membersUser.setUid(uid);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = sdf.parse(birthdayData);
            membersUser.setBirthday(birthday);
            membersUser.setAge(DateUtil.getAge(birthday));
        } catch (Exception e) {
            logger.error("cmd=updateBirthDay.SimpleDateFormat | birthdayData={} | msg={}",
                    birthdayData, BizExceptionEnum.TIME_PATTERN);
            throw new BusinessException(BizExceptionEnum.TIME_PATTERN);
        }
        logger.info("cmd=updateBirthDay.param | uid={} |birthday={}", uid,birthdayData);
        return membersUserManager.updateMemberUserByUid(membersUser);
    }

    @Override
    public int updateShowGroup(String uid, Boolean showgroup) {
        logger.info("cmd=updateShowGroup.param | uid={} |showgroup={}", uid,showgroup);
        if(StringUtils.isEmpty(uid) || showgroup == null){
            logger.error("cmd=updateShowGroup | uid={} | birthdayData={} | msg={}",
                    uid,showgroup, BizExceptionEnum.PARAM_ENTRY_ERROR);
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }
        MembersUser membersUser = new MembersUser();
        membersUser.setUid(uid);
        int sgroup = showgroup ? 1 : 0;
        membersUser.setShowGroup(sgroup);
        logger.info("cmd=updateShowGroup.param | uid={} | showgroup={}", uid,sgroup);
        return membersUserManager.updateMemberUserByUid(membersUser);
    }

    @Override
    public ImmemberVo getImMenber(String imUid) {
        if(StringUtils.isEmpty(imUid)){
            logger.error("cmd=getImMenber | imUid={} | msg={}", imUid, BizExceptionEnum.PARAM_ENTRY_ERROR);
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }
        ImmemberVo immemberVo = new ImmemberVo();

        //判断是否是指定用户
        try{
            ResponsePacket<List<DictBaseVO<Object>>> dictionary = dictionaryClient.findByCategoryCode(
                            DictionaryCategoryCodeEnum.BJDJ_RONGYUN_ADMIN.getCode(), null);
            logger.info("cmd=getImMenber findByCategoryCode param | imUid={} | dictionary={}", imUid,JSONObject.toJSON(dictionary));
            if(dictionary != null ){
                List<DictBaseVO<Object>> dictBaseVOList = dictionary.getData();
                if(dictBaseVOList != null && dictBaseVOList.size() > 0){
                    for (DictBaseVO vo: dictBaseVOList) {
                        if(vo.getCode().equals(imUid)){
                            Map dictionaryValue = (Map) vo.getValue();
                            String uid = (String) dictionaryValue.get("uid");
                            String username = (String) dictionaryValue.get("username");
                            String thumbnail = (String) dictionaryValue.get("thumbnail");
                            immemberVo.setUid(uid);
                            immemberVo.setThumbnail(thumbnail);
                            immemberVo.setUsername(username);
                            logger.info("cmd=getImMenber admin param | imUid={} | immemberVo={}", imUid,JSONObject.toJSON(immemberVo));
                            return immemberVo;
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("cmd=getImMenber admin param | imUid={} | immemberVo={}", imUid,JSONObject.toJSON(e));
        }

        String uid = imUid.split("_")[0];
        List<MembersUser> membersUserList = membersUserRepository.selectUserInfoIdByUid(uid);
        if (membersUserList == null || membersUserList.size() <= 0 || membersUserList.size() > 1) {
            logger.error("cmd=getImMenber | imUid={} | membersUserList={} | msg={}",
                    imUid, JSONObject.toJSON(membersUserList), BizExceptionEnum.USER_NOT_EXIST);
            throw new BusinessException(BizExceptionEnum.USER_NOT_EXIST);
        }
        MembersUser membersUser = membersUserList.get(0);
        immemberVo.setUid(membersUser.getUid());
        immemberVo.setThumbnail(membersUser.getThumbnail());
        immemberVo.setUsername(membersUser.getUsername());
        logger.info("cmd=getImMenber.param | imUid={} | immemberVo={}", imUid,JSONObject.toJSON(immemberVo));
        return immemberVo;
    }

    @Override
    public MembersUser getMembersUserByUid(String uid) {
        logger.info("cmd=getMembersUserByUid.param | uid={}", uid);
        if(StringUtils.isEmpty(uid)){
            logger.error("cmd = getMembersUserByUid | uid={} | msg={}", uid, BizExceptionEnum.PARAM_ENTRY_ERROR);
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }
        Integer userId = getUserIdByUid(uid);
        return getMembersUserById(userId);
    }

    /**
     * 通过手机号码查询用户
     */
    @Override
    public MembersUser getUserByPhone(String phone) {
        MembersUser con = new MembersUser();
        con.setPhone(phone);
        MembersUser user = membersUserRepository.selectOne(con);
        if (user == null) {
            logger.info("cmd=MembersUserService.getUserByPhone | msg=该手机号码没有注册用户 | phone={}",
                    phone);
        }
        return user;
    }

    /**
     * 通过userId判断有没有绑定wx
     * @param userId
     * @param platform
     * @return
     */
    @Override
    public boolean userHasBindAuth(Integer userId, String platform) {
        MembersAuth3 con = new MembersAuth3();
        con.setUserId(userId);
        con.setPlatform(platform);
        int count = membersAuth3Repository.selectCount(con);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public PicturesVo updatePicture(Integer userId, String url, String action, Integer id) {
        logger.info("cmd=updatePicture.param | userId={} | url={} | action={} | id={}", userId,url,action,id);
        PicturesVo picturesVo = null;
        if("add".equals(action)){
            picturesVo = membersAlbumpictureManager.addPicture(userId,url);
        }else if("change".equals(action)){
            picturesVo = membersAlbumpictureManager.changePicture(userId,url,id);
        }else if("delete".equals(action)){
            picturesVo = membersAlbumpictureManager.deletePicture(userId,id);
        }else{
            logger.error("cmd=updatePicture.error | userId={} | url={} | action={} | id={} | msg={}",
                    userId,url,action,id,BizExceptionEnum.ACTION_NO_FIND_METHOD.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ACTION_NO_FIND_METHOD);
        }
        return picturesVo;
    }

    /**
     * 更新用户头像
     * @param userId
     * @param avatar
     * @return
     */
    @Override
    public Integer updateAvatar(int userId, String avatar) {
        if(StringUtils.isEmpty(avatar)){
            logger.error("cmd=updateAvatar.error | userId={} | avatar={} | msg={}",
                    userId,avatar,BizExceptionEnum.PARAM_ENTRY_ERROR.getErrMsg());
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }

        //进行鉴黄鉴暴
        int result = checkQpulpAndCheckQterror(avatar,userId);
        if (result == -1){
            throw new BusinessException(BizExceptionEnum.USER_AVATAR_VERIFY);
        }

        if(result == ThumbnailVerifyResultType.NORMAL.getCode()){
            //修改表members_user中，thumbnail字段为avatar
            MembersUser membersUser = new MembersUser();
            membersUser.setId(userId);
            membersUser.setThumbnail(avatar);
            logger.info("cmd=verifyThumbnail.updateMemberUser param | membersUser={}", JSONObject.toJSON(membersUser));
            membersUserRepository.updateMemberUserById(membersUser);
        }else{
            throw new BusinessException(BizExceptionEnum.USER_AVATAR_VERIFY_FAIL);
        }
        return result;
    }

    /**
     * 小程序创建用户
     * @param info
     * @param phone
     * @return
     */
    @Override
    public MembersUser mpCreateUser(WxUserInfo info, String phone) {
        MembersUser user = new MembersUser();
        Integer sex = Integer.valueOf(info.getGender());
        String uid = getRandomUid();

        user.setPhone(phone);
        user.setSex(sex == 0 ? 3 : sex);//0未知，对应系统的3未知性别
        user.setThumbnail(info.getAvatarUrl());
        user.setRegisterWay(MembersAuthConstants.REGISTER_WAY_MP);
        user.setUsername(info.getNickName() + Utils.random(4));
        user.setUid(uid);
        checkUserName(user);
        fillNotNullUserFied(user);
        int i = membersUserRepository.insertMemberUser(user);
        if (i < 1) {
            logger.error("cmd=MembersUserService.mpCreateUser | msg={} | user={} | wxUserInfo={}",
                    "小程序创建用户失败", JSON.toJSON(user), JSON.toJSON(info));
            throw new BusinessException(BizExceptionEnum.USER_SYSTEM_THIRDPART_VALID_FAIL);
        }
        return user;
    }

    /**
     * 审核照片
     * @param message
     * @return
     */
    @Override
    public void verifyPicture(UserUrlMessageVo message) {
        int userId = message.getUserId();
        String picturePath = message.getUrl();
        int pictureId = message.getId();
        logger.info("cmd=verifyPicture.insert param | message={}", JSONObject.toJSON(message));
        membersAlbumpictureManager.verifyPicture(userId,picturePath,pictureId);
    }

    /**
     * 根据uid获取用户信息
     * @param uid
     * @return
     */
    @Override
    public UserInfoVo getUserInfo(String uid) {
        UserSessionContext context = UserSessionContext.getUser();
        int currentUserId = context.getId();
        int userId = 0;
        if(StringUtils.isEmpty(uid)){
            uid = context.getUid();
            userId = context.getId();
        }else{
            List<MembersUser> userList = membersUserRepository.selectUserInfoIdByUid(uid);
            if(userList == null || userList.size() <= 0){
                logger.error("cmd = getUserInfo | currentUserId={} | uid={} | msg={}",
                        currentUserId, uid, BizExceptionEnum.USER_NOT_EXIST);
                throw new BusinessException(BizExceptionEnum.USER_NOT_EXIST);
            }
            userId = userList.get(0).getId();
        }
        UserInfoVo userInfoVo =  membersUserManager.getUserByUid(uid);
        boolean blackList = membersUserManager.isBlackList(currentUserId,userId);
        userInfoVo.setIs_black_list(blackList);

        int relation = userRelationship(currentUserId,userId);
        userInfoVo.setRelationship(relation);

        userInfoVo.setRcUserid(getRonyunUid(uid));
        return userInfoVo;
    }

    /**
     * 获取个人资料卡片
     * @param uid
     * @param game
     * @return
     */
    @Override
    public UserCardVo getUserCard(String uid, Integer game) {
        UserCardVo userCardVo = new UserCardVo();
        UserVo userVo = getUser(uid);
        RelationVo relationVo = getRelationVo(uid,userVo.getUserId());
        BaoJiVo baoJiVo = getBaoJiVo(uid,game);
        //UserOrderDateVo userOrderDateVo = membersOrderCountService.getUserOrderDate(uid);
        UserOrderDateVo userOrderDateVo = membersUserManager.getUserOrderDate(uid);
        userCardVo.setUserinfo(userVo);
        userCardVo.setRelationinfo(relationVo);
        userCardVo.setUserorderdata(userOrderDateVo);
        userCardVo.setBaojiinfo(baoJiVo);
        logger.info("cmd = getUserCard | uid={} | userCardVo={}", uid, JSON.toJSON(userCardVo));
        return userCardVo;
    }

    /**
     * 获取基本信息
     * @param uid
     * @return
     */
    @Override
    public UserVo getUser(String uid){
        return membersUserManager.getUser(uid);
    }

    /**
     * 获取用户关系信息
     * @param uid
     * @param userId
     * @return
     */
    @Override
    public RelationVo getRelationVo(String uid,int userId){
        return membersUserManager.getRelationVo(uid,userId);
    }

    /**
     * 获取暴鸡信息
     * @param uid
     * @param gameCode
     * @return
     */
    @Override
    public BaoJiVo getBaoJiVo(String uid,Integer gameCode){
        return membersUserManager.getBaoJiVo(uid,gameCode);
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

    /**
     * 鉴黄鉴暴，并且判断是否转人工审核
     * @param avatar
     * @param userId
     * @return
     */
    private int checkQpulpAndCheckQterror(String avatar,int userId){
        boolean review = false;
        JSONObject verifyTracking = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
        //鉴定图片是否涉黄/涉暴
        int result = ThumbnailVerifyResultType.NORMAL.getCode();
        ResponsePacket<QiniuImageCheckVo> qpulpResult = qiniuManageServiceClient.checkQpulpImage(avatar);
        logger.info("cmd=updateAvatar.checkQpulpImage | userId={} | avatar={} | msg={}", userId,avatar,JSON.toJSON(qpulpResult));
        if(qpulpResult.responseSuccess()){
            if (qpulpResult.getData().getVerify()) {
                //鉴定图片是否涉暴
                ResponsePacket<QiniuImageCheckVo> qterrorResult = qiniuManageServiceClient.checkQterrorImage(avatar);
                logger.info("cmd=updateAvatar.checkQterrorImage | userId={} | avatar={} | msg={}", userId, avatar, JSON.toJSON(qterrorResult));
                result = qterrorResult.getData().getVerifyCode();
                //需要人工审核，则判断分数是否大于等于标准值（大于等于，则不需要转人工）
                review = Boolean.parseBoolean(qterrorResult.getData().getReview());
                review = review ? !(qpulpResult.getData().getScore() >= QINIU_PIC_VERIFY_TERROR_SCORE_DIVID) : review;
                verifyTracking.put("qiniu_verify_violence",qpulpResult.getData());
                verifyTracking.put("qiniu_verify_violence_time",sdf.format(new Date()));
            } else {
                result = qpulpResult.getData().getVerifyCode();
                //需要人工审核，则判断分数是否大于等于标准值（大于等于，则不需要转人工）
                review = Boolean.parseBoolean(qpulpResult.getData().getReview());
                review = review ? !(qpulpResult.getData().getScore() >= QINIU_PIC_VERIFY_YELLOW_SCORE_DIVID) : review;
                verifyTracking.put("qiniu_verify_pornography",qpulpResult.getData());
                verifyTracking.put("qiniu_verify_pornography_time",sdf.format(new Date()));
            }
        }else{
            throw new BusinessException(BizExceptionEnum.HYSTRIX_SERVER);
        }
        logger.info("cmd=updateAvatar.checkQterrorImage | userId={} | avatar={} | review={}", userId,avatar,review);

        //判断是否转人工
        if(result != ThumbnailVerifyResultType.NORMAL.getCode() && result != ThumbnailVerifyResultType.SEXY.getCode()){
            if(review){
                MembersUserThumbnailRecord membersUserThumbnailRecord = new MembersUserThumbnailRecord();
                membersUserThumbnailRecord.setCreateTime(new Date());
                membersUserThumbnailRecord.setThumbnail(avatar);
                membersUserThumbnailRecord.setUpdateTime(new Date());
                membersUserThumbnailRecord.setUserId(userId);
                membersUserThumbnailRecord.setVerifyTracking(verifyTracking.toString());
                membersUserThumbnailRecord.setVerifyStatus(ThumbnailVerifyStatusType.VERIFY.getCode().shortValue());
                logger.info("cmd=updateAvatar.insert param | userId={} | avatar={} | msg={}",
                        userId,avatar,JSONObject.toJSON(membersUserThumbnailRecord));
                membersUserThumbnailRecordRepository.insert(membersUserThumbnailRecord);
                return -1;
            }
        }
        return result;
    }

    /**
     * 获取两个用户的关系
     * 0. 自己
     * 1. 没有任何关系
     * 2. 互相关注
     * 3. A关注B A是B的粉丝
     * 4. B关注A B是A的粉丝
     * @param userId
     * @return
     */
    private int userRelationship(int currentUserId,int userId) {
        int result = 1;
        if(currentUserId == userId){
            result = 0;
        }else{
            List<MembersUserrelationship> membersUserrelationshipList =
                    membersUserrelationshipRepository.selectUserRelationship(currentUserId,userId);
            if(membersUserrelationshipList != null && membersUserrelationshipList.size() > 0){
                MembersUserrelationship membersUserrelationship = membersUserrelationshipList.get(0);
                result = membersUserrelationship.getIsFriend() ? 1 : 2;
            }else{
                List<MembersUserrelationship> membersUserrelationship2List =
                        membersUserrelationshipRepository.selectUserRelationship(userId,currentUserId);
                if(membersUserrelationship2List != null && membersUserrelationship2List.size() > 0){
                    MembersUserrelationship membersUserrelationship = membersUserrelationship2List.get(0);
                    if(!membersUserrelationship.getIsFriend()){
                        result = 3;
                    }
                }
            }
        }
        return result;
    }

    private void fillNotNullUserFied(MembersUser user) {
        Date d = new Date();

        user.setDesc("");
        user.setPassword("");
        user.setFirstName("");
        user.setEmail("");
        user.setLastName("");
        user.setRegion("");
        user.setIsStaff(0);
        user.setIsActive(1);
        user.setDateJoined(d);
        user.setLastLogin(d);
        user.setShowGroup(0);
        user.setIsSuperuser(0);
        user.setChickenId(membersChickenCacheManager.getAvailableChickenId());
    }

    private void checkUserName(MembersUser user) {
        long start = System.currentTimeMillis();
        MembersUser con = new MembersUser();
        String userName = user.getUsername();
        con.setUsername(userName);
        while (membersUserRepository.selectCount(con) > 0) {
            userName = userName + Utils.random(4);
            con.setUsername(userName);
            if (System.currentTimeMillis() - start > 2 * 1000) {//超过2s超时
                throw new BusinessException(BizExceptionEnum.USER_SYSTEM_PHONE_REGIST_FAIL);
            }
        }
        user.setUsername(userName);
    }

}
