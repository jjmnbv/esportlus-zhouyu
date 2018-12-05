package com.kaihei.esportingplus.trade.event;

import com.kaihei.esportingplus.common.event.Event;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;

import java.util.List;

/**
 * 創建暴鸡訂單的消息通知參數
 */

public class CreateBaojiOrderEvent implements Event {
    private RPGTeamStartOrderVO RPGTeamStartOrderVO;
    private List<String> orderIdList;

    public CreateBaojiOrderEvent(){}

    public CreateBaojiOrderEvent(RPGTeamStartOrderVO RPGTeamStartOrderVO,List<String> orderIdList){
        this.RPGTeamStartOrderVO = RPGTeamStartOrderVO;
        this.orderIdList = orderIdList;
    }

    public RPGTeamStartOrderVO getRPGTeamStartOrderVO() {
        return RPGTeamStartOrderVO;
    }

    public void setRPGTeamStartOrderVO(RPGTeamStartOrderVO RPGTeamStartOrderVO) {
        this.RPGTeamStartOrderVO = RPGTeamStartOrderVO;
    }

    public List<String> getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(List<String> orderIdList) {
        this.orderIdList = orderIdList;
    }
}
