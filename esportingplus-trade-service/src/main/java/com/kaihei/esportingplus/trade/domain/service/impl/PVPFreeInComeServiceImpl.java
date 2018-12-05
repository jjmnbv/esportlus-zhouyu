package com.kaihei.esportingplus.trade.domain.service.impl;

import com.kaihei.esportingplus.api.feign.ChickenpointGainConfigClient;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenPointGainConfigValueWithResultVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeBaseVO;
import com.kaihei.esportingplus.trade.api.params.ChickenPointIncomeParams;
import com.kaihei.esportingplus.trade.api.vo.PVPFreePreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPFreePreIncomeVo.PVPFreePreIncomeVoBuilder;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeTeamMembersIncome;
import com.kaihei.esportingplus.trade.domain.service.PVPFreeInComeService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("pvpFreeInComeService")
public class PVPFreeInComeServiceImpl implements PVPFreeInComeService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ChickenpointGainConfigClient chickenpointGainConfigClient;

    @Override
    public PVPFreePreIncomeVo getChickenPointIncome(ChickenPointIncomeParams incomeParams, Consumer<Map<Integer,Integer>> updateHook) {

        PVPFreePreIncomeVoBuilder result = PVPFreePreIncomeVo.builder();
        Integer freeTeamTypeId = incomeParams.getFreeTeamTypeId();
        Integer baojiLevel = incomeParams.getBaojiLevel();
        Integer gameResultCode = incomeParams.getGameResultCode();
        List<PVPFreeBaseVO> pvpFreeBossVOS = incomeParams.getPvpFreeBossVOS();

        //获取暴鸡的鸡分配置列表
        ResponsePacket<List<ChickenPointGainConfigValueWithResultVO>> danConfigValuesPacket
                = chickenpointGainConfigClient
                .getChickenConfigValueByTeamTypeAndBaojiLevel(freeTeamTypeId, baojiLevel);
        if(danConfigValuesPacket.getCode() != BizExceptionEnum.SUCCESS.getErrCode()
            || CollectionUtils.isEmpty(danConfigValuesPacket.getData())){
            LOGGER.error("鸡分获取失败：{}",danConfigValuesPacket.toString());
            return result.totalIncome(0).build();
        }

        List<ChickenPointGainConfigValueWithResultVO> danConfigValues = danConfigValuesPacket.getData();

        //存放对应段位应付的金额，用于勾子更新订单收益
        Map<Integer,Integer> updateParms = new HashMap<>(pvpFreeBossVOS.size() + 1);
        List<PVPFreeTeamMembersIncome> incomesFromBoss = new ArrayList<>(pvpFreeBossVOS.size());

        //计算老板鸡分总和
        int sum = pvpFreeBossVOS.stream()
                .mapToInt(m -> danConfigValues.parallelStream()
                        .filter(pointGain -> {
                            //段位匹配
                            return pointGain.getGameDanId().equals(m.getGameDanId()) &&
                                    pointGain.getGameResultId().equals(gameResultCode);
                        })
                        .map(config->{
                            //对应段位的鸡分
                            Integer value = config.getValue();
                            //给勾子添加参数
                            updateParms.put(m.getGameDanId(),value);
                            PVPFreeTeamMembersIncome incomeFromBoss = PVPFreeTeamMembersIncome.builder()
                                    .price(value)
                                    .uid(m.getUid())
                                    .build();
                            incomesFromBoss.add(incomeFromBoss);
                            return value;
                        })
                        .findFirst().orElse(0))
                .sum();

        //如果有收益和勾子：执行更新的勾子更新收益
        if(updateHook != null && sum > 0){
            updateHook.accept(updateParms);
        }
        LOGGER.info("暴鸡应得鸡分：{}",sum);
        return result
                .totalIncome(sum)
                .freeTeamMembersIncomes(incomesFromBoss)
                .build();
    }

}
