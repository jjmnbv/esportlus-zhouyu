package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamOrderCancelParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamStartParams;

public interface WxSmallProgramPushService {

    /**
     * 订单结束服务微信通知
     * @param pushParams
     */
    public ResponsePacket<Void> pushOrderEnd(WxTeamEndParams pushParams);

    /**
     * 订单取消服务通知
     * @param cancelParams
     */
    public  ResponsePacket<Void> pushOrderCancel(WxTeamOrderCancelParams cancelParams);

    /**
     * 团长开车微信小程序推送，团长除外
     * @param startParams
     */
    public  ResponsePacket<Void> pushTeamStart(WxTeamStartParams startParams);

}
