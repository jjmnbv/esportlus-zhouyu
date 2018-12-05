package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.feign.PVPBaojiPricingConfigServiceClient;
import com.kaihei.esportingplus.api.params.BaojiPricingConfigParams;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.GameDictVO;
import com.kaihei.esportingplus.api.vo.PVPBaojiGameDanIncomeVO;
import com.kaihei.esportingplus.api.vo.PVPBaojiPricingConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.DanDictVo;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.data.repository.PVPBaojiPricingConfigRepository;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.PVPBaojiPricingConfig;
import com.kaihei.esportingplus.resource.domain.service.PVPBaojiPricingConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 暴鸡计价配置
 * @author liangyi
 */
@RestController
@RequestMapping("/baoji/pricing/config")
@Api(tags = {"暴鸡计价配置"})
public class PVPBaojiPricingConfigController implements PVPBaojiPricingConfigServiceClient {

    @Autowired
    DictionaryDictManager dictionaryDictManager;
    @Autowired
    PVPBaojiPricingConfigRepository pvpBaojiPricingConfigRepository;
    @Autowired
    PVPBaojiPricingConfigService pvpBaojiPricingConfigService;



    @ApiOperation(value = "查询单个详细的暴鸡计价配置")
    @Override
    public ResponsePacket<PVPBaojiPricingConfigVO> getBaojiPricingConfigDetail(
                @ApiParam(value = "查询参数", required = true)
                @RequestBody BaojiPricingConfigParams baojiPricingConfigParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, baojiPricingConfigParams);
        PVPBaojiPricingConfig pricingConfig = BeanMapper
                .map(baojiPricingConfigParams, PVPBaojiPricingConfig.class);

        // 根据暴鸡等级 code 查询出暴鸡等级 id
        Dictionary baojiLevelDict = dictionaryDictManager.findByCodeAndCategoryCode(
                String.valueOf(baojiPricingConfigParams.getBaojiLevelCodeList().get(0)),
                DictionaryCategoryCodeEnum.BAOJI_LEVEL.getCode());
        DictBaseVO baojiLevel = BeanMapper.map(baojiLevelDict, DictBaseVO.class);

        pricingConfig.setBaojiLevelId(baojiLevel.getDictId());
        pricingConfig.setBossGameDanId(baojiPricingConfigParams.getBossGameDanIdList().get(0));
        // 只查询启用的
        pricingConfig.setStatus((byte)1);

        PVPBaojiPricingConfig pvpBaojiPricingConfig = pvpBaojiPricingConfigRepository
                .selectOne(pricingConfig);

        PVPBaojiPricingConfigVO pvpBaojiPricingConfigVO = bulidPVPBaojiPricingConfigVO(
                pvpBaojiPricingConfig);
        pvpBaojiPricingConfigVO.setBaojiLevel(baojiLevel);

        return ResponsePacket.onSuccess(pvpBaojiPricingConfigVO);
    }


    @ApiOperation(value = "查询暴鸡段位的计价配置")
    @Override
    public ResponsePacket<List<PVPBaojiGameDanIncomeVO>> getBaojiGameDanIncome(
            @ApiParam(value = "查询参数", required = true)
            @RequestBody BaojiPricingConfigParams baojiPricingConfigParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, baojiPricingConfigParams);
        List<PVPBaojiGameDanIncomeVO> baojiGameDanIncomeList = pvpBaojiPricingConfigService
                .getBaojiGameDanIncome(baojiPricingConfigParams);
        return ResponsePacket.onSuccess(baojiGameDanIncomeList);
    }


    private PVPBaojiPricingConfigVO bulidPVPBaojiPricingConfigVO(
            PVPBaojiPricingConfig pvpBaojiPricingConfig) {
        PVPBaojiPricingConfigVO pricingConfigVO = BeanMapper
                .map(pvpBaojiPricingConfig, PVPBaojiPricingConfigVO.class);
        pricingConfigVO.setBaojiPricingConfigId(pvpBaojiPricingConfig.getId());

        // 游戏
        if (pvpBaojiPricingConfig.getGameId() != 0) {
            DictBaseVO<GameDictVO> gameDict = dictionaryDictManager
                    .buildDictBase(pvpBaojiPricingConfig.getGameId(), GameDictVO.class);
            pricingConfigVO.setGame(gameDict.getValue());
        }

        // 段位
        if (pvpBaojiPricingConfig.getBossGameDanId() != 0) {
            DictBaseVO<DanDictVo> danDict = dictionaryDictManager
                    .buildDictBase(pvpBaojiPricingConfig.getBossGameDanId(), DanDictVo.class);
            pricingConfigVO.setGameDan(danDict.getValue());
        }

        return pricingConfigVO;
    }
}
