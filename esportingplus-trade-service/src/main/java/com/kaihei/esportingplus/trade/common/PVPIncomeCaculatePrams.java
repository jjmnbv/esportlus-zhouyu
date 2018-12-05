package com.kaihei.esportingplus.trade.common;

import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class PVPIncomeCaculatePrams implements Serializable {

    private static final long serialVersionUID = -6002281640539042410L;

    //车队模式
    private SettlementTypeEnum settlementTypeEnum;
    //老板支付总金额
    private int bossPaySum;
    //当前暴鸡等级系数
    private double currentBaojiLevelRate;
    //所有暴鸡等级系数总和
    private double baojiLevelRateSum;
    //暴娘人数
    private int baoNiangNumbers;
    //上分净胜局数或陪玩已打小时
    private double battleSum;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
