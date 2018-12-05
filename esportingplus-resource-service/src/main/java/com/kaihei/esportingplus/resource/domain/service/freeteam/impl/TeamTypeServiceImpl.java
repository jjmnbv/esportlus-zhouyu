package com.kaihei.esportingplus.resource.domain.service.freeteam.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.enums.FreeTeamTypeSceneEnum;
import com.kaihei.esportingplus.api.params.BaojiCertifyParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamSettlementModeParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeAppQueryParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeBackendQueryParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeParams;
import com.kaihei.esportingplus.api.vo.BaojiCertInfo;
import com.kaihei.esportingplus.api.vo.BaojiCertifyResult;
import com.kaihei.esportingplus.api.vo.BaojiDanRangeVO;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.GameDictVO;
import com.kaihei.esportingplus.api.vo.freeteam.DanDictVo;
import com.kaihei.esportingplus.api.vo.freeteam.TeamSettlementModeVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeDetailVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeSimpleVO;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.enums.StatusEnum;
import com.kaihei.esportingplus.common.enums.TeamCategoryEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.resource.data.manager.FreeTeamCacheManager;
import com.kaihei.esportingplus.resource.data.manager.PythonSupportService;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.data.repository.teamtype.TeamSettlementModeRepository;
import com.kaihei.esportingplus.resource.data.repository.teamtype.TeamTypeRepository;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.TeamSettlementMode;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.TeamType;
import com.kaihei.esportingplus.resource.domain.service.BaojiDanRangeService;
import com.kaihei.esportingplus.resource.domain.service.freeteam.TeamTypeService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liangyi
 */
@Service("teamTypeService")
public class TeamTypeServiceImpl implements TeamTypeService {

    @Autowired
    TeamTypeRepository teamTypeRepository;
    @Autowired
    TeamSettlementModeRepository teamSettlementModeRepository;
    @Autowired
    DictionaryDictManager dictionaryDictManager;
    @Autowired
    BaojiDanRangeService baojiDanRangeService;
    @Autowired
    FreeTeamCacheManager freeTeamCacheManager;
    @Autowired
    PythonSupportService pythonSupportService;

    private static final Logger log = LoggerFactory.getLogger(TeamTypeServiceImpl.class);

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

    @Override
    public TeamTypeDetailVO getTypeById(Integer freeTeamTypeId) {
        // 查询免费车队类型主表
        TeamType teamType = teamTypeRepository.selectByPrimaryKey(freeTeamTypeId);
        if (Objects.isNull(teamType)) {
            log.error("未查询到id为[{}]的免费车队类型", teamType);
            throw new BusinessException(BizExceptionEnum.FREE_TEAM_TYPE_NOT_FOUND,
                    String.valueOf(freeTeamTypeId));
        }
        return buildFreeTeamTypeDetailVO(teamType);
    }

    private TeamTypeDetailVO buildFreeTeamTypeDetailVO(TeamType teamType) {
        // 游戏
        DictBaseVO<GameDictVO> gameDict = dictionaryDictManager.buildDictBase(
                teamType.getGameId(), GameDictVO.class);
        // 查询免费车队类型关联的游戏区
        List<Integer> gameZoneIds = teamTypeRepository.selectGameZone(
                teamType.getTeamTypeId());
        List<DictBaseVO> gameZone = buildDictBaseList(gameZoneIds, String.class);
        // 查询免费车队类型关联的游戏段位
        List<Integer> gameDanIds = teamTypeRepository.selectGameDan(
                teamType.getTeamTypeId());
        List<DanDictVo> gameDan = buildDictDanList(gameDanIds);
        // 玩法模式
        DictBaseVO playMode = dictionaryDictManager.buildDictBase(
                teamType.getPlayModeId(), String.class);
        // 结算方式
        List<TeamSettlementModeVO> settlementModeList
                = buildTeamSettlementModeVOList(teamType.getTeamTypeId());

        // 暴鸡身份
        DictBaseVO baojiIdentity = dictionaryDictManager.buildDictBase(
                teamType.getBaojiIdentityId(), String.class);
        // 组装 freeTeamTypeVO
        return TeamTypeDetailVO.builder()
                .teamTypeId(teamType.getTeamTypeId())
                .name(teamType.getName())
                .category(teamType.getCategory().intValue())
                .game(gameDict.getValue())
                .gameZoneList(gameZone)
                .gameDanList(gameDan)
                .playMode(playMode)
                .settlementModeList(settlementModeList)
                .baojiIdentity(baojiIdentity)
                .maxPositionCount(teamType.getMaxPositionCount())
                .status(teamType.getStatus().intValue())
                .orderIndex(teamType.getOrderIndex())
                .build();
    }

    private List<TeamSettlementModeVO> buildTeamSettlementModeVOList(Integer teamTypeId) {

        List<TeamSettlementMode> teamSettlementModeList
                = teamSettlementModeRepository.selectByTeamTypeId(teamTypeId);
        List<TeamSettlementModeVO> teamSettlementModeVOS = new ArrayList<>();

        for (TeamSettlementMode teamSettlementMode : teamSettlementModeList) {

            TeamSettlementModeVO teamSettlementModeVO = new TeamSettlementModeVO();
            BigDecimal settlementNumber = teamSettlementMode.getSettlementNumber();

            DictBaseVO settlement = dictionaryDictManager
                    .buildDictBase(teamSettlementMode.getSettlementTypeId(), String.class);

            teamSettlementModeVO.setSettlementNumber(settlementNumber);
            teamSettlementModeVO.setSettlementType(settlement);

            teamSettlementModeVOS.add(teamSettlementModeVO);
        }
        return teamSettlementModeVOS;
    }


    @Override
    public TeamTypeDetailVO getTypeDetail(TeamTypeAppQueryParams typeAppQueryParams) {
        log.info("APP调用获取免费车队类型详情, 参数: {}", typeAppQueryParams);
        String uid = UserSessionContext.getUser().getUid();
        Integer teamTypeId = typeAppQueryParams.getTeamTypeId();
        Integer userIdentity = typeAppQueryParams.getUserIdentity();
        Integer teamTypeScene = typeAppQueryParams.getTeamTypeScene();
        TeamTypeDetailVO detailVO = getTypeById(teamTypeId);
        // 判断这个车队类型的可组建身份是否符合前端传递的身份
        String identityCode = detailVO.getBaojiIdentity().getCode();
        if (FreeTeamTypeSceneEnum.CREATE_TEAM.getCode() == teamTypeScene
                && !identityCode.equals(String.valueOf(UserIdentityEnum.BJ_BN.getCode()))
                && !identityCode.equals(String.valueOf(userIdentity))) {
            // 组建免费车队时, 不是暴鸡暴娘均可的类型, 并且身份不符合该车队可组建的身份
            log.error("APP获取车队详情, 该用户身份:[{}]不符合该车队类型[id:{}]的要求",
                    userIdentity, teamTypeId);
            throw new BusinessException(BizExceptionEnum.BAOJI_IDENTITY_NOT_MATCH_FREE_TEAM);
        }
        //  仅当暴鸡组建车队时, 才需要过滤段位范围, 取交集 --暴鸡上车这个版本还没需求, 待定 todo
        /*  2018-10-25 15:15需求重新确认,
            由于免费车队的暴鸡可以创建陪玩车队,
            暴娘可以创建上分车队,
            因此都需要过滤段位范围
        */
        /*  2018-10-31 11:08需求重新确认,
            陪玩车队段位返回空数组
        */
        /*String playModeCode = detailVO.getPlayMode().getCode();
        if (DictionaryCodeEnum.PLAY_MODE_PLAY.getCode().equals(playModeCode)) {
            // 陪玩--段位返回空数组
            detailVO.setGameDanList(new ArrayList<>());
        } else if (DictionaryCodeEnum.PLAY_MODE_POINTS.getCode().equals(playModeCode)) {
            // 上分--如果是创建车队过滤段位范围
            if (TeamTypeSceneEnum.CREATE_TEAM.getCode() == teamTypeScene) {
                intersectDanByUser(uid, userIdentity, detailVO);
            }
        }*/

        /*  2018-11-16 免费车队第一次重构
            陪玩车队也需要选段位(免费车队只有陪玩模式)
        */
        if (FreeTeamTypeSceneEnum.JOIN_TEAM.getCode() == teamTypeScene
                || UserIdentityEnum.BOSS.getCode() == userIdentity) {
            // 老板或加入车队的场景直接返回数据, 不做任何处理
            return detailVO;
        }
        intersectDanByUser(uid, userIdentity, detailVO);
        return detailVO;
    }

    private void intersectDanByUser(String uid, Integer baojiIdentity,
            TeamTypeDetailVO detailVO) {
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
                        Integer baojiLevelCode = baojiCertInfo.getBaojiLevel();
                        log.info("APP端获取免费车队类型详情, 准备获取接单段位交集!"
                                + "游戏code:[{}], 暴鸡等级code:[{}]", gameCode, baojiLevelCode);
                        // 查询暴鸡接单范围
                        BaojiDanRangeVO baojiDanRangeVO = baojiDanRangeService
                                .getBaojiDanRangeByGameAndLevel(
                                        detailVO.getGame().getDictId(), baojiLevelCode);
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
            // 按权重排序倒序
            gameDanList.sort((d1, d2) -> d2.getOrderIndex() - d1.getOrderIndex());
            for (DanDictVo danDictVo : gameDanList) {
                Integer orderIndex = danDictVo.getOrderIndex();
                if (orderIndex >= lowerDan.getOrderIndex()
                        && orderIndex <= upperDan.getOrderIndex()) {
                    // 取交集
                    intersectionDanList.add(danDictVo);
                }
            }
        }
        return intersectionDanList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTeamType(TeamTypeParams teamTypeParams) {
        log.info("新增免费车队类型, 参数: {}", teamTypeParams);
        TeamType teamType = BeanMapper.map(teamTypeParams, TeamType.class);
        // 根据结算类型固定结算数量 2018-11-01 15:30 新增需求
        // setSettlementNumberByType(teamType);
        teamType.setStatus(teamTypeParams.getStatus().byteValue());
        teamTypeRepository.insertSelective(teamType);
        Integer teamTypeId = teamType.getTeamTypeId();

        // 结算类型
        addTeamSettlementMode(teamTypeParams, teamTypeId);
        // 游戏区
        List<Integer> gameZoneList = teamTypeParams.getGameZoneIdList();
        addRelatedGameZone(gameZoneList, teamTypeId);
        // 游戏段位
        List<Integer> gameDanList = teamTypeParams.getGameDanIdList();
        addRelatedGameDan(gameDanList, teamTypeId);
        // 缓存添加, 仅当上架时才加入到 redis 中, 2018/11/16免费车队第一次重构, 不加入 redis 了
        /*if (teamType.getStatus() == StatusEnum.ENABLE.getCode()) {
            TeamTypeDetailVO typeDetailVO = getTypeById(teamTypeId);
            freeTeamCacheManager.addOrUpdateFreeTeamType(typeDetailVO);
        }*/
    }

    /**
     * 新增车队结算方式
     * @param teamTypeParams
     * @param teamTypeId 车队类型 id
     */
    private void addTeamSettlementMode(TeamTypeParams teamTypeParams, Integer teamTypeId) {

        List<TeamSettlementModeParams> settlementModeList = teamTypeParams.getSettlementModeList();
        boolean isNullSettlementMode = true;
        for (TeamSettlementModeParams settlementModeParams : settlementModeList) {
            TeamSettlementMode settlementMode =
                    BeanMapper.map(settlementModeParams, TeamSettlementMode.class);
            settlementMode.setTeamTypeId(teamTypeId);

            // --但我觉得应该会带着脑子来上班, 这个版本先不处理 2018/11/16免费车队第一次 Java重构
            // int categroy = teamType.getCategory().intValue();
            //--这里并不会有很多, 就循环插入了, 不做批量插入
            Integer settlementTypeId = settlementMode.getSettlementTypeId();
            if (settlementTypeId != null) {
                // 发现前端有时候这个 id传 null, 导致排位赛, 先处理一波 2018/11/27
                if (TeamCategoryEnum.FREE.getCode() == teamTypeParams.getCategory()) {
                    // 免费车队写死 1局, 0.5小时
                    Dictionary settDict = dictionaryDictManager.findById(settlementTypeId);
                    if (String.valueOf(SettlementTypeEnum.ROUND.getCode())
                            .equals(settDict.getCode())) {
                        // 写死一局
                        settlementMode.setSettlementNumber(new BigDecimal(SETTLEMENT_ROUND_NUMBER));
                    } else if (String.valueOf(SettlementTypeEnum.HOUR.getCode())
                            .equals(settDict.getCode())) {
                        // 写死一小时
                        settlementMode.setSettlementNumber(new BigDecimal(SETTLEMENT_HOUR_NUMBER));
                    }
                }
                teamSettlementModeRepository.insertSelective(settlementMode);
                isNullSettlementMode = false;
            }
        }

        // 如果结算类型 id全部是 null, 设置个默认值一局
        if (isNullSettlementMode) {
            Dictionary dict = dictionaryDictManager
                    .findByCodeAndCategoryCode(
                            String.valueOf(SettlementTypeEnum.ROUND.getCode()),
                            DictionaryCategoryCodeEnum.SETTLEMENT_TYPE.getCode());
            TeamSettlementMode settlementMode = TeamSettlementMode.builder()
                    .settlementTypeId(dict.getDictId())
                    .settlementNumber(new BigDecimal(SETTLEMENT_ROUND_NUMBER))
                    .teamTypeId(teamTypeId)
                    .build();
            teamSettlementModeRepository.insertSelective(settlementMode);
        }

    }

    /*private void setSettlementNumberByType(TeamType teamType) {
        Integer settlementTypeId = teamType.getSettlementTypeId();
        Dictionary settlementType = dictionaryDictManager.findById(settlementTypeId);
        if (DictionaryCodeEnum.SETTLEMENT_ROUND.getCode().equals(settlementType.getCode())) {
            teamType.setSettlementNumber(SETTLEMENT_ROUND_NUMBER);
        } else if (DictionaryCodeEnum.SETTLEMENT_HOUR.getCode().equals(settlementType.getCode())) {
            teamType.setSettlementNumber(SETTLEMENT_HOUR_NUMBER);
        }
    }*/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTeamType(TeamTypeParams teamTypeParams) {
        log.info("更新车队类型, 参数: {}", teamTypeParams);
        TeamType teamType = BeanMapper.map(teamTypeParams, TeamType.class);
        // 根据结算类型固定结算数量 2018-11-01 15:30 新增需求
        // setSettlementNumberByType(teamType);
        teamTypeRepository.updateByPrimaryKeySelective(teamType);
        // 游戏区
        updateRelatedGameZone(teamTypeParams);
        // 游戏段位
        updateRelatedGameDan(teamTypeParams);
        // 结算方式 先删除后添加
        teamSettlementModeRepository.deleteByTeamTypeId(teamTypeParams.getTeamTypeId());
        addTeamSettlementMode(teamTypeParams, teamTypeParams.getTeamTypeId());


        // 缓存更新 2018/11/16免费车队第一次重构, 不放在 redis 了
        /*TeamTypeDetailVO typeDetailVO = getTypeById(teamTypeParams.getTeamTypeId());
        freeTeamCacheManager.addOrUpdateFreeTeamType(typeDetailVO);*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTeamType(Integer teamTypeId) {
        // 删除的时候做下架处理...其实就是修改(因为 Python组需要保留历史数据)
        log.info("删除车队类型,后台改为下架处理,车队类型id: {}", teamTypeId);
        TeamType teamType = new TeamType();
        teamType.setTeamTypeId(teamTypeId);
        teamType.setStatus((byte)StatusEnum.DISABLE.getCode());
        teamTypeRepository.updateByPrimaryKeySelective(teamType);
        //  2018/11/16免费车队第一次重构, 已经不会放 redis了
        /*TeamTypeDetailVO typeDetailVO = getTypeById(teamTypeId);
        freeTeamCacheManager.addOrUpdateFreeTeamType(typeDetailVO);*/

    }

    @Override
    public List<TeamTypeSimpleVO> getSimpleByBaojiIdentity(
            TeamTypeAppQueryParams teamTypeAppQueryParams) {
        log.info("APP调用获取车队类型列表, 参数: {}", teamTypeAppQueryParams);
        String uid = UserSessionContext.getUser().getUid();
        Integer userIdentity = teamTypeAppQueryParams.getUserIdentity();
        Integer teamTypeScene = teamTypeAppQueryParams.getTeamTypeScene();
        // 这个阶段是免费车队类型不会由前端传参, 先写死 2018/11/16
        List<TeamTypeSimpleVO> simpleVOList = teamTypeRepository
                .selectTeamTypeList(StatusEnum.ENABLE.getCode(), TeamCategoryEnum.FREE.getCode());

        if (ObjectTools.isNotEmpty(simpleVOList)) {
            // 游戏数据, 游戏的 VO没法通过 sql级联查询出来, 暂时先这样处理吧
            simpleVOList.stream()
                    .forEach(tp -> tp.setGame(dictionaryDictManager
                            .buildDictBase(tp.getGameId(), GameDictVO.class).getValue()));
        }

        /*
         * 以下情况返回所有有效的车队类型
         * 1: 老板或暴鸡暴娘均可
         * 2: 暴鸡或暴娘上车
         * UserIdentityEnum.BJ_BN.getCode().equals(baojiIdentity.byteValue())
         */
        if (FreeTeamTypeSceneEnum.JOIN_TEAM.getCode() == teamTypeScene
                || UserIdentityEnum.BOSS.getCode() == userIdentity) {
            // 按 orderIndex 升序排, 产品需求
            simpleVOList.sort(Comparator.comparingInt(TeamTypeSimpleVO::getOrderIndex));
            return simpleVOList;
        }
        // 查询出暴鸡/暴娘的数据字典 id, 过滤
        filterTypeByBaojiIdentity(userIdentity, simpleVOList, UserIdentityEnum.BAOJI,
                UserIdentityEnum.BN);
        filterTypeByBaojiIdentity(userIdentity, simpleVOList, UserIdentityEnum.BN,
                UserIdentityEnum.BAOJI);
        // 调用 Python 接口通过游戏过滤
        return filterTypeByGameCertify(uid, userIdentity, simpleVOList);
    }

    private List<TeamTypeSimpleVO> filterTypeByGameCertify(String uid, Integer baojiIdentity,
            List<TeamTypeSimpleVO> simpleVOList) {
        BaojiCertifyParams baojiCertifyParams = new BaojiCertifyParams.Builder().uid(uid)
                .identity(UserIdentityEnum.getByCode(baojiIdentity.byteValue()).getPythonCode())
                .build();
        BaojiCertifyResult baojiCertifyResult = pythonSupportService
                .getBaojiCertifyInfo(baojiCertifyParams);
        Set<TeamTypeSimpleVO> actuallySet = new HashSet<>();
        if (ObjectTools.isNotNull(baojiCertifyResult)) {
            List<BaojiCertInfo> certInfo = baojiCertifyResult.getCertInfo();
            if (ObjectTools.isNotEmpty(certInfo)) {
                log.info("APP端获取免费车队类型列表,Python返回的游戏认证结果: {}", certInfo);
                for (BaojiCertInfo baojiCertInfo : certInfo) {
                    String gameCode = String.valueOf(baojiCertInfo.getGameCode());
                    // 如果是同一个游戏, 这里需要保证双方的游戏 code 是一致的
                    log.info("APP端获取免费车队类型列表, 过滤已认证的游戏,"
                            + " 暴鸡身份code:[{}], 游戏code:[{}]", baojiIdentity, gameCode);
                    for (TeamTypeSimpleVO simpleVO : simpleVOList) {
                        // 如果两个游戏 code 相等则放入新的集合
                        if (simpleVO.getGame().getCode().equals(gameCode)) {
                            actuallySet.add(simpleVO);
                        }
                    }
                }
            }
        }
        List<TeamTypeSimpleVO> actuallyList = new ArrayList<>(actuallySet);
        // 按 orderIndex 升序排, 产品需求
        actuallyList.sort(Comparator.comparingInt(TeamTypeSimpleVO::getOrderIndex));
        return actuallyList;
    }

    @Override
    public PagingResponse<TeamTypeSimpleVO> getAllByPage(
            TeamTypeBackendQueryParams teamTypeBackendQueryParams) {
        Page<TeamTypeSimpleVO> page = PageHelper
                .startPage(teamTypeBackendQueryParams.getOffset(),
                        teamTypeBackendQueryParams.getLimit());
        List<TeamTypeSimpleVO> simpleVOList = teamTypeRepository
                .selectTeamTypeList(teamTypeBackendQueryParams.getStatus(),
                        teamTypeBackendQueryParams.getCategory());
        if (ObjectTools.isNotEmpty(simpleVOList)) {
            // 游戏数据, 游戏的 VO没法通过 sql级联查询出来, 暂时先这样处理吧
            simpleVOList.stream()
                    .forEach(tp -> tp.setGame(dictionaryDictManager
                            .buildDictBase(tp.getGameId(), GameDictVO.class).getValue()));
        }
        PagingResponse<TeamTypeSimpleVO> pagingResponse = new PagingResponse<>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), simpleVOList);
        return pagingResponse;
    }

    @Override
    public void initFreeTeamTypeCache() {
        // 2018-11-16 免费车队第一次重构, 不存到 redis 中
        /*List<TeamType> typeList = teamTypeRepository
                .selectByStatusAndCategory(StatusEnum.ENABLE.getCode(),
                        TeamCategoryEnum.FREE.getCode());
        if (ObjectTools.isNotEmpty(typeList)) {
            for (TeamType teamType : typeList) {
                TeamTypeDetailVO typeDetailVO = buildFreeTeamTypeDetailVO(teamType);
                freeTeamCacheManager.addOrUpdateFreeTeamType(typeDetailVO);
            }
            log.info("初始化免费车队类型到 redis 中成功!");
        }*/
    }

    private void filterTypeByBaojiIdentity(Integer baojiIdentity,
            List<TeamTypeSimpleVO> simpleVOList, UserIdentityEnum baoji, UserIdentityEnum bn) {
        if (baoji.getCode() == baojiIdentity) {
            // 如果传入的是暴鸡身份, 则过滤暴娘身份的免费车队类型, 反之亦然
            Dictionary dictBn = dictionaryDictManager
                    .findByCodeAndCategoryCode(String.valueOf(bn.getCode()),
                            DictionaryCategoryCodeEnum.BAOJI_IDENTITY.getCode());
            for (int i = 0; i < simpleVOList.size(); i++) {
                TeamTypeSimpleVO simpleVO = simpleVOList.get(i);
                if (dictBn.getDictId().equals(simpleVO.getBaojiIdentityId())) {
                    simpleVOList.remove(simpleVO);
                }
            }

        }
    }

    /**
     * 修改关联的游戏段位
     */
    private void updateRelatedGameDan(TeamTypeParams teamTypeParams) {

        Integer teamTypeId = teamTypeParams.getTeamTypeId();

        List<Integer> gameDanList = teamTypeParams.getGameDanIdList();
        List<Integer> gameDans = teamTypeRepository.selectGameDan(teamTypeId);
        if (ObjectTools.isNotNull(gameDans)) {
            if (!CollectionUtils.isEqualCollection(gameDans, gameDanList)) {
                // 先删除后添加
                teamTypeRepository.deleteGameDan(teamTypeId);
                addRelatedGameDan(gameDanList, teamTypeId);
            }
        }
    }

    /**
     * 修改车队类型关联的游戏区
     */
    private void updateRelatedGameZone(TeamTypeParams teamTypeParams) {

        Integer teamTypeId = teamTypeParams.getTeamTypeId();

        List<Integer> gameZoneList = teamTypeParams.getGameZoneIdList();
        List<Integer> gameZones = teamTypeRepository.selectGameZone(teamTypeId);
        if (ObjectTools.isNotNull(gameZones)) {
            if (!CollectionUtils.isEqualCollection(gameZones, gameZoneList)) {
                // 先删除后添加
                teamTypeRepository.deleteGameZone(teamTypeId);
                addRelatedGameZone(gameZoneList, teamTypeId);
            }
        }
    }

    /**
     * 新增免费车队类型与游戏段位关联
     */
    private void addRelatedGameDan(List<Integer> gameDanList, Integer freeTeamTypeId) {
        if (ObjectTools.isNotEmpty(gameDanList)) {
            teamTypeRepository.insertGameDan(freeTeamTypeId, gameDanList);
        }
    }

    /**
     * 新增免费车队类型与游戏区关联
     */
    private void addRelatedGameZone(List<Integer> gameZoneList, Integer freeTeamTypeId) {
        if (ObjectTools.isNotEmpty(gameZoneList)) {
            teamTypeRepository.insertGameZone(freeTeamTypeId, gameZoneList);
        }
    }

    /**
     * 根据数据字典 id 构造数据字典基础属性集合
     */
    private List<DictBaseVO> buildDictBaseList(List<Integer> dictIds, Class targetClass) {
        List<DictBaseVO> dictBaseVOS = new ArrayList<>();
        if (ObjectTools.isNotEmpty(dictIds)) {
            for (Integer dictId : dictIds) {
                dictBaseVOS.add(dictionaryDictManager.buildDictBase(dictId, targetClass));
            }
        }
        // order_index正序
        dictBaseVOS.sort(Comparator.comparingInt(DictBaseVO::getOrderIndex));
        return dictBaseVOS;
    }

    /**
     * 根据段位 id 获取段位数据
     */
    private List<DanDictVo> buildDictDanList(List<Integer> dictIds) {
        List<DanDictVo> danDictVos = new ArrayList<>();
        if (ObjectTools.isNotEmpty(dictIds)) {
            for (Integer dictId : dictIds) {
                DictBaseVO<DanDictVo> dictBaseVO = dictionaryDictManager
                        .buildDictBase(dictId, DanDictVo.class);
                if (dictBaseVO != null) {
                    danDictVos.add(dictBaseVO.getValue());
                }
            }
        }
        // order_index 正序
        danDictVos.sort(Comparator.comparingInt(DanDictVo::getOrderIndex));
        return danDictVos;
    }

}
