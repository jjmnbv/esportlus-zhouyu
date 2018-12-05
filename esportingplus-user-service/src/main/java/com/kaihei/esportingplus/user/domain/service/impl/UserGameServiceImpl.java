package com.kaihei.esportingplus.user.domain.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.feign.ResourceServiceClient;
import com.kaihei.esportingplus.api.vo.RedisAwakeningCareer;
import com.kaihei.esportingplus.api.vo.RedisGameRaid;
import com.kaihei.esportingplus.api.vo.RedisSmallZoneRefAcrossZone;
import com.kaihei.esportingplus.api.vo.RedisTopCareer;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.config.SnowFlakeConfig;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.user.api.params.UserGameQueryBaseParams;
import com.kaihei.esportingplus.user.api.params.UserGameRaidCodeQueryParams;
import com.kaihei.esportingplus.user.api.params.UserGameRoleAcrossDataQueryParams;
import com.kaihei.esportingplus.user.api.params.UserGameRoleAcrossQueryParams;
import com.kaihei.esportingplus.user.api.params.UserSingeRoleQueryParams;
import com.kaihei.esportingplus.user.api.vo.CareerChangeVo;
import com.kaihei.esportingplus.user.api.vo.CertRoleWithJoinRaidVo;
import com.kaihei.esportingplus.user.api.vo.SecondRaidVo;
import com.kaihei.esportingplus.user.api.vo.UserGameAboardVo;
import com.kaihei.esportingplus.user.api.vo.UserGameBaseRoleInfoVo;
import com.kaihei.esportingplus.user.api.vo.UserGameDetailRoleInfoVo;
import com.kaihei.esportingplus.user.api.vo.UserGameRoleSimpleVo;
import com.kaihei.esportingplus.user.api.vo.UserGameUserCredentialVo;
import com.kaihei.esportingplus.user.api.vo.UserSingleRoleDetailInfoVo;
import com.kaihei.esportingplus.user.assembler.UserGameRoleAssembler;
import com.kaihei.esportingplus.user.data.repository.UserGameRoleRepository;
import com.kaihei.esportingplus.user.domain.entity.UserGameRole;
import com.kaihei.esportingplus.user.domain.service.UserConfigService;
import com.kaihei.esportingplus.user.domain.service.UserGameService;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

/**
 * @author zhangfang
 */
@Service
public class UserGameServiceImpl implements UserGameService {

    private Logger logger = LoggerFactory.getLogger(UserGameServiceImpl.class);
    private static final int ROLE_CREATE_THRESHOLD = 3;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserGameRoleRepository userGameRoleRepository;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private ResourceServiceClient resourceServiceClient;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    SnowFlakeConfig snowFlakeConfig;

    /**
     * 获取游戏角色基础信息
     *
     * @param userId 用户Id
     * @param gameCode 游戏Id
     */
    @Override
    public List<UserGameBaseRoleInfoVo> getUserAllBaseRoles(String userId, Integer gameCode) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, userId, gameCode);
        List<UserGameRole> userGameRoles = userGameRoleRepository
                .selectByUserIdAndGameId(userId, gameCode);
        return UserGameRoleAssembler.convertToBaseInfoVoList(userGameRoles);
    }

    /**
     * 获取游戏角色基础信息
     *
     * @param userId 用户Id
     * @param gameCode 游戏Id
     * @param params 附带的过滤条件
     */
    @Override
    public List<UserGameDetailRoleInfoVo> getAllUserGameRoles(String userId, Integer gameCode,
            UserGameQueryBaseParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, userId, gameCode);
        List<UserGameRole> userGameRoles = userGameRoleRepository
                .selectByUserIdAndGameIdWithCondition(userId, gameCode, params);
        List<UserGameDetailRoleInfoVo> voList = new ArrayList<>();
        if (ObjectTools.isEmpty(userGameRoles)) {
            return voList;
        }
        userGameRoles.forEach(ugr -> {
            //在缓存中查询出认证角色
            UserGameDetailRoleInfoVo detailRoleInfoVo = userConfigService
                    .getUserRoleDetailRoleInfo(
                            String.format(RedisKey.USER_GAME_ROLE_CERTS_KEY, userId, gameCode),
                            ugr.getId().toString());
            voList.add(UserGameRoleAssembler.convertToDetailsInfoVoList(ugr, detailRoleInfoVo));

        });
        return voList;
    }

    /**
     * 上车时由车队服务调用，用户判断此角色能否加入这个副本,
     *
     * 如果是老板上车，则此角色在该副本必须没有认证、 如果是暴鸡上车，则此角色在此副本中一定要经过认证
     */
    @Override
    public UserSingleRoleDetailInfoVo getUserIdentityRoleInfo(UserSingeRoleQueryParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        //1：通过角色id查询出对应的角色
        UserGameRole gameRole = userGameRoleRepository.selectByPrimaryKey(params.getRoleId());
        ValidateAssert.hasNotNull(BizExceptionEnum.USER_GAME_ROLE_NOT_EXIST, gameRole);
        ValidateAssert.isTrue(gameRole.getUid().equals(params.getUid()),
                BizExceptionEnum.OPERATE_PERSON_DIFFER);
        //2：通过副本获取认证副本并尝试从缓存中加载认证角色
        final Integer certRaidCode = getCertRaidCodeByRaidCode(params.getGameCode(),
                params.getRaidCode());
        UserGameDetailRoleInfoVo detailRoleInfoVo = userConfigService.getUserRoleDetailRoleInfo(
                String.format(RedisKey.USER_GAME_ROLE_CERTS_KEY, params.getUid(),
                        params.getGameCode()), gameRole.getId().toString());
        //用于过滤副本认证角色的list
        UserGameUserCredentialVo certRaidVo = null;
        //TODO
        if (ObjectTools.isNotNull(detailRoleInfoVo) && ObjectTools
                .isNotEmpty(detailRoleInfoVo.getCredentials())) {
            certRaidVo = detailRoleInfoVo.getCredentials().stream()
                    .filter(uc -> uc.getRaidCode().equals(certRaidCode))
                    .findFirst().orElse(null);
        }
        //根据身份信息和副本认证组装返回对象
        return buildRoleCredentialInfo(gameRole, certRaidVo, params.getUserIdentity());

    }

    private UserSingleRoleDetailInfoVo buildRoleCredentialInfo(UserGameRole gameRole,
            UserGameUserCredentialVo certRaidVo, int userIdentity) {
        UserSingleRoleDetailInfoVo vo = new UserSingleRoleDetailInfoVo();
        //2：根据身份组装对应的角色信息，选择
        if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
            //如果是老板,则必须要该副本对应的认证角色为空
            ValidateAssert.isTrue(ObjectTools.isEmpty(certRaidVo),
                    BizExceptionEnum.USER_GAME_ROLE_IS_NOT_BOSS);
            BeanUtils.copyProperties(gameRole, vo);
        } else {
            //如果选择的是暴鸡，则该副本的认证角色一定不能为空
            ValidateAssert.isTrue(ObjectTools.isNotEmpty(certRaidVo),
                    BizExceptionEnum.USER_ROLE_NOT_CERT);
            BeanUtils.copyProperties(gameRole, vo);
            vo.setCredential(certRaidVo);
        }
        return vo;
    }

    @Override
    public List<UserGameDetailRoleInfoVo> getUserCredentialsRoles(String userId, Integer gameCode,
            UserGameRaidCodeQueryParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, userId, gameCode);
        //通关条件过滤出用户游戏角色
        List<UserGameRole> userGameRoles = userGameRoleRepository
                .selectByUserIdAndGameIdWithCondition(userId, gameCode, params);
        List<UserGameDetailRoleInfoVo> voList = new ArrayList<>();
        //如果传入了副本code，则说明需要通过副本code过滤
        //TODO
        Integer certRaidCode = null;
        if (params != null && ObjectTools.isNotEmpty(params.getRaidCode())) {
            certRaidCode = getCertRaidCodeByRaidCode(gameCode,
                    params.getRaidCode());
        }
        if (ObjectTools.isEmpty(userGameRoles)) {
            return voList;
        }
        final Integer finalCertRaidCode = certRaidCode;
        userGameRoles.forEach(ugr -> {
            //获取游戏角色认证
            UserGameDetailRoleInfoVo detailRoleInfoVo = userConfigService.getUserRoleDetailRoleInfo(
                    String.format(RedisKey.USER_GAME_ROLE_CERTS_KEY, userId, gameCode),
                    ugr.getId().toString());
            //因为是获取认证的用户角色，不符合条件的剔除，首先判断是否为空,只有不为空才加入
            if (detailRoleInfoVo == null || ObjectTools
                    .isEmpty(detailRoleInfoVo.getCredentials())) {
                return;
            }
            //其次判断是否通过副本Code去过滤，如果过滤，则没有此Code的角色剔除
            if (finalCertRaidCode == null) {
                voList.add(UserGameRoleAssembler
                        .convertToDetailsInfoVoList(ugr, detailRoleInfoVo));
                return;
            }

            List<UserGameUserCredentialVo> filterVoList = detailRoleInfoVo
                    .getCredentials().stream()
                    .filter(u -> u.getRaidCode().equals(finalCertRaidCode)).collect(
                            Collectors.toList());
            //为空跳过，循环下一个
            if (filterVoList == null || filterVoList.isEmpty()) {
                return;
            }
            voList.add(UserGameRoleAssembler
                    .convertToDetailsInfoVoList(ugr, filterVoList));

        });

        return voList;
    }

    /**
     * 创建车队时调用，用户显示该暴鸡可以创建哪些副本
     */
    @Override
    public List<CertRoleWithJoinRaidVo> getUserCredentialsAndRaidRoles(String userId,
            Integer gameCode) {
        //首先获取该用户所有的认证角色
        List<UserGameDetailRoleInfoVo> userCredentialsRoles = getUserCredentialsRoles(userId,
                gameCode, null);
        List<CertRoleWithJoinRaidVo> list = new ArrayList<>();
        //TODO 因为游戏可供认证的副本很少，为降低循环查询，本地缓存一份,key 是认证副本code，value是该认证副本对应的可加入副本
        Map<Integer, List<RedisGameRaid>> raidsMap = new HashMap<>();
        for (UserGameDetailRoleInfoVo roleInfoVo : userCredentialsRoles) {
            CertRoleWithJoinRaidVo vo = BeanMapper.map(roleInfoVo, CertRoleWithJoinRaidVo.class);
            //单个角色认证的角色对应副本map,Key是认证副本code value是该认证副本对应的可加入副本
            Map<Integer, List<RedisGameRaid>> singleRoleRaidMap = new HashMap<>();
            List<UserGameUserCredentialVo> credentials = roleInfoVo.getCredentials();
            //认证副本找到对应的可加入副本
            for (UserGameUserCredentialVo credentialVo : credentials) {
                List<RedisGameRaid> redisGameRaids = raidsMap.get(credentialVo.getRaidCode());
                if (redisGameRaids == null) {
                    redisGameRaids = userConfigService
                            .getGameRaidThroughCertCode(gameCode, credentialVo.getRaidCode());
                    raidsMap.put(credentialVo.getRaidCode(), redisGameRaids);
                }
                singleRoleRaidMap
                        .put(credentialVo.getRaidCode(), redisGameRaids);
            }
            List<SecondRaidVo> roleRaidList = singleRoleRaidMap.values().stream()
                    .flatMap(vl -> vl.stream()).map(redisGameRaid ->
                            BeanMapper.map(redisGameRaid, SecondRaidVo.class)
                    ).collect(Collectors.toList());
            vo.setRaids(roleRaidList);
            list.add(vo);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserGameRole(String userId, UserGameRoleSimpleVo vo) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, userId, vo);
        String thresholdKey = String.format(RedisKey.USER_CREATE_ROLE_THRESHOLD_KEY, userId);
        Boolean exists = cacheManager.exists(thresholdKey);
        ValidateAssert.isFalse(exists, BizExceptionEnum.USER_ROLE_CREATE_FAST);
        cacheManager.set(thresholdKey, com.kaihei.esportingplus.common.tools.StringUtils.EMPTY,
                ROLE_CREATE_THRESHOLD);
        String[] careerCodes = vo.getCareerCode().split(",");
        ValidateAssert.isTrue(careerCodes.length == 2, BizExceptionEnum.PARAM_VALID_FAIL);
        //1:通过游戏小区拿到对应跨区大区
        RedisSmallZoneRefAcrossZone zone = userConfigService
                .getBigAndAcrossZoneBySmallCode(vo.getGameCode(), vo.getZoneSmallCode());
        ValidateAssert.hasNotNull(BizExceptionEnum.ZONE_SERVER_NOT_MATCH, zone);
        //校验是否对应的是此大区
        ValidateAssert.isTrue(vo.getZoneBigCode().equals(zone.getZoneBigCode()),
                BizExceptionEnum.ZONE_SERVER_NOT_MATCH);

        //2:同样的，需要校验职业
        ResponsePacket<RedisTopCareer> careerResponse = resourceServiceClient
                .getCareerByGameCodeAndTopCareerCode(vo.getGameCode(),
                        Integer.valueOf(careerCodes[0]));
        ValidateAssert.isTrue(careerResponse.responseSuccess(), BizExceptionEnum.HYSTRIX_SERVER);
        RedisTopCareer topCareer = careerResponse.getData();
        ValidateAssert
                .hasNotNull(BizExceptionEnum.GAME_CAREER_NOT_MATCH, topCareer);
        List<RedisAwakeningCareer> awakeningAareer = topCareer.getAwakeningCareer();
        ValidateAssert.hasNotNull(BizExceptionEnum.GAME_CAREER_NOT_MATCH, awakeningAareer);
        //通过反射拿到对应的code name icon
        CareerChangeVo matchCareer = getMatchCareer(topCareer.getAwakeningCareer(), careerCodes[1]);
        ValidateAssert.hasNotNull(
                BizExceptionEnum.GAME_CAREER_NOT_MATCH, matchCareer);
        //上面校验结束
        UserGameRole gameRole = BeanMapper.map(vo, UserGameRole.class);
        gameRole.setUid(userId);
        gameRole.setAvatar(matchCareer.getCareerIcon());
        gameRole.setCareerName(new StringBuilder().append(topCareer.getCareerName()).append(",")
                .append(matchCareer.getCareerName()).toString());
        gameRole.setZoneBigName(zone.getZoneBigName());
        String smallZoneName = userConfigService
                .getSmallZoneName(vo.getGameCode(), vo.getZoneSmallCode());
        gameRole.setZoneSmallName(smallZoneName);
        userGameRoleRepository.insertSelective(gameRole);
    }

    /**
     * 根据次级职业code，找到对应的name和icon，只有在有匹配到时，返回对象才不为空
     */
    private CareerChangeVo getMatchCareer(List<RedisAwakeningCareer> careers, String matchCode) {
        if (ObjectTools.isEmpty(careers)) {
            return null;
        }

        Class<RedisAwakeningCareer> awakeningCareerClass = RedisAwakeningCareer.class;
        Field[] fields = awakeningCareerClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().endsWith("Code")) {
                String prefix = field.getName()
                        .substring(0, field.getName().lastIndexOf("Code"));
                Method getCodemethod = ReflectionUtils.findMethod(awakeningCareerClass,
                        "get" + prefix.substring(0, 1).toUpperCase() + prefix.substring(1)
                                + "Code");

                if (getCodemethod != null) {
                    for (RedisAwakeningCareer ak : careers) {
                        try {
                            Object invoke = getCodemethod.invoke(ak);
                            if (matchCode.equals(invoke == null ? null : invoke.toString())) {
                                //匹配则找到对应的name和icon
                                Method getNameMethod = ReflectionUtils
                                        .findMethod(awakeningCareerClass,
                                                "get" + prefix.substring(0, 1).toUpperCase()
                                                        + prefix.substring(1)
                                                        + "Name");
                                Method getIconMethod = ReflectionUtils
                                        .findMethod(awakeningCareerClass,
                                                "get" + prefix.substring(0, 1).toUpperCase()
                                                        + prefix.substring(1)
                                                        + "Icon");
                                Object nameValue =
                                        getNameMethod == null ? null : getNameMethod.invoke(ak);
                                Object iconValue =
                                        getIconMethod == null ? null : getIconMethod.invoke(ak);
                                CareerChangeVo vo = new CareerChangeVo();
                                vo.setCareerCode(matchCode);
                                vo.setCareerName(nameValue == null ? null : nameValue.toString());
                                vo.setCareerIcon(iconValue == null ? null : iconValue.toString());
                                return vo;
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }

                    }
                }

            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserGameRole(String userId, Long roleId) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, userId, roleId);
        //查找角色
        UserGameRole gameRole = userGameRoleRepository.selectByPrimaryKey(roleId);
        ValidateAssert.allNotNull(BizExceptionEnum.USER_GAME_ROLE_NOT_EXIST, gameRole);
        ValidateAssert
                .isTrue(userId.equals(gameRole.getUid()), BizExceptionEnum.OPERATE_PERSON_DIFFER);

        // 再去检查该角色是否认证，如果还没取消认证不允许删除
        UserGameRaidCodeQueryParams queryBaseParams = new UserGameRaidCodeQueryParams();
        queryBaseParams.setRoleId(roleId);
        List<UserGameDetailRoleInfoVo> userCredentialsRoles = getUserCredentialsRoles(userId,
                gameRole.getGameCode(), queryBaseParams);
        ValidateAssert.isFalse(userCredentialsRoles != null && !userCredentialsRoles.isEmpty(),
                BizExceptionEnum.USER_GAME_ROLE_CERT_NOT_ALLOW_DELETE);
        //现在，删除此角色
        userGameRoleRepository.deleteByPrimaryKey(roleId);
    }

    /**
     * 获取上车时可以使用的角色
     *
     * @param params 可选参数
     */
    @Override
    public List<UserGameAboardVo> getAboardGameRole(UserGameRoleAcrossQueryParams params) {
        List<UserGameAboardVo> list = new ArrayList<>();
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        //第一步，用跨区获取到对应的所有小区代码
        List<Integer> smallCodeList = userConfigService
                .findSmallCodeFromAcrossCode(params.getGameCode(), params.getZoneAcrossCode());
        //没有小区，认为有误，不会查询所有,直接返回
        if (ObjectTools.isEmpty(smallCodeList)) {
            return list;
        }
        //第二步，通过小区和其它参数，查询出用户在这个跨区下的所有角色
        UserGameRoleAcrossDataQueryParams queryParams = BeanMapper
                .map(params, UserGameRoleAcrossDataQueryParams.class);
        queryParams.setZoneSmallList(smallCodeList);
        //先通过条件在数据库中查找
        List<UserGameRole> userGameRoles = userGameRoleRepository
                .selectByUserIAndGameCodeAndSmallCodeList(queryParams);
        //
        if (ObjectTools.isEmpty(userGameRoles)) {
            return list;
        }
        //第三步 为避免角色太多，查询太多次缓存，这里设计一次性加载此用户此游戏所有角色
        Map<String, UserGameDetailRoleInfoVo> cacheUserGameRole = userConfigService
                .getUserAllRoleDetailRoleInfo(
                        String.format(RedisKey.USER_GAME_ROLE_CERTS_KEY, params.getUid(),
                                params.getGameCode()));
        //如果缓存为空且是老板上车判定,加入，如果选择的是暴鸡角色，则不加入
        if (ObjectTools.isEmpty(cacheUserGameRole)
                && params.getUserIdentity().intValue() == UserIdentityEnum.BOSS.getCode()) {
            userGameRoles.forEach(ugr -> {
                list.add(createUserGameAboardVo(ugr, null));
            });
            return list;
        } else if (ObjectTools.isEmpty(cacheUserGameRole)
                && params.getUserIdentity().intValue() == UserIdentityEnum.BAOJI.getCode()) {
            return list;
        }
        final Integer certRaidCode = getCertRaidCodeByRaidCode(params.getGameCode(),
                params.getRaidCode());
        //下面的情况，缓存不为空，通过角色去判定
        userGameRoles.forEach(ugr -> {
            UserGameDetailRoleInfoVo detailRoleInfoVo = cacheUserGameRole
                    .get(ugr.getId().toString());
            //如果此角色认证信息为空，则只有老板才加入
            if (detailRoleInfoVo == null || ObjectTools
                    .isEmpty(detailRoleInfoVo.getCredentials())) {
                if (params.getUserIdentity().intValue() == UserIdentityEnum.BOSS.getCode()) {
                    list.add(createUserGameAboardVo(ugr, null));
                }
                return;
            }
            //通过副本Code去过滤，没有此Code的角色剔除
            List<UserGameUserCredentialVo> filterVoList = detailRoleInfoVo.getCredentials().stream()
                    .filter(u -> u.getRaidCode().equals(certRaidCode)).collect(
                            Collectors.toList());
            //如果此角色在此副本中没有认证，只有选择老板身份才加入
            if (ObjectTools.isEmpty(filterVoList)) {
                if (params.getUserIdentity().intValue() == UserIdentityEnum.BOSS.getCode()) {
                    list.add(createUserGameAboardVo(ugr, null));
                }
                return;
            }
            //如果此角色在此副本中有认证，只有选择暴鸡身份才加入
            if (params.getUserIdentity().intValue() == UserIdentityEnum.BAOJI.getCode()) {
                UserGameAboardVo userGameAboardVo = createUserGameAboardVo(ugr,
                        filterVoList.get(0).getBaojiLevel());
                userGameAboardVo.setRaidLocationCode(filterVoList.get(0).getRaidLocationCode());
                userGameAboardVo.setRaidLocationName(filterVoList.get(0).getRaidLocationName());
                list.add(userGameAboardVo);
            }


        });

        return list;
    }

    private UserGameAboardVo createUserGameAboardVo(UserGameRole ugr, Integer baojiLevel) {
        UserGameAboardVo vo = new UserGameAboardVo();
        vo.setRoleId(ugr.getId());
        vo.setRoleName(ugr.getName());
        vo.setBaojiLevel(baojiLevel);
        String[] careerCodes = ugr.getCareerCode().split(",");
        vo.setCareerCode(Integer.valueOf(careerCodes[careerCodes.length - 1]));
        String[] careerNames = ugr.getCareerName().split(",");
        vo.setCareerName(careerNames[careerNames.length - 1]);
        return vo;
    }

    private Integer getCertRaidCodeByRaidCode(Integer gameCode, Integer raidCode) {
        //如果副本code过滤不为空，则查找出认证的副本code
        RedisGameRaid redisGameRaid = userConfigService
                .getGameRaid(gameCode, raidCode);
        return redisGameRaid.getCertRaidCode();
    }
}
