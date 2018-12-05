package com.kaihei.esportingplus.trade.domain.service.impl;

import com.kaihei.esportingplus.api.vo.RedisGameRaid;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BaojiLevelEnum;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.RaidLocationEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.trade.api.params.OrderTeamRPGMember;
import com.kaihei.esportingplus.trade.api.params.RPGInComeParams;
import com.kaihei.esportingplus.trade.api.vo.InComeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPPreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPTeamMembersIncome;
import com.kaihei.esportingplus.trade.common.IncomeCaculateParams;
import com.kaihei.esportingplus.trade.common.RPGIncomeCaculateParams;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.service.RPGInComeService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service("rpgInComeService")
public class RPGInComeServiceImpl extends AbstractIncomeService implements RPGInComeService {

    @Override
    public PVPPreIncomeVo getPreInCome(RPGInComeParams inComeParams) {

        LOGGER.info("RPG预计收益计算开始: {}", inComeParams);
        //取出队长
        OrderTeamRPGMember leader = inComeParams.getTeamMembers().get(0);
        if (ObjectTools.isNull(leader)) {
            LOGGER.error("预期收益计算错误, 该队长[{}]已不在车队[{}]中!",
                    leader.getTeamMemberUID(), inComeParams.getTeamSequence());
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_NOT_EXIST);
        }
        //计算其他暴鸡的收益
        double incomeRatio = getIncomeRatio();
        List<PVPTeamMembersIncome> orderProfits = inComeParams.getTeamMembers().stream()
                //排除队长
                .filter(member -> !member.getTeamMemberUID().equals(leader.getTeamMemberUID()))
                .map(member -> {
                    PVPTeamMembersIncome orderProfit = new PVPTeamMembersIncome();
                    orderProfit.setUid(member.getTeamMemberUID());
                    orderProfit.setUserIdentity(member.getUserIdentity());
                    //老板直接设置车队价格
                    if (member.getUserIdentity().equals(UserIdentityEnum.BOSS.getCode())) {
                        orderProfit.setPrice(inComeParams.getTeamPrice());
                    } else {
                        //暴鸡计算收益金额
                        Integer price = calculaRMBPreIncome(inComeParams, member);
                        orderProfit.setPrice(price);

                        //设置 抽成后的收益
                        int baojiProfitAfterRatio = getProfitAfterRate(price,
                                incomeRatio,member.getTeamMemberUID());
                        orderProfit.setProfitAfterRatio(baojiProfitAfterRatio);
                    }
                    orderProfit.setUserName(member.getTeamMemberName());
                    return orderProfit;
                }).collect(Collectors.toList());

        return getPreIncomeVo(inComeParams.getUid(), leader, incomeRatio, orderProfits);
    }

    @Override
    public InComeVo getIncome(IncomeCaculateParams caculateParams) {
        RPGIncomeCaculateParams profit = getProfitParam(caculateParams);
        InComeVo inCome = new InComeVo();
        inCome.setPaySum(profit.getSumAmout());
        inCome.setInComeAmounts(calculaInCome(profit));
        return inCome;
    }


    private Integer calculaRMBPreIncome(RPGInComeParams inComeParams,
            OrderTeamRPGMember member) {
        RPGIncomeCaculateParams profit = getPreProfitParam(inComeParams, member);
        return calculaInCome(profit);
    }

    private Integer calculaInCome(RPGIncomeCaculateParams profit) {
        //收益金额
        double profitFee = 0;
        //对应位置的占比
        double hiredRate = profit.getRaidLocation() == RaidLocationEnum.DPS.getCode()
                ? profit.getRaidDpsHiredRate() : profit.getRaidAssistHiredRate();
        //对应位置的人数
        int hiredNumbers = profit.getRaidLocation() == RaidLocationEnum.DPS.getCode()
                ? profit.getRaidDpsNumbers() : profit.getRaidAssistNumbers();

        //普通暴鸡对应位置人数
        int ordinaryLocationNumbers = profit.getRaidLocation() == RaidLocationEnum.DPS.getCode()
                ? profit.getOrdinaryBaojiDpsNumbers() : profit.getOrdinaryBaojiAssistNumbers();

        //优选暴鸡对应位置人数
        int excellentLocationNumbers = profit.getRaidLocation() == RaidLocationEnum.DPS.getCode()
                ? profit.getExcellentBaojiDpsNumbers() : profit.getExcellentBaojiAssistNumbers();

        //超级暴鸡对应位置人数
        int superLocationNumbers = profit.getRaidLocation() == RaidLocationEnum.DPS.getCode()
                ? profit.getSuperBaojiDpsNumbers() : profit.getSuperBaojiAssistNumbers();

        //普通暴鸡数量
        int ordinaryNumbers =
                profit.getOrdinaryBaojiDpsNumbers() + profit.getOrdinaryBaojiAssistNumbers();
        //普通暴鸡数量
        int excellentNumbers =
                profit.getExcellentBaojiDpsNumbers() + profit.getExcellentBaojiAssistNumbers();
        //普通暴鸡数量
        int superNumbers = profit.getSuperBaojiDpsNumbers() + profit.getSuperBaojiAssistNumbers();

//        If普通暴鸡数量、优选暴鸡数量、超级暴鸡数量均不为0：
//        每个普通暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)
//          *（普通暴鸡系数/（普通暴鸡系数*普通打手暴鸡人数+优选暴鸡系数*优选打手暴鸡人数+超级暴鸡系数*超级打手暴鸡人数））
//        每个优选暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)
//          *（优选暴鸡系数/（普通暴鸡系数*普通打手暴鸡人数+优选暴鸡系数*优选打手暴鸡人数+超级暴鸡系数*超级打手暴鸡人数））
//        每个超级暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)
//          *（超级暴鸡系数/（普通暴鸡系数*普通打手暴鸡人数+优选暴鸡系数*优选打手暴鸡人数+超级暴鸡系数*超级打手暴鸡人数））
        if (ordinaryNumbers != 0
                && excellentNumbers != 0
                && superNumbers != 0) {

            LOGGER.debug("普通暴鸡数量、优选暴鸡数量、超级暴鸡数量均不为0");

            //普通暴鸡
            double baojiRate = 0;

            if (profit.getBaojiLevel() == BaojiLevelEnum.COMMON.getCode()) {
                baojiRate = profit.getOrdinaryBaojiLevelRate();
            }
            //优选暴鸡
            if (profit.getBaojiLevel() == BaojiLevelEnum.PREFERENCE.getCode()) {
                baojiRate = profit.getExcellentBaojiLevelRate();
            }
            //超级暴鸡
            if (profit.getBaojiLevel() == BaojiLevelEnum.SUPER.getCode()) {
                baojiRate = profit.getSuperBaojiLevelRate();
            }

            profitFee = profit.getSumAmout()
                    * (hiredRate * hiredNumbers / (
                    profit.getRaidDpsHiredRate() * profit.getRaidDpsNumbers()
                            + profit.getRaidAssistHiredRate() * profit.getRaidAssistNumbers()))
                    * (baojiRate / (profit.getOrdinaryBaojiLevelRate() * ordinaryLocationNumbers
                    + profit.getExcellentBaojiLevelRate() * excellentLocationNumbers
                    + profit.getSuperBaojiLevelRate() * superLocationNumbers));

//            If普通暴鸡数量=0：
//            每个普通暴鸡佣金=0
//            每个优选暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)
//              *（优选暴鸡系数/（优选暴鸡系数*优选打手暴鸡人数+超级暴鸡系数*超级打手暴鸡人数））
//            每个超级暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)
//              *（超级暴鸡系数/（优选暴鸡系数*优选打手暴鸡人数+超级暴鸡系数*超级打手暴鸡人数））
        } else if (ordinaryNumbers == 0
                && excellentNumbers != 0
                && superNumbers != 0) {

            LOGGER.debug("普通暴鸡数量=0、优选暴鸡数量 != 0、超级暴鸡数量均不为!= 0");

            double baojiRaid = 0;
            //优选暴鸡
            if (profit.getBaojiLevel() == BaojiLevelEnum.PREFERENCE.getCode()) {
                baojiRaid = profit.getExcellentBaojiLevelRate();
            }
            //超级暴鸡
            if (profit.getBaojiLevel() == BaojiLevelEnum.SUPER.getCode()) {
                baojiRaid = profit.getSuperBaojiLevelRate();
            }
            profitFee = profit.getSumAmout()
                    * (hiredRate * hiredNumbers / (
                    profit.getRaidDpsHiredRate() * profit.getRaidDpsNumbers()
                            + profit.getRaidAssistHiredRate() * profit.getRaidAssistNumbers()))
                    * (baojiRaid / (profit.getExcellentBaojiLevelRate() * excellentLocationNumbers
                    + profit.getSuperBaojiLevelRate() * superLocationNumbers));

//            If优选暴鸡数量=0：
//            每个普通暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)
//              *（普通暴鸡系数/（普通暴鸡系数*普通打手暴鸡人数+超级暴鸡系数*超级打手暴鸡人数））
//            每个优选暴鸡佣金=0
//            每个超级暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)
//              *（超级暴鸡系数/（普通暴鸡系数*普通打手暴鸡人数+超级暴鸡系数*超级打手暴鸡人数））
        } else if (ordinaryNumbers != 0
                && excellentNumbers == 0
                && superNumbers != 0) {

            LOGGER.debug("普通暴鸡数量!=0、优选暴鸡数量 == 0、超级暴鸡数量均不为!= 0");

            double baojiRaid = 0;
            //普通暴鸡
            if (profit.getBaojiLevel() == BaojiLevelEnum.COMMON.getCode()) {
                baojiRaid = profit.getOrdinaryBaojiLevelRate();
            }
            //超级暴鸡
            if (profit.getBaojiLevel() == BaojiLevelEnum.SUPER.getCode()) {
                baojiRaid = profit.getSuperBaojiLevelRate();
            }
            profitFee = profit.getSumAmout()
                    * (hiredRate * hiredNumbers / (
                    profit.getRaidDpsHiredRate() * profit.getRaidDpsNumbers()
                            + profit.getRaidAssistHiredRate() * profit.getRaidAssistNumbers()))
                    * (baojiRaid / (profit.getOrdinaryBaojiLevelRate() * ordinaryLocationNumbers
                    + profit.getSuperBaojiLevelRate() * superLocationNumbers));

//            If超级暴鸡数量=0：
//            每个普通暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)
//              *（普通暴鸡系数/（普通暴鸡系数*普通打手暴鸡人数+优选暴鸡系数*优选打手暴鸡人数））
//            每个优选暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)
//              *（优选暴鸡系数/（普通暴鸡系数*普通打手暴鸡人数+优选暴鸡系数*优选打手暴鸡人数））
//            每个超级暴鸡佣金=0
        } else if (ordinaryNumbers != 0
                && excellentNumbers != 0) {

            LOGGER.debug("普通暴鸡数量!=0、优选暴鸡数量 != 0、超级暴鸡数量均不为= 0");

            double baojiRaid = 0;
            //普通暴鸡
            if (profit.getBaojiLevel() == BaojiLevelEnum.COMMON.getCode()) {
                baojiRaid = profit.getOrdinaryBaojiLevelRate();
            }
            //优选暴鸡
            if (profit.getBaojiLevel() == BaojiLevelEnum.PREFERENCE.getCode()) {
                baojiRaid = profit.getExcellentBaojiLevelRate();
            }
            profitFee = profit.getSumAmout()
                    * (hiredRate * hiredNumbers / (
                    profit.getRaidDpsHiredRate() * profit.getRaidDpsNumbers()
                            + profit.getRaidAssistHiredRate() * profit.getRaidAssistNumbers()))
                    * (baojiRaid / (profit.getOrdinaryBaojiLevelRate() * ordinaryLocationNumbers
                    + profit.getExcellentBaojiLevelRate() * excellentLocationNumbers));

//            If普通暴鸡数量=0且优选暴鸡数量=0：
//            每个普通暴鸡佣金=0
//            每个优选暴鸡佣金=0
//            每个超级暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)*超级打手暴鸡人数
        } else if (ordinaryNumbers == 0
                && excellentNumbers == 0
                && superNumbers != 0) {

            LOGGER.debug("普通暴鸡数量=0、优选暴鸡数量 = 0、超级暴鸡数量均不为!= 0");

            profitFee = profit.getSumAmout()
                    * (hiredRate * hiredNumbers / (
                    profit.getRaidDpsHiredRate() * profit.getRaidDpsNumbers()
                            + profit.getRaidAssistHiredRate() * profit.getRaidAssistNumbers()))
                    / superLocationNumbers;

//            If普通暴鸡数量=0且超级暴鸡数量=0：
//            每个普通暴鸡佣金=0
//            每个优选暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)*优选打手暴鸡人数
//            每个超级暴鸡佣金=0
        } else if (ordinaryNumbers == 0
                && excellentNumbers != 0) {

            LOGGER.debug("普通暴鸡数量=0、优选暴鸡数量 != 0、超级暴鸡数量均不为= 0");

            profitFee = profit.getSumAmout()
                    * (hiredRate * hiredNumbers / (
                    profit.getRaidDpsHiredRate() * profit.getRaidDpsNumbers()
                            + profit.getRaidAssistHiredRate() * profit.getRaidAssistNumbers()))
                    / excellentLocationNumbers;

//            If优选暴鸡数量=0且超级暴鸡数量=0：
//            每个普通暴鸡佣金=（老板支付总金额*(打手系数*打手人数/（打手系数*打手人数+辅助系数*辅助人数）)*普通打手暴鸡人数
//            每个优选暴鸡佣金=0
//            每个超级暴鸡佣金=0
        } else if (ordinaryNumbers != 0) {

            LOGGER.debug("普通暴鸡数量!=0、优选暴鸡数量 = 0、超级暴鸡数量均不为= 0");

            profitFee = profit.getSumAmout()
                    * (hiredRate * hiredNumbers / (
                    profit.getRaidDpsHiredRate() * profit.getRaidDpsNumbers()
                            + profit.getRaidAssistHiredRate() * profit.getRaidAssistNumbers()))
                    / ordinaryLocationNumbers;
        } else {
            //If普通暴鸡数量=0且优选暴鸡数量=0且超级暴鸡数量=0，佣金全部结算给打手
            //其他情况直接异常中断
            return 0;
        }

        int profitFeeFloor = (int) profitFee;
        LOGGER.info(">> 收益: {},去尾数:{}", profitFeeFloor, profitFee - profitFeeFloor);
        return profitFeeFloor;
    }

    private RPGIncomeCaculateParams getProfitParam(IncomeCaculateParams caculateParams) {

        List<Order> orders = caculateParams.getPayedOrders();
        List<OrderTeamRPGMember> members = caculateParams.getRpgTeamMembers();
        OrderTeamRPGMember member = caculateParams.getRpgMember();

        //2.计算老板订单总金额
        int sumBossAmout = orders.parallelStream()
                //过虑老板
                .filter(f -> f.getOrderItemTeamRPG().getUserIdentity()
                        .equals(UserIdentityEnum.BOSS.getCode()))
                .mapToInt(m -> m.getActualPaidAmount() + m.getDiscountAmount())
                .sum();

        if (sumBossAmout == 0) {
            LOGGER.error("无已支付的老板订单+中途退出的老板订单");
            throw new BusinessException(BizExceptionEnum.SUM_BOSS_AMOUT_FAIL);
        }

        long dpsNumbers = members.parallelStream()
                .filter(f -> (UserIdentityEnum.BAOJI.getCode() == f.getUserIdentity()
                        || UserIdentityEnum.LEADER.getCode() == f.getUserIdentity())
                        && f.getRaidLocation() == RaidLocationEnum.DPS.getCode()
                )
                .count();

        long assistNumbers = members.parallelStream()
                .filter(f -> (UserIdentityEnum.BAOJI.getCode() == f.getUserIdentity()
                        || UserIdentityEnum.LEADER.getCode() == f.getUserIdentity())
                        && f.getRaidLocation() == RaidLocationEnum.ASSIST.getCode()
                )
                .count();

        //3.获取各暴鸡等级对应的人数
        Map<Integer, Long> dpsBaojiLevelNumbers = members.stream()
                .filter(f -> f.getRaidLocation().equals(RaidLocationEnum.DPS.getCode()))
                .collect(Collectors
                        .groupingBy(OrderTeamRPGMember::getBaojiLevel, Collectors.counting()));

        Map<Integer, Long> assistBaojiLevelNumbers = members.stream()
                .filter(f -> f.getRaidLocation().equals(RaidLocationEnum.ASSIST.getCode()))
                .collect(Collectors
                        .groupingBy(OrderTeamRPGMember::getBaojiLevel, Collectors.counting()));

        //4.批量获取副本位置占比
        Integer gameCode = orders.get(0).getOrderItemTeamRPG().getGameCode();
        Integer raidCode = orders.get(0).getOrderItemTeamRPG().getRaidCode();

        Map<Integer, Double> hiredRate = getHiredRate(gameCode,
                raidCode);

        //1.取出暴鸡等级列表
        List<Integer> baojiLevels = members.parallelStream()
                //过虑暴鸡
                .filter(f -> UserIdentityEnum.BAOJI.getCode() == f.getUserIdentity()
                        || UserIdentityEnum.LEADER.getCode() == f.getUserIdentity())
                //转换暴鸡等级
                .map(OrderTeamRPGMember::getBaojiLevel)
                .collect(Collectors.toList());

        Map<Integer, Double> levelsRate = getLevelsRate(baojiLevels);

        RPGIncomeCaculateParams profitParams = new RPGIncomeCaculateParams();
        //打手人数
        profitParams.setRaidDpsNumbers((int) dpsNumbers);
        //辅助人数
        profitParams.setRaidAssistNumbers((int) assistNumbers);
        //老板支付总金额
        profitParams.setSumAmout(sumBossAmout);
        //当前暴鸡所在位置
        profitParams.setRaidLocation(member.getRaidLocation());
        //当前暴鸡等级
        profitParams.setBaojiLevel(member.getBaojiLevel());
        //dps占比
        profitParams.setRaidDpsHiredRate(hiredRate.get(RaidLocationEnum.DPS.getCode()));
        //辅助占比
        profitParams.setRaidAssistHiredRate(hiredRate.get(RaidLocationEnum.ASSIST.getCode()));
        //普通暴鸡等级系数
        if (levelsRate.get(BaojiLevelEnum.COMMON.getCode()) != null) {
            profitParams.setOrdinaryBaojiLevelRate(
                    levelsRate.get(BaojiLevelEnum.COMMON.getCode()));
        }
        //优选暴鸡等级系数
        if (levelsRate.get(BaojiLevelEnum.PREFERENCE.getCode()) != null) {
            profitParams.setExcellentBaojiLevelRate(
                    levelsRate.get(BaojiLevelEnum.PREFERENCE.getCode()));
        }
        //超级暴鸡等级系数
        if (levelsRate.get(BaojiLevelEnum.SUPER.getCode()) != null) {
            profitParams
                    .setSuperBaojiLevelRate(levelsRate.get(BaojiLevelEnum.SUPER.getCode()));
        }
        //普通暴鸡对应位置人数
        if (levelsRate.get(BaojiLevelEnum.COMMON.getCode()) != null) {

            if (dpsBaojiLevelNumbers.get(BaojiLevelEnum.COMMON.getCode()) != null) {
                profitParams.setOrdinaryBaojiDpsNumbers(
                        dpsBaojiLevelNumbers.get(BaojiLevelEnum.COMMON.getCode()).intValue());
            }

            if (assistBaojiLevelNumbers.get(BaojiLevelEnum.COMMON.getCode()) != null) {
                profitParams.setOrdinaryBaojiAssistNumbers(
                        assistBaojiLevelNumbers.get(BaojiLevelEnum.COMMON.getCode()).intValue());
            }

        }
        //优选暴鸡对应位置人数
        if (levelsRate.get(BaojiLevelEnum.PREFERENCE.getCode()) != null) {

            if (dpsBaojiLevelNumbers.get(BaojiLevelEnum.PREFERENCE.getCode()) != null) {
                profitParams.setExcellentBaojiDpsNumbers(
                        dpsBaojiLevelNumbers.get(BaojiLevelEnum.PREFERENCE.getCode()).intValue());
            }

            if (assistBaojiLevelNumbers.get(BaojiLevelEnum.PREFERENCE.getCode()) != null) {
                profitParams.setExcellentBaojiAssistNumbers(
                        assistBaojiLevelNumbers.get(BaojiLevelEnum.PREFERENCE.getCode())
                                .intValue());
            }
        }
        //超级暴鸡对应位置人数
        if (levelsRate.get(BaojiLevelEnum.SUPER.getCode()) != null) {

            if (dpsBaojiLevelNumbers.get(BaojiLevelEnum.SUPER.getCode()) != null) {
                profitParams.setSuperBaojiDpsNumbers(
                        dpsBaojiLevelNumbers.get(BaojiLevelEnum.SUPER.getCode()).intValue());
            }

            if (assistBaojiLevelNumbers.get(BaojiLevelEnum.SUPER.getCode()) != null) {
                profitParams.setSuperBaojiAssistNumbers(
                        assistBaojiLevelNumbers.get(BaojiLevelEnum.SUPER.getCode()).intValue());
            }
        }
        return profitParams;
    }

    private RPGIncomeCaculateParams getPreProfitParam(RPGInComeParams inComeParams,
            OrderTeamRPGMember member) {
        List<OrderTeamRPGMember> teamMembers = inComeParams.getTeamMembers();
        //1.计算老板订单总金额
        long sumBossAmout = teamMembers.parallelStream()
                //过虑老板
                .filter(f -> UserIdentityEnum.BOSS.getCode() == f.getUserIdentity())
                .count() * inComeParams.getTeamPrice();

        long dpsNumbers = teamMembers.parallelStream()
                .filter(f -> (UserIdentityEnum.BAOJI.getCode() == f.getUserIdentity()
                        || UserIdentityEnum.LEADER.getCode() == f.getUserIdentity())
                        && f.getRaidLocation() == RaidLocationEnum.DPS.getCode()
                )
                .count();

        long assistNumbers = teamMembers.parallelStream()
                .filter(f -> (UserIdentityEnum.BAOJI.getCode() == f.getUserIdentity()
                        || UserIdentityEnum.LEADER.getCode() == f.getUserIdentity())
                        && f.getRaidLocation() == RaidLocationEnum.ASSIST.getCode()
                )
                .count();

        //2.取出暴鸡等级列表
        List<Integer> baojiLevels = teamMembers.parallelStream()
                //过虑暴鸡
                .filter(f -> UserIdentityEnum.BAOJI.getCode() == f.getUserIdentity()
                        || UserIdentityEnum.LEADER.getCode() == f.getUserIdentity())
                //转换暴鸡等级
                .map(OrderTeamRPGMember::getBaojiLevel)
                .distinct()
                .collect(Collectors.toList());

        //3.获取的打手暴鸡(key等级,value对应的人数)
        Map<Integer, Long> dpsBaojiLevelNumbers = teamMembers.stream()
                .filter(f -> f.getRaidLocation().equals(RaidLocationEnum.DPS.getCode()))
                .collect(Collectors
                        .groupingBy(OrderTeamRPGMember::getBaojiLevel, Collectors.counting()));

        //3.获取的辅助暴鸡(key等级,value对应的人数)
        Map<Integer, Long> assistBaojiLevelNumbers = teamMembers.stream()
                .filter(f -> f.getRaidLocation().equals(RaidLocationEnum.ASSIST.getCode()))
                .collect(Collectors
                        .groupingBy(OrderTeamRPGMember::getBaojiLevel, Collectors.counting()));

        //4.获取所有暴鸡等级和对应的系数
        Map<Integer, Double> rates = getLevelsRate(baojiLevels);

        //获取游戏副本中的打手占比和辅助占比
        Map<Integer, Double> hiredRate = getHiredRate(inComeParams.getGameCode(),
                inComeParams.getRaidCode());

        RPGIncomeCaculateParams profitParams = new RPGIncomeCaculateParams();
        //老板支付总金额
        profitParams.setSumAmout((int) sumBossAmout);
        //当前暴鸡所在位置
        profitParams.setRaidLocation(member.getRaidLocation());
        //dps占比
        profitParams.setRaidDpsHiredRate(hiredRate.get(RaidLocationEnum.DPS.getCode()));
        //辅助占比
        profitParams.setRaidAssistHiredRate(hiredRate.get(RaidLocationEnum.ASSIST.getCode()));
        //普通暴鸡等级系数
        if (rates.get(BaojiLevelEnum.COMMON.getCode()) != null) {
            profitParams.setOrdinaryBaojiLevelRate(
                    rates.get(BaojiLevelEnum.COMMON.getCode()));
        }
        //优选暴鸡等级系数
        if (rates.get(BaojiLevelEnum.PREFERENCE.getCode()) != null) {
            profitParams.setExcellentBaojiLevelRate(rates.get(BaojiLevelEnum.PREFERENCE.getCode()));
        }
        //超级暴鸡等级系数
        if (rates.get(BaojiLevelEnum.SUPER.getCode()) != null) {
            profitParams.setSuperBaojiLevelRate(rates.get(BaojiLevelEnum.SUPER.getCode()));
        }
        //普通暴鸡对应位置人数
        if (rates.get(BaojiLevelEnum.COMMON.getCode()) != null) {

            if (dpsBaojiLevelNumbers.get(BaojiLevelEnum.COMMON.getCode()) != null) {
                profitParams.setOrdinaryBaojiDpsNumbers(
                        dpsBaojiLevelNumbers.get(BaojiLevelEnum.COMMON.getCode()).intValue());
            }

            if (assistBaojiLevelNumbers.get(BaojiLevelEnum.COMMON.getCode()) != null) {
                profitParams.setOrdinaryBaojiAssistNumbers(
                        assistBaojiLevelNumbers.get(BaojiLevelEnum.COMMON.getCode()).intValue());
            }

        }
        //优选暴鸡对应位置人数
        if (rates.get(BaojiLevelEnum.PREFERENCE.getCode()) != null) {

            if (dpsBaojiLevelNumbers.get(BaojiLevelEnum.PREFERENCE.getCode()) != null) {
                profitParams.setExcellentBaojiDpsNumbers(
                        dpsBaojiLevelNumbers.get(BaojiLevelEnum.PREFERENCE.getCode()).intValue());
            }

            if (assistBaojiLevelNumbers.get(BaojiLevelEnum.PREFERENCE.getCode()) != null) {
                profitParams.setExcellentBaojiAssistNumbers(
                        assistBaojiLevelNumbers.get(BaojiLevelEnum.PREFERENCE.getCode())
                                .intValue());
            }
        }
        //超级暴鸡对应位置人数
        if (rates.get(BaojiLevelEnum.SUPER.getCode()) != null) {

            if (dpsBaojiLevelNumbers.get(BaojiLevelEnum.SUPER.getCode()) != null) {
                profitParams.setSuperBaojiDpsNumbers(
                        dpsBaojiLevelNumbers.get(BaojiLevelEnum.SUPER.getCode()).intValue());
            }

            if (assistBaojiLevelNumbers.get(BaojiLevelEnum.SUPER.getCode()) != null) {
                profitParams.setSuperBaojiAssistNumbers(
                        assistBaojiLevelNumbers.get(BaojiLevelEnum.SUPER.getCode()).intValue());
            }
        }

        //当前暴鸡等级
        profitParams.setBaojiLevel(member.getBaojiLevel());
        //打手人数
        profitParams.setRaidDpsNumbers((int) dpsNumbers);
        //辅助人数
        profitParams.setRaidAssistNumbers((int) assistNumbers);

        return profitParams;
    }

    //TODO 返回值说明
    private Map<Integer, Double> getHiredRate(Integer gameCode, Integer raidCode) {
        //7.批量获取副本位置占比
        ResponsePacket<RedisGameRaid> singleGameRaid = resourceServiceClient
                .getSingleGameRaid(gameCode, raidCode);
        if (singleGameRaid.getCode() != BizExceptionEnum.SUCCESS.getErrCode()
                || singleGameRaid.getData() == null) {
            throw new BusinessException(BizExceptionEnum.SINGLE_GAME_RAID_FAIL);
        }

        double dpsHiredRate = singleGameRaid.getData().getRaidDpsRate().doubleValue();
        double assistHiredRate = singleGameRaid.getData().getRaidAssistRate().doubleValue();

        if (dpsHiredRate == 0 || assistHiredRate == 0) {
            throw new BusinessException(BizExceptionEnum.BATCH_BAOJI_RATE);
        }

        Map<Integer, Double> hiredRate = new HashMap<>(2);
        hiredRate.put(RaidLocationEnum.DPS.getCode(), dpsHiredRate);
        hiredRate.put(RaidLocationEnum.ASSIST.getCode(), assistHiredRate);

        return hiredRate;
    }

}
