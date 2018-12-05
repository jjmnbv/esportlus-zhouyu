package com.kaihei.esportingplus.resource.domain.service.freeteam.impl;

import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.enums.FreeTeamTypeSceneEnum;
import com.kaihei.esportingplus.api.params.BaojiCertifyParams;
import com.kaihei.esportingplus.api.params.freeteam.FreeTeamTypeAppQueryParams;
import com.kaihei.esportingplus.api.vo.BaojiCertInfo;
import com.kaihei.esportingplus.api.vo.BaojiCertifyResult;
import com.kaihei.esportingplus.api.vo.BaojiDanRangeVO;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.GameDictVO;
import com.kaihei.esportingplus.api.vo.SettlementModeVO;
import com.kaihei.esportingplus.api.vo.freeteam.DanDictVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeSimpleVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeVO;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.enums.StatusEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.HanyuPinyinUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.resource.data.manager.PythonSupportService;
import com.kaihei.esportingplus.resource.data.manager.dao.DictionaryCategoryDAO;
import com.kaihei.esportingplus.resource.data.manager.dao.DictionaryDAO;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import com.kaihei.esportingplus.resource.domain.service.BaojiDanRangeService;
import com.kaihei.esportingplus.resource.domain.service.freeteam.FreeTeamTypeService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liangyi
 */
@Service("freeTeamTypeService")
public class FreeTeamTypeServiceImpl implements FreeTeamTypeService {

    @Autowired
    DictionaryDAO dictionaryDAO;
    @Autowired
    DictionaryCategoryDAO dictionaryCategoryDAO;
    @Autowired
    DictionaryDictManager dictManager;
    @Autowired
    BaojiDanRangeService baojiDanRangeService;
    @Autowired
    PythonSupportService pythonSupportService;

    private static final Logger log = LoggerFactory.getLogger(FreeTeamTypeServiceImpl.class);

    /*
     * 2018-11-01 15:30 新增需求, 后台的结算类型需要固定不可更改
     * 选择局: 固定为 1局; 选择小时: 固定为 0.5小时
     */
    /**
     * 按局结算, 默认为 1局
     */
    private static final double SETTLEMENT_ROUND_NUMBER = 1;

    /**
     * 按小时结算, 默认为 0.5小时
     */
    private static final double SETTLEMENT_HOUR_NUMBER = 0.5;

    private FreeTeamTypeDetailVO buildFreeTeamTypeDetailVO(DictBaseVO<FreeTeamTypeVO> dictBaseVO) {

        FreeTeamTypeVO freeTeamTypeVO = dictBaseVO.getValue();

        FreeTeamTypeDetailVO detailVO = BeanMapper.map(freeTeamTypeVO,
                FreeTeamTypeDetailVO.class);

        detailVO.setFreeTeamTypeId(dictBaseVO.getDictId());
        detailVO.setName(dictBaseVO.getName());
        detailVO.setStatus(dictBaseVO.getStatus().intValue());
        detailVO.setOrderIndex(dictBaseVO.getOrderIndex());

        GameDictVO game = buildGame(freeTeamTypeVO);
        detailVO.setGame(game);
        // 游戏区
        List<DictBaseVO> gameZoneList = buildGameZone(freeTeamTypeVO.getGameZoneIdList());
        detailVO.setGameZoneList(gameZoneList);
        // 游戏段位
        List<DanDictVo> gameDanList = buildGameDan(freeTeamTypeVO.getGameDanIdList());
        detailVO.setGameDanList(gameDanList);
        // 结算类型
        List<SettlementModeVO> settlementModeList = bulidTeamSettlementMode(freeTeamTypeVO);
        detailVO.setSettlementModeList(settlementModeList);
        return detailVO;
    }

    @Override
    public FreeTeamTypeDetailVO getByFreeTeamTypeId(Integer freeTeamTypeId) {
        DictBaseVO<FreeTeamTypeVO> dictBaseVO = dictionaryDAO
                .buildDictBase(freeTeamTypeId, FreeTeamTypeVO.class);
        // 查询免费车队类型主表
        FreeTeamTypeDetailVO detailVO;
        try {
            detailVO = buildFreeTeamTypeDetailVO(dictBaseVO);
        } catch (Exception e) {
            // id错误无法转换直接抛出异常
            throw new BusinessException(BizExceptionEnum.FREE_TEAM_TYPE_NOT_FOUND,
                    String.valueOf(freeTeamTypeId));
        }
        if (Objects.isNull(detailVO)) {
            log.error("未查询到id为[{}]的免费车队类型", detailVO);
            throw new BusinessException(BizExceptionEnum.FREE_TEAM_TYPE_NOT_FOUND,
                    String.valueOf(freeTeamTypeId));
        }
        return detailVO;
    }

    @Override
    public FreeTeamTypeDetailVO getFreeTeamTypeDetail(
            FreeTeamTypeAppQueryParams freeTeamTypeAppQueryParams) {
        log.info("APP调用获取免费车队类型详情, 参数: {}", freeTeamTypeAppQueryParams);
        String uid = UserSessionContext.getUser().getUid();
        Integer freeTeamTypeId = freeTeamTypeAppQueryParams.getFreeTeamTypeId();
        Integer userIdentity = freeTeamTypeAppQueryParams.getUserIdentity();
        Integer freeTeamTypeScene = freeTeamTypeAppQueryParams.getFreeTeamTypeScene();
        FreeTeamTypeDetailVO detailVO = getByFreeTeamTypeId(freeTeamTypeId);
        // 判断这个车队类型的可组建身份是否符合前端传递的身份
        Integer identityCode = detailVO.getBaojiIdentity();
        if (FreeTeamTypeSceneEnum.CREATE_TEAM.getCode() == freeTeamTypeScene
                && identityCode != UserIdentityEnum.BJ_BN.getCode()
                && !identityCode.equals(userIdentity)) {
            // 组建免费车队时, 不是暴鸡暴娘均可的类型, 并且身份不符合该车队可组建的身份
            log.error("APP获取车队详情, 该用户身份:[{}]不符合该车队类型[id:{}]的要求",
                    userIdentity, freeTeamTypeId);
            throw new BusinessException(BizExceptionEnum.BAOJI_IDENTITY_NOT_MATCH_FREE_TEAM);
        }
        if (FreeTeamTypeSceneEnum.JOIN_TEAM.getCode() == freeTeamTypeScene
                || UserIdentityEnum.BOSS.getCode() == userIdentity) {
            // 老板或加入车队的场景直接返回数据, 不做任何处理
            // 按权重排序升序--给 APP用的降序排, APP少改代码逻辑
            detailVO.getGameDanList()
                    .sort(Comparator.comparingInt(DanDictVo::getOrderIndex).reversed());
            return detailVO;
        }
        intersectDanByUser(uid, userIdentity, detailVO);
        return detailVO;
    }

    private void intersectDanByUser(String uid, Integer baojiIdentity,
            FreeTeamTypeDetailVO detailVO) {
        UserIdentityEnum userIdentityEnum =
                UserIdentityEnum.getByCode(baojiIdentity.byteValue());
        BaojiCertifyParams baojiCertifyParams = new BaojiCertifyParams.Builder()
                .uid(uid).identity(userIdentityEnum.getPythonCode()).build();
        BaojiCertifyResult baojiCertifyResult =
                pythonSupportService.getBaojiCertifyInfo(baojiCertifyParams);
        if (ObjectTools.isNotNull(baojiCertifyResult)) {
            List<BaojiCertInfo> certInfo = baojiCertifyResult.getCertInfo();
            if (ObjectTools.isNotEmpty(certInfo)) {
                for (BaojiCertInfo baojiCertInfo : certInfo) {
                    String gameCode = detailVO.getGame().getCode();
                    // 如果是同一个游戏, 这里需要保证双方的游戏 code 是一致的
                    if (gameCode.equals(String.valueOf(baojiCertInfo.getGameCode()))) {
                        Integer baojiLevel = baojiCertInfo.getBaojiLevel();
                        log.info("APP端获取免费车队类型详情, 准备获取接单段位交集!"
                                + "游戏code:[{}], 暴鸡等级code:[{}]", gameCode, baojiLevel);
                        // 查询暴鸡接单范围
                        BaojiDanRangeVO baojiDanRangeVO = baojiDanRangeService
                                .getBaojiDanRangeByGameAndLevel(
                                        detailVO.getGame().getDictId(), baojiLevel);
                        // 取段位交集
                        List<DanDictVo> intersectDan = intersectDan(detailVO.getGameDanList(),
                                baojiDanRangeVO.getUpperDan(), baojiDanRangeVO.getLowerDan());
                        detailVO.setGameDanList(intersectDan);
                    }
                }
            }
        }
    }

    private List<DanDictVo> intersectDan(List<DanDictVo> gameDanList,
            DanDictVo upperDan, DanDictVo lowerDan) {
        List<DanDictVo> intersectionDanList = new ArrayList<>();
        if (ObjectTools.isNotEmpty(gameDanList)) {
            for (DanDictVo danDictVo : gameDanList) {
                Integer orderIndex = danDictVo.getOrderIndex();
                if (orderIndex >= lowerDan.getOrderIndex()
                        && orderIndex <= upperDan.getOrderIndex()) {
                    // 取交集
                    intersectionDanList.add(danDictVo);
                }
            }
        }
        // 按权重排序升序--给 APP用的降序排, APP少改代码逻辑
        intersectionDanList.sort(Comparator.comparingInt(DanDictVo::getOrderIndex).reversed());
        return intersectionDanList;
    }

    @Override
    public void addFreeTeamType(FreeTeamTypeVO freeTeamTypeVO) {
        log.info("新增免费车队类型, 参数: {}", freeTeamTypeVO);
        Dictionary dictionary = buildFreeTeamTypeDictionary(freeTeamTypeVO);
        dictionaryDAO.insert(dictionary);
    }

    private Dictionary buildFreeTeamTypeDictionary(FreeTeamTypeVO freeTeamTypeVO) {
        GameDictVO game = buildGame(freeTeamTypeVO);
        Dictionary dictionary = new Dictionary();
        String dictName = game.getName();
        if (ObjectTools.isNotEmpty(freeTeamTypeVO.getSubtitle())) {
            dictName += "-"+ freeTeamTypeVO.getSubtitle();
        }
        dictionary.setName(dictName);
        String freeTeamTypeCode = DictionaryCategoryCodeEnum.FREE_TEAM_TYPE.getCode();
        dictionary.setCode(freeTeamTypeCode+"_"+HanyuPinyinUtils.getFirstLettersLo(dictName));
        dictionary.setStatus(freeTeamTypeVO.getStatus().byteValue());
        dictionary.setOrderIndex(freeTeamTypeVO.getOrderIndex());
        DictionaryCategory dictCategory = dictionaryCategoryDAO.findByCode(freeTeamTypeCode);
        dictionary.setCategoryId(dictCategory.getId());
        // value 里面将这三个字段置空
        freeTeamTypeVO.setFreeTeamTypeId(null);
        freeTeamTypeVO.setOrderIndex(null);
        freeTeamTypeVO.setStatus(null);
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(freeTeamTypeVO));
        return dictionary;
    }

    /**
     * 免费车队与游戏关联
     */
    private GameDictVO buildGame(FreeTeamTypeVO freeTeamTypeVO) {
        return dictionaryDAO
                .buildDictBase(freeTeamTypeVO.getGameId(), GameDictVO.class).getValue();
    }


    /**
     * 免费车队结算方式
     * @param freeTeamTypeVO
     */
    private List<SettlementModeVO> bulidTeamSettlementMode(FreeTeamTypeVO freeTeamTypeVO) {

        List<SettlementModeVO> sms = new ArrayList<>();
        List<SettlementModeVO> settlementModeList = freeTeamTypeVO.getSettlementModeList();
        boolean isNullSettlementMode = true;
        for (SettlementModeVO settlementModeVO : settlementModeList) {
            Integer settlementType = settlementModeVO.getSettlementType();
            if (settlementType != null) {
                // 发现前端有时候这个 id传 null, 导致排位赛, 先处理一波 2018/11/27
                // 免费车队写死 1局, 0.5小时
                if (settlementType == SettlementTypeEnum.ROUND.getCode()) {
                    // 写死一局
                    settlementModeVO.setSettlementNumber(new BigDecimal(SETTLEMENT_ROUND_NUMBER));
                } else if (settlementType == SettlementTypeEnum.HOUR.getCode()) {
                    // 写死一小时
                    settlementModeVO.setSettlementNumber(new BigDecimal(SETTLEMENT_HOUR_NUMBER));
                }
                isNullSettlementMode = false;
                sms.add(settlementModeVO);
            }
        }

        // 如果结算类型全部是 null, 设置个默认值一局
        if (isNullSettlementMode) {
            SettlementModeVO settlementMode = SettlementModeVO.builder()
                    .settlementType(SettlementTypeEnum.ROUND.getCode())
                    .settlementNumber(new BigDecimal(SETTLEMENT_ROUND_NUMBER))
                    .build();
            sms.add(settlementMode);
        }
        return sms;
    }


    /**
     * 免费车队类型与游戏段位关联
     */
    private List<DanDictVo> buildGameDan(List<Integer> gameDanList) {
        if (ObjectTools.isNotEmpty(gameDanList)) {
            return gameDanList.stream()
                    .map(d -> dictionaryDAO.buildDictBase(d, DanDictVo.class).getValue())
                    // 升序排
                    .sorted(Comparator.comparingInt(DanDictVo::getOrderIndex))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 免费车队类型与游戏区关联
     */
    private List<DictBaseVO> buildGameZone(List<Integer> gameZoneList) {
        if (ObjectTools.isNotEmpty(gameZoneList)) {
            return gameZoneList.stream()
                    .map(d -> dictionaryDAO.buildDictBase(d, String.class))
                    // 升序排
                    .sorted(Comparator.comparingInt(DictBaseVO::getOrderIndex))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void updateFreeTeamType(FreeTeamTypeVO freeTeamTypeVO) {
        Integer freeTeamTypeId = freeTeamTypeVO.getFreeTeamTypeId();
        Dictionary dictionary = buildFreeTeamTypeDictionary(freeTeamTypeVO);
        dictionary.setId(freeTeamTypeId);
        dictionaryDAO.updateById(dictionary);
    }

    @Override
    public void deleteFreeTeamType(Integer freeTeamTypeId) {
        Dictionary dictionary = new Dictionary();
        dictionary.setStatus((byte)StatusEnum.DISABLE.getCode());
        dictionary.setId(freeTeamTypeId);
        dictionaryDAO.updateById(dictionary);
    }

    @Override
    public List<FreeTeamTypeSimpleVO> getSimpleByBaojiIdentity(
            FreeTeamTypeAppQueryParams freeTeamTypeAppQueryParams) {
        log.info("APP调用获取车队类型列表, 参数: {}", freeTeamTypeAppQueryParams);
        String uid = UserSessionContext.getUser().getUid();
        Integer userIdentity = freeTeamTypeAppQueryParams.getUserIdentity();
        Integer freeTeamTypeScene = freeTeamTypeAppQueryParams.getFreeTeamTypeScene();
        // 这个阶段是免费车队类型不会由前端传参, 先写死 2018/11/16
        List<FreeTeamTypeSimpleVO> simpleVOList = getAll();

        /*
         * 以下情况返回所有有效的车队类型
         * 1: 老板或暴鸡暴娘均可
         * 2: 暴鸡或暴娘上车
         * UserIdentityEnum.BJ_BN.getCode().equals(baojiIdentity.byteValue())
         */
        if (FreeTeamTypeSceneEnum.JOIN_TEAM.getCode() == freeTeamTypeScene
                || UserIdentityEnum.BOSS.getCode() == userIdentity) {
            // 按 orderIndex 升序排, 产品需求
            simpleVOList.sort(Comparator.comparingInt(FreeTeamTypeSimpleVO::getOrderIndex));
            return simpleVOList;
        }
        // 查询出暴鸡/暴娘的数据字典 id, 过滤
        simpleVOList = filterTypeByBaojiIdentity(userIdentity, simpleVOList);
        // 调用 Python 接口通过游戏过滤
        return filterTypeByGameCertify(uid, userIdentity, simpleVOList);
    }

    private List<FreeTeamTypeSimpleVO> filterTypeByBaojiIdentity(Integer baojiIdentity,
            List<FreeTeamTypeSimpleVO> simpleVOList) {
        return simpleVOList.stream()
                // 过滤出相同的可组建身份或者暴鸡暴娘均可的身份
                .filter(s -> s.getBaojiIdentity().equals(baojiIdentity)
                        || UserIdentityEnum.BJ_BN.getCode() == s.getBaojiIdentity())
                // 启用的
                .filter(s -> StatusEnum.ENABLE.getCode() == s.getStatus())
                .collect(Collectors.toList());

    }

    private List<FreeTeamTypeSimpleVO> filterTypeByGameCertify(String uid, Integer baojiIdentity,
            List<FreeTeamTypeSimpleVO> simpleVOList) {
        BaojiCertifyParams baojiCertifyParams = new BaojiCertifyParams.Builder().uid(uid)
                .identity(UserIdentityEnum.getByCode(baojiIdentity.byteValue()).getPythonCode())
                .build();
        BaojiCertifyResult baojiCertifyResult = pythonSupportService
                .getBaojiCertifyInfo(baojiCertifyParams);
        Set<FreeTeamTypeSimpleVO> actuallySet = new HashSet<>();
        if (ObjectTools.isNotNull(baojiCertifyResult)) {
            List<BaojiCertInfo> certInfo = baojiCertifyResult.getCertInfo();
            if (ObjectTools.isNotEmpty(certInfo)) {
                log.info("APP端获取免费车队类型列表,Python返回的游戏认证结果: {}", certInfo);
                for (BaojiCertInfo baojiCertInfo : certInfo) {
                    String gameCode = String.valueOf(baojiCertInfo.getGameCode());
                    // 如果是同一个游戏, 这里需要保证双方的游戏 code 是一致的
                    log.info("APP端获取免费车队类型列表, 过滤已认证的游戏,"
                            + " 暴鸡身份code:[{}], 游戏code:[{}]", baojiIdentity, gameCode);
                    for (FreeTeamTypeSimpleVO simpleVO : simpleVOList) {
                        // 如果两个游戏 code 相等则放入新的集合
                        if (simpleVO.getGame().getCode().equals(gameCode)) {
                            actuallySet.add(simpleVO);
                        }
                    }
                }
            }
        }
        List<FreeTeamTypeSimpleVO> actuallyList = new ArrayList<>(actuallySet);
        // 按 orderIndex 升序排, 产品需求
        actuallyList.sort(Comparator.comparingInt(FreeTeamTypeSimpleVO::getOrderIndex));
        return actuallyList;
    }

    @Override
    public List<FreeTeamTypeSimpleVO> getAll() {
        List<Dictionary> list = dictionaryDAO
                .findByCategoryCode(DictionaryCategoryCodeEnum.FREE_TEAM_TYPE.getCode());

        List<FreeTeamTypeSimpleVO> dictBaseVOS = list.stream()
                .map(d -> {
                    DictBaseVO<FreeTeamTypeVO> dictBaseVO = dictionaryDAO
                            .buildDictBase(d.getDictId(), FreeTeamTypeVO.class);
                    FreeTeamTypeVO freeTeamTypeVO = dictBaseVO.getValue();
                    FreeTeamTypeSimpleVO simpleVO = BeanMapper
                            .map(freeTeamTypeVO, FreeTeamTypeSimpleVO.class);
                    BeanMapper.map(dictBaseVO, simpleVO);
                    simpleVO.setFreeTeamTypeId(dictBaseVO.getDictId());
                    GameDictVO game = buildGame(freeTeamTypeVO);
                    simpleVO.setGameName(game.getName());
                    simpleVO.setGame(game);
                    return simpleVO;
                })
                .collect(Collectors.toList());

        return dictBaseVOS;
    }

    @Override
    public List<FreeTeamTypeDetailVO> getAllEnableFreeTeamType() {
        List<Dictionary> list = dictionaryDAO
                .findByCategoryCode(DictionaryCategoryCodeEnum.FREE_TEAM_TYPE.getCode());
        return list.stream()
                .filter(d -> StatusEnum.ENABLE.getCode() == d.getStatus())
                .map(d -> {
                    DictBaseVO<FreeTeamTypeVO> dictBaseVO = dictionaryDAO
                            .buildDictBase(d.getDictId(), FreeTeamTypeVO.class);
                    FreeTeamTypeDetailVO detailVO = buildFreeTeamTypeDetailVO(dictBaseVO);
                    return detailVO;
                })
                .collect(Collectors.toList());
    }
}
