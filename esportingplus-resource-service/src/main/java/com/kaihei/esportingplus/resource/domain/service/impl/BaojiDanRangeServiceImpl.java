package com.kaihei.esportingplus.resource.domain.service.impl;

import com.kaihei.esportingplus.api.params.BaojiDanRangeBatchParams;
import com.kaihei.esportingplus.api.params.BaojiDanRangeParams;
import com.kaihei.esportingplus.api.vo.BaojiDanRangeVO;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.GameDictVO;
import com.kaihei.esportingplus.api.vo.freeteam.DanDictVo;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.resource.data.manager.FreeTeamCacheManager;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.data.repository.BaojiDanRangeRepository;
import com.kaihei.esportingplus.resource.domain.entity.BaojiDanRange;
import com.kaihei.esportingplus.resource.domain.service.BaojiDanRangeService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liangyi
 */
@Service("baojiDanRangeService")
public class BaojiDanRangeServiceImpl implements BaojiDanRangeService {

    private static final Logger log = LoggerFactory.getLogger(BaojiDanRangeServiceImpl.class);

    @Autowired
    DictionaryDictManager dictionaryDictManager;
    @Autowired
    BaojiDanRangeRepository baojiDanRangeRepository;
    @Autowired
    FreeTeamCacheManager freeTeamCacheManager;

    @Override
    public BaojiDanRangeVO getBaojiDanRangeByGameAndLevel(Integer gameId, Integer baojiLevel) {
        BaojiDanRangeVO baojiDanRangeVO = freeTeamCacheManager
                .getBaojiDanRange(gameId, baojiLevel);
        if (ObjectTools.isNull(baojiDanRangeVO)) {
            // 缓存中没有, 查询数据库
            log.warn("未从redis中获取到游戏[{}]的暴鸡等级[{}]的接单范围", gameId, baojiLevel);
            // 从 DB 中查询
            BaojiDanRange baojiDanRange = baojiDanRangeRepository
                    .selectByGameAndBaojiLevel(gameId, baojiLevel);
            if (ObjectTools.isNull(baojiDanRange)) {
                log.error("未从DB中查询到游戏id:[{}]暴击等级:[{}]的接单范围!",
                        gameId, baojiLevel);
                throw new BusinessException(BizExceptionEnum.BAOJI_DAN_RANGE_NOT_FOUND);
            }
            baojiDanRangeVO = bulildBaojiDanRangeVO(baojiDanRange);
        }
        return baojiDanRangeVO;
    }

    @Override
    public List<BaojiDanRangeVO> getAllBaojiDanRangeByGameId(Integer gameId) {
        List<BaojiDanRange> baojiDanRangeList = baojiDanRangeRepository
                .selectBaojiDanRangeList(gameId);
        List<BaojiDanRangeVO> baojiDanRangeVOS = new ArrayList<>();
        for (BaojiDanRange baojiDanRange : baojiDanRangeList) {
            BaojiDanRangeVO rangeVO = bulildBaojiDanRangeVO(baojiDanRange);
            baojiDanRangeVOS.add(rangeVO);
        }
        return baojiDanRangeVOS;
    }

    private BaojiDanRangeVO bulildBaojiDanRangeVO(BaojiDanRange baojiDanRange) {
        DictBaseVO<GameDictVO> gameDict = dictionaryDictManager.buildDictBase(
                baojiDanRange.getGameId(), GameDictVO.class);
        DictBaseVO<DanDictVo> lowerDan = dictionaryDictManager.buildDictBase(
                baojiDanRange.getLowerDanId(), DanDictVo.class);
        DictBaseVO<DanDictVo> upperDan = dictionaryDictManager.buildDictBase(
                baojiDanRange.getUpperDanId(), DanDictVo.class);
        return BaojiDanRangeVO.builder()
                .baojiDanRangeId(baojiDanRange.getBaojiDanRangeId())
                .game(gameDict.getValue()).baojiLevel(baojiDanRange.getBaojiLevel())
                .lowerDan(lowerDan.getValue()).upperDan(upperDan.getValue()).build();
    }

    @Override
    public void addBaojiDanRange(BaojiDanRangeBatchParams baojiDanRangeBatchParams) {
        log.info("新增暴鸡接单范围, 参数: {}", baojiDanRangeBatchParams);
        Integer gameId = baojiDanRangeBatchParams.getGameId();
        List<BaojiDanRangeParams> baojiDanRangeList = baojiDanRangeBatchParams
                .getBaojiDanRangeList();
        // 由于这里的数据并不是很多, 就3个...所以直接用循环来处理
        for (BaojiDanRangeParams baojiDanRangeParams : baojiDanRangeList) {
            addBaojiDanRangeAndCache(gameId, baojiDanRangeParams);
        }
    }

    private void addBaojiDanRangeAndCache(Integer gameId, BaojiDanRangeParams baojiDanRangeParams) {
        BaojiDanRange baojiDanRange = BeanMapper.map(baojiDanRangeParams, BaojiDanRange.class);
        baojiDanRange.setBaojiDanRangeId(null);
        baojiDanRange.setGameId(gameId);
        baojiDanRangeRepository.insertSelective(baojiDanRange);
        log.info("DB--新增暴鸡接单范围: {}", baojiDanRange);
        // 缓存添加
        BaojiDanRangeVO baojiDanRangeVO = bulildBaojiDanRangeVO(baojiDanRange);
        freeTeamCacheManager.addOrUpdateBaojiDanRange(baojiDanRangeVO);
    }

    @Override
    public void updateBaojiDanRange(BaojiDanRangeBatchParams baojiDanRangeBatchParams) {
        log.info("更新暴鸡接单范围, 参数: {}", baojiDanRangeBatchParams);
        Integer gameId = baojiDanRangeBatchParams.getGameId();
        List<BaojiDanRangeParams> baojiDanRangeList = baojiDanRangeBatchParams
                .getBaojiDanRangeList();
        for (BaojiDanRangeParams baojiDanRangeParams : baojiDanRangeList) {
            if (baojiDanRangeParams.getBaojiDanRangeId() == 0) {
                addBaojiDanRangeAndCache(gameId, baojiDanRangeParams);
            } else {
                updateBaojiDanRangeAndCache(gameId, baojiDanRangeParams);
            }
        }
    }

    private void updateBaojiDanRangeAndCache(Integer gameId, BaojiDanRangeParams baojiDanRangeParams) {
        BaojiDanRange baojiDanRange = BeanMapper.map(baojiDanRangeParams, BaojiDanRange.class);
        baojiDanRange.setGameId(gameId);
        baojiDanRangeRepository.updateByPrimaryKeySelective(baojiDanRange);
        log.info("DB--更新暴鸡接单范围到: {}", baojiDanRange);
        // 缓存更新
        BaojiDanRangeVO baojiDanRangeVO = bulildBaojiDanRangeVO(baojiDanRange);
        freeTeamCacheManager.addOrUpdateBaojiDanRange(baojiDanRangeVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBaojiDanRangeBatch(List<Integer> baojiDanRangeIdList) {
        log.info("删除暴鸡接单范围, 参数: {}", baojiDanRangeIdList);
        for (Integer baojiDanRangeId : baojiDanRangeIdList) {
            BaojiDanRange baojiDanRange = baojiDanRangeRepository
                    .selectByPrimaryKey(baojiDanRangeId);
            if (ObjectTools.isNotNull(baojiDanRange)) {
                baojiDanRangeRepository.deleteByPrimaryKey(baojiDanRangeId);
                log.info("DB--删除暴鸡接单范围: {}", baojiDanRange);
                // 删除 redis 中的缓存
                freeTeamCacheManager.deleteBaojiDanRange(
                        baojiDanRange.getGameId(), baojiDanRange.getBaojiLevel());
            }
        }
    }

    @Override
    public void initBaojiDanRangeCache() {
        List<BaojiDanRange> baojiDanRanges =
                baojiDanRangeRepository.selectBaojiDanRangeList(null);
        if (ObjectTools.isNotEmpty(baojiDanRanges)) {
            for (BaojiDanRange baojiDanRange : baojiDanRanges) {
                BaojiDanRangeVO baojiDanRangeVO = bulildBaojiDanRangeVO(baojiDanRange);
                freeTeamCacheManager.addOrUpdateBaojiDanRange(baojiDanRangeVO);
            }
            log.info("初始化暴鸡接单范围到Redis中成功!");
        }
    }
}
