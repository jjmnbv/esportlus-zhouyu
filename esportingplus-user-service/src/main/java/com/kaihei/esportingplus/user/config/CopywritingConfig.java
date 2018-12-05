package com.kaihei.esportingplus.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author zl.zhao
 * @description:文案配置
 * @date: 2018/11/27 23:04
 */
@Configuration
public class CopywritingConfig {
    @Value("${user.point.item.content.exchange}")
    private String pointExchange;

    @Value("${user.point.item.content.team.drive}")
    private String pointTeamDrive;

    @Value("${user.point.item.content.obtainstar}")
    private String pointObtainstar;

    @Value("${user.free.coupons.no.chances}")
    private String couponsNoChances;

    @Value("${user.free.coupons.team.drive}")
    private String couponsTeamDrive;

    public String getPointExchange() {
        return pointExchange;
    }

    public void setPointExchange(String pointExchange) {
        this.pointExchange = pointExchange;
    }

    public String getPointTeamDrive() {
        return pointTeamDrive;
    }

    public void setPointTeamDrive(String pointTeamDrive) {
        this.pointTeamDrive = pointTeamDrive;
    }

    public String getPointObtainstar() {
        return pointObtainstar;
    }

    public void setPointObtainstar(String pointObtainstar) {
        this.pointObtainstar = pointObtainstar;
    }

    public String getCouponsNoChances() {
        return couponsNoChances;
    }

    public void setCouponsNoChances(String couponsNoChances) {
        this.couponsNoChances = couponsNoChances;
    }

    public String getCouponsTeamDrive() {
        return couponsTeamDrive;
    }

    public void setCouponsTeamDrive(String couponsTeamDrive) {
        this.couponsTeamDrive = couponsTeamDrive;
    }
}
