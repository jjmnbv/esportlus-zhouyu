package com.kaihei.esportingplus.trade.domain.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.feign.ResourceServiceClient;
import com.kaihei.esportingplus.api.vo.BaojiLevelRateVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.payment.api.enums.DeductRatioEnum;
import com.kaihei.esportingplus.payment.api.enums.DeductRatioStatus;
import com.kaihei.esportingplus.trade.api.params.OrderTeamMember;
import com.kaihei.esportingplus.trade.api.vo.PVPPreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPTeamMembersIncome;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractIncomeService {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    protected ResourceServiceClient resourceServiceClient;

    protected PVPPreIncomeVo getPreIncomeVo(String currentUid, OrderTeamMember leader,
            double incomeRatio, List<PVPTeamMembersIncome> orderProfits) {
        //统计老板总额和队长以外的暴鸡收益总额
        int bossTotalAmout = 0;
        int baojiTotalProfit = 0;
        int currentMemberPrice = 0;
        for (PVPTeamMembersIncome member : orderProfits) {
            if (member.getUserIdentity().equals(UserIdentityEnum.BOSS.getCode())) {
                bossTotalAmout += member.getPrice();
            } else if (member.getUserIdentity().equals(UserIdentityEnum.BAOJI.getCode())) {
                if (member.getUid().equals(currentUid)) {
                    currentMemberPrice = member.getPrice();
                }
                baojiTotalProfit += member.getPrice();

                //统计其他暴鸡原始总收入后，再把抽成后的收益覆盖回来，返回客户端，
                // 防止队长拿其他暴鸡抽成后的收益进行计算
                member.setPrice(member.getProfitAfterRatio());
            }
        }

        //设置队长收益
        PVPPreIncomeVo preInComeVo = new PVPPreIncomeVo();
        int leaderProfitAmout = bossTotalAmout - baojiTotalProfit;
        if (currentUid.equals(leader.getTeamMemberUID())) {
            // 说明当前是队长
            currentMemberPrice = getProfitAfterRate(leaderProfitAmout,incomeRatio,leader.getTeamMemberUID());
        }
        LOGGER.info("老板支付总额：{}", bossTotalAmout);
        LOGGER.info("队长[{}]抽成后收益：{}", leader.getTeamMemberUID(), currentMemberPrice);
        // 外层为当前暴鸡的预期收益
        preInComeVo.setPrice(currentMemberPrice);
        //追加当队长收益
        PVPTeamMembersIncome leaderProfit = new PVPTeamMembersIncome();
        leaderProfit.setUid(leader.getTeamMemberUID());
        leaderProfit.setPrice(currentMemberPrice);
        leaderProfit.setUserIdentity(leader.getUserIdentity());
        leaderProfit.setUserName(leader.getTeamMemberName());
        orderProfits.add(leaderProfit);

        preInComeVo.setTeamMembersIncomes(orderProfits);

        LOGGER.info("PVP预计收益计算结束");
        return preInComeVo;
    }

    protected int getProfitAfterRate(int price,double incomeRatio,String uid){
        //抽成后收益
        double priceAfterRatio = price * (1 - incomeRatio);
        //如果存在小数点继续去尾
        int priceAfterRatioCutTail = (int) priceAfterRatio;
        LOGGER.info("暴鸡[{}]预计收益：{},抽成后收益:{}，去尾:{},抽成比例:{}",
                uid, price,priceAfterRatio,
                priceAfterRatio - priceAfterRatioCutTail,incomeRatio);

        return priceAfterRatioCutTail;
    }

    protected double getIncomeRatio(){
        double incomeRatio = 0.0D;
        Map incomeRatioMap = cacheManager.get(String.format(RedisKey.PAYMENT_DEDUCT_RATIO_PREFIX, DeductRatioEnum.CALC_ORDER.name().toLowerCase()), Map.class);
        if(incomeRatioMap == null){
            LOGGER.error("获取平台抽成比例失败，使用默认抽成比例：0,失败原因：没有抽成比例的缓存");
        }else{
            if(incomeRatioMap.get("state").toString().equals(DeductRatioStatus.DISABLE.name())){
                LOGGER.error("获取平台抽成比例失败，使用默认抽成比例：0,失败原因：抽成比例配置已关闭");
            }else{
                incomeRatio = Double.valueOf(incomeRatioMap.get("ratio").toString());
            }
        }
        return incomeRatio;
    }

    protected Map<Integer, Double> getLevelsRate(List<Integer> baojiLevels) {
        //1.获取所有暴鸡等级和对应的系数
        ResponsePacket<List<BaojiLevelRateVo>> baojiLevelRateBatch = resourceServiceClient
                .getBaojiLevelRateBatch(baojiLevels);
        if (baojiLevelRateBatch.getCode() != BizExceptionEnum.SUCCESS.getErrCode()
                || baojiLevelRateBatch.getData() == null) {
            LOGGER.error(baojiLevelRateBatch.toString());
            throw new BusinessException(BizExceptionEnum.BATCH_BAOJI_LEVEL_RATE);
        }

        //3.获取各暴鸡等级的系数 重复key的时候会报异常 需要使用BinaryOperator参数：(k,v)->v)
        Map<Integer, Double> rates = baojiLevelRateBatch.getData().stream()
                .collect(Collectors.toMap(BaojiLevelRateVo::getBaojiLevel,
                        x -> x.getBaojiLevelRate().doubleValue(), (k, v) -> v));

        //错误的暴鸡等级校验 TODO 可不校验，在创建数据的时候校验
        String invalidLevels = baojiLevels.stream()
                .filter(f -> !rates.containsKey(Integer.valueOf(f)))
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        if (StringUtils.isNoneBlank(invalidLevels)) {
            throw new BusinessException(BizExceptionEnum.INVALID_BAOJI_LEVEL, invalidLevels);
        }

        return rates;
    }
}
