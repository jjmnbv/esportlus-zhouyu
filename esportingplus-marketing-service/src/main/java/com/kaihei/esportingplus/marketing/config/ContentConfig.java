package com.kaihei.esportingplus.marketing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author zl.zhao
 * @description:文案配置
 * @date: 2018/11/27 23:04
 */
@Configuration
public class ContentConfig {

    @Value("${marketing.free.coupons.no.chances}")
    private String couponsNoChances;

    @Value("${marketing.free.coupons.team.drive}")
    private String couponsTeamDrive;

    @Value("${marketing.free.friend.finish.order.im}")
    private String friendFinishOrderIm;

    @Value("${marketing.friend.invit.friend.im}")
    private String friendInvitFriendIm;

    public String getFriendInvitFriendIm() {
        return friendInvitFriendIm;
    }

    public void setFriendInvitFriendIm(String friendInvitFriendIm) {
        this.friendInvitFriendIm = friendInvitFriendIm;
    }

    public String getFriendFinishOrderIm() {
        return friendFinishOrderIm;
    }

    public void setFriendFinishOrderIm(String friendFinishOrderIm) {
        this.friendFinishOrderIm = friendFinishOrderIm;
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
