package com.kaihei.esportingplus.gamingteam.event.weixin;

import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamOrderPushData;
import com.kaihei.esportingplus.trade.api.vo.OrderVO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.stereotype.Component;

/**
 * 创建微信订单
 * @author liangyi
 */
@Component
public class WeXinOrderSupport {

    /**
     * 组装微信服务通知所需参数, 过滤多余的老板订单
     * @param wxOrderList
     * @param orderVOList
     * @param bossOrderList
     */
    public void filterWeXinBossOrder(List<WxTeamOrderPushData> wxOrderList,
            List<OrderVO> orderVOList, List<OrderVO> bossOrderList) {
        for (OrderVO orderVO : orderVOList) {
            if (UserIdentityEnum.BOSS.getCode()
                     == orderVO.getOrderItemTeam().getUserIdentity()) {
                // 过滤老板订单, 老板可能会有多个订单, 此处去重后取最新的一个
                bossOrderList.add(orderVO);
            } else {
                WxTeamOrderPushData wxOrder = createWxTeamOrderPush(orderVO);
                wxOrderList.add(wxOrder);
            }
        }
        if (ObjectTools.isNotEmpty(bossOrderList)) {
            // 去重老板订单, 按时间取最新的
            List<OrderVO> bossOrderVOS = distinctOrder(bossOrderList);
            for (OrderVO bossOrderVO : bossOrderVOS) {
                WxTeamOrderPushData wxOrder = createWxTeamOrderPush(bossOrderVO);
                wxOrderList.add(wxOrder);
            }
        }
    }

    /**
     * 通过订单创建微信订单
     * @param orderVO
     * @return
     */
    public WxTeamOrderPushData createWxTeamOrderPush(OrderVO orderVO) {
        WxTeamOrderPushData wxOrder = new WxTeamOrderPushData();
        wxOrder.setOrderSequence(orderVO.getSequeue());
        wxOrder.setUid(orderVO.getUid());
        wxOrder.setOrderMoney(orderVO.getOrderItemTeam().getPrice());
        return wxOrder;
    }

    /**
     * 去重 uid, 按时间降序排列
     * @param orderList
     * @return
     */
    public List<OrderVO> distinctOrder(List<OrderVO> orderList) {
        // 先按时间降序排序
        orderList.sort(new Comparator<OrderVO>() {
            @Override
            public int compare(OrderVO o1, OrderVO o2) {
                return o2.getGmtCreate().compareTo(o1.getGmtCreate());
            }
        });
        // uid 去重
        Set<OrderVO> orderVOS = new TreeSet<>(new Comparator<OrderVO>() {
            @Override
            public int compare(OrderVO o1, OrderVO o2) {
                return o1.getUid().compareTo(o2.getUid());
            }
        });
        orderVOS.addAll(orderList);
        return new ArrayList<>(orderVOS);
    }

}
