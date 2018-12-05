package com.kaihei.esportingplus.trade.domain.service.impl;

import com.kaihei.esportingplus.api.feign.PVPBaojiPricingConfigServiceClient;
import com.kaihei.esportingplus.api.params.BaojiPricingConfigParams;
import com.kaihei.esportingplus.api.vo.PVPBaojiGameDanIncomeVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.ProductBizTypeEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.trade.api.params.OrderTeamPVPMember;
import com.kaihei.esportingplus.trade.api.params.PVPInComeParams;
import com.kaihei.esportingplus.trade.api.vo.InComeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPPreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPTeamMembersIncome;
import com.kaihei.esportingplus.trade.common.IncomeCaculateParams;
import com.kaihei.esportingplus.trade.common.PVPIncomeCaculatePrams;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.service.PVPInComeService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("pvpInComeService")
public class PVPInComeServiceImpl extends AbstractIncomeService implements PVPInComeService {

    @Autowired
    private PVPBaojiPricingConfigServiceClient pricingConfigServiceClient;

    @Override
    public InComeVo getIncome(IncomeCaculateParams caculateParams) {

        String teamMemberUID = caculateParams.getPvpMember().getTeamMemberUID();

        //构建收益计算所需的参数
        PVPIncomeCaculatePrams pvpProfitParams = getPVPIncomeParams(caculateParams);
        //老板支付折扣：注意此处产品极有可能说变就变，coder们请接好炸弹，做好自爆准备
        //变更历史：无折扣->配置化折扣->无折扣(目前)->接龙
        double bossPayDiscount = 1.0;
        pvpProfitParams.setBossPaySum((int)(pvpProfitParams.getBossPaySum() * bossPayDiscount));
        //某暴鸡获得酬劳金额(局) = 老板支付总金额* （某暴鸡的系数 / (暴鸡1系数 + 暴鸡2系数+ 暴鸡3系数 + ... + 暴鸡n系数)) * 局数
        //某暴鸡获得酬劳金额(小时) = 老板支付总金额*  暴娘人数 * 小时数
        return getIncome(teamMemberUID, pvpProfitParams);
    }

    private InComeVo getIncome(String teamMemberUID, PVPIncomeCaculatePrams pvpProfitParams) {
        double baojiIncome = pvpProfitParams.getSettlementTypeEnum() == SettlementTypeEnum.ROUND ?
                pvpProfitParams.getBossPaySum() * pvpProfitParams.getCurrentBaojiLevelRate() /
                pvpProfitParams.getBaojiLevelRateSum() * pvpProfitParams.getBattleSum()
                //如果按小时结算
                : pvpProfitParams.getBossPaySum() * pvpProfitParams.getBaoNiangNumbers() *
                        pvpProfitParams.getBattleSum();

        int baojiIncomeFloor = (int) baojiIncome;
        LOGGER.info(">> [{}]收益: {},去尾数:{}", teamMemberUID,
                baojiIncomeFloor, baojiIncome - baojiIncomeFloor);
        return InComeVo.builder()
                .uid(teamMemberUID)
                .paySum(pvpProfitParams.getBossPaySum())
                .inComeAmounts(baojiIncomeFloor)
                .build();
    }

    private PVPIncomeCaculatePrams getPVPIncomeParams(IncomeCaculateParams caculateParams){

        List<OrderTeamPVPMember> teamMembers = caculateParams.getPvpTeamMembers();
        OrderTeamPVPMember member = caculateParams.getPvpMember();
        List<Order> payedOrders = caculateParams.getPayedOrders();

        //取出所有暴鸡暴娘等级列表
        List<Integer> baojiLevels = teamMembers.parallelStream()
                //过虑暴鸡
                .filter(f -> UserIdentityEnum.BOSS.getCode() != f.getUserIdentity())
                //转换暴鸡等级
                .map(OrderTeamPVPMember::getBaojiLevel)
                .distinct()
                .collect(Collectors.toList());

        //拿到暴鸡等级对应的系数
        Map<Integer, Double> levelsRate = getLevelsRate(baojiLevels);
        //当前暴鸡的系数
        double currentLevelRate = levelsRate.get(member.getBaojiLevel());

        //暴鸡系数总和
        double baojiLevelRateSum = levelsRate.values().stream().mapToDouble(rate->rate).sum();

        //获取结算类型
        SettlementTypeEnum settlementTypeEnum = SettlementTypeEnum.getByCode(payedOrders.get(0)
                .getOrderItemTeamPVP().getSettlementType().intValue());

        //计算老板订单总金额
        int bossPaySum = payedOrders.parallelStream()
                //过虑老板
                .filter(f -> f.getOrderItemTeamPVP().getUserIdentity()
                        .equals(UserIdentityEnum.BOSS.getCode()))
                .mapToInt(m -> m.getActualPaidAmount() + m.getDiscountAmount())
                .sum();

        //暴娘人数
        long baoNiangNumbers = teamMembers.stream()
                .filter(f -> f.getUserIdentity() == UserIdentityEnum.BN.getCode()).count();
        return PVPIncomeCaculatePrams.builder()
                .settlementTypeEnum(settlementTypeEnum)
                //如果产生了部分退款，结算收益时需要减去此部分的钱
                .bossPaySum(bossPaySum - caculateParams.getRefundFee())
                .currentBaojiLevelRate(currentLevelRate)
                .baojiLevelRateSum(baojiLevelRateSum)
                .baoNiangNumbers((int)baoNiangNumbers)
                .battleSum(caculateParams.getBattleSum())
                .build();
    }

    private PVPIncomeCaculatePrams getPVPPreIncomeParams(PVPInComeParams pvpInComeParams){

        List<OrderTeamPVPMember> teamMembers = pvpInComeParams.getTeamMembers();

        //老板段位ID集合
        List<Integer> bossDans = teamMembers.stream()
                .filter(f->f.getUserIdentity() == UserIdentityEnum.BOSS.getCode())
                .map(OrderTeamPVPMember::getBossDanId)
                .collect(Collectors.toList());

        //暴鸡等级集合
        List<Integer> baojiLevels = teamMembers.stream()
                .filter(f->f.getUserIdentity() == UserIdentityEnum.BAOJI.getCode()
                            || f.getUserIdentity() == UserIdentityEnum.LEADER.getCode())
                .map(OrderTeamPVPMember::getBaojiLevel)
                .collect(Collectors.toList());

        //老板支付总额
        int bossPaySum = getBossPaySum(pvpInComeParams.getGameId(),bossDans,baojiLevels)
                * pvpInComeParams.getPreRounds();

        //拿到暴鸡等级对应的系数
        Map<Integer, Double> levelsRate = getLevelsRate(baojiLevels);
        //当前暴鸡的系数
        double currentLevelRate = levelsRate.get(pvpInComeParams.getBaojiLevel());

        //暴鸡系数总和
        double baojiLevelRateSum = levelsRate.values().stream().mapToDouble(rate->rate).sum();

        //暴娘人数
        long baoNiangNumbers = pvpInComeParams.getTeamMembers().stream()
                .filter(f -> f.getUserIdentity() == UserIdentityEnum.BN.getCode())
                .count();

        return PVPIncomeCaculatePrams.builder()
                .settlementTypeEnum(pvpInComeParams.getSettlementTypeEnum())
                //如果产生了部分退款，结算收益时需要减去此部分的钱
                .bossPaySum(bossPaySum)
                .currentBaojiLevelRate(currentLevelRate)
                .baojiLevelRateSum(baojiLevelRateSum)
                .baoNiangNumbers((int)baoNiangNumbers)
                .battleSum(pvpInComeParams.getPreRounds())
                .build();

    }

    @Override
    public int getBossPaySum(Integer gameId,List<Integer> bossDans,List<Integer> baojiLevels) {

        //入参构造
        BaojiPricingConfigParams configParams = BaojiPricingConfigParams.builder()
                .pricingType(ProductBizTypeEnum.GAMING_TEAM.getCode())
                .gameId(gameId)
                .bossGameDanIdList(bossDans)
                .baojiLevelCodeList(baojiLevels)
                .build();
        //获取对应段位应得的收益
        ResponsePacket<List<PVPBaojiGameDanIncomeVO>> baojiIncomePacket = pricingConfigServiceClient
                .getBaojiGameDanIncome(configParams);
        if (baojiIncomePacket.getCode() == BizExceptionEnum.SUCCESS.getErrCode()
                && baojiIncomePacket.getData() != null
                && CollectionUtils.isNotEmpty(baojiIncomePacket.getData())) {

            List<PVPBaojiGameDanIncomeVO> incomeData = baojiIncomePacket.getData();

            return incomeData.stream().mapToInt(PVPBaojiGameDanIncomeVO::getOriginalFee).sum();
        } else {
            LOGGER.error("查询对应段位的收益失败，{}", baojiIncomePacket.toString());
            throw new BusinessException(baojiIncomePacket.getCode(), baojiIncomePacket.getMsg());
        }
    }

    @Override
    public PVPPreIncomeVo preIncome(PVPInComeParams pvpInComeParams) {

        LOGGER.info("PVP预计收益计算开始: {}", pvpInComeParams);

        //取出队长
        OrderTeamPVPMember leader = pvpInComeParams.getTeamMembers().get(0);
        if (ObjectTools.isNull(leader)) {
            LOGGER.error("预期收益计算错误, 该队长[{}]已不在车队[{}]中!",
                    leader.getTeamMemberUID(), pvpInComeParams.getTeamSequence());
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_NOT_EXIST);
        }

        //计算其他暴鸡的收益
        double incomeRatio = getIncomeRatio();
        List<PVPTeamMembersIncome> orderProfits = pvpInComeParams.getTeamMembers().stream()
                //排除队长
                .filter(member -> !member.getTeamMemberUID().equals(leader.getTeamMemberUID()))
                .map(member -> {
                    PVPTeamMembersIncome orderProfit = new PVPTeamMembersIncome();
                    orderProfit.setUid(member.getTeamMemberUID());
                    orderProfit.setUserIdentity(member.getUserIdentity());

                    //暴鸡计算收益金额
                    PVPIncomeCaculatePrams pvpPreIncomeParams = getPVPPreIncomeParams(pvpInComeParams);
                    InComeVo inComeVo = getIncome(member.getTeamMemberUID(), pvpPreIncomeParams);

                    //老板设置对应段位应付多少钱
                    if (member.getUserIdentity().equals(UserIdentityEnum.BOSS.getCode())) {
                        orderProfit.setPrice(inComeVo.getInComeAmounts());
                    } else {
                        //暴鸡设置应得多少钱
                        orderProfit.setPrice(inComeVo.getInComeAmounts());

                        //设置 抽成后的收益
                        int baojiProfitAfterRatio = getProfitAfterRate(inComeVo.getInComeAmounts(),
                                incomeRatio,member.getTeamMemberUID());
                        orderProfit.setProfitAfterRatio(baojiProfitAfterRatio);
                    }
                    orderProfit.setUserName(member.getTeamMemberName());
                    return orderProfit;
                }).collect(Collectors.toList());

        return getPreIncomeVo(pvpInComeParams.getUid(), leader, incomeRatio, orderProfits);
    }

}
