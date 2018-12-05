package com.kaihei.esportingplus.trade.event;

import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGBaojiInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;
import com.kaihei.esportingplus.trade.api.enums.OrderStatusEnum;
import com.kaihei.esportingplus.trade.api.enums.OrderTeamStatus;
import com.kaihei.esportingplus.trade.data.repository.OrderItemTeamRPGRepository;
import com.kaihei.esportingplus.trade.data.repository.OrderRepository;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamRPG;
import com.kaihei.esportingplus.trade.enums.BusinessTypeEnum;
import com.kaihei.esportingplus.trade.enums.PayMentTypeEnum;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateBaojiOrderEventConsumer extends EventConsumer {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemTeamRPGRepository orderItemTeamRPGRepository;

    /**
     * 创建暴鸡订单
     * @param event
     */
/*    @Subscribe
    @AllowConcurrentEvents*/
    @Transactional(rollbackFor = Exception.class)
    public void createBaojiOrder(CreateBaojiOrderEvent event) {
        RPGTeamStartOrderVO RPGTeamStartOrderVO = event.getRPGTeamStartOrderVO();
        List<String> orderIdList = event.getOrderIdList();

        Order order = null;
        OrderItemTeamRPG orderItemTeamRPG = null;

        List<RPGBaojiInfoVO> list= RPGTeamStartOrderVO.getBaojiInfoList();
        RPGBaojiInfoVO rpgBaojiInfoVO = null;

        //循环车队队员列表，入库
        for(int i = 0; !list.isEmpty() && i<list.size(); i++) {
            //1.封装order实体信息
            order = new Order();
            rpgBaojiInfoVO = list.get(i);
            order.setUid(rpgBaojiInfoVO.getUid());
            order.setSequeue(orderIdList.get(i));
            order.setBusinessType((byte)BusinessTypeEnum.TEAM_ORDER.getCode());//订单业务类型
            order.setBusinessType((byte)BusinessTypeEnum.TEAM_ORDER.getCode());//订单业务类型
            order.setStatus((byte)OrderStatusEnum.READY.getCode());//订单状态
            order.setPrepaidAmount(0);//预付金额
            order.setActualPaidAmount(0);//实付金额
            order.setDiscountAmount(0);
            order.setPaymentType((byte)PayMentTypeEnum.WEIXIN_PAY.getCode());//支付方式

            //2.封装OrderItemTeam实体信息
            orderItemTeamRPG = BeanMapper.map(rpgBaojiInfoVO, OrderItemTeamRPG.class);
            orderItemTeamRPG.setTeamSequeue(RPGTeamStartOrderVO.getSequence());
            orderItemTeamRPG.setGameCode(RPGTeamStartOrderVO.getGameCode());
            orderItemTeamRPG.setGameName(RPGTeamStartOrderVO.getGameName());
            orderItemTeamRPG.setRaidCode(RPGTeamStartOrderVO.getRaidCode());
            orderItemTeamRPG.setRaidName(RPGTeamStartOrderVO.getRaidName());
            orderItemTeamRPG.setUserBaojiLevel(rpgBaojiInfoVO.getBaojiLevel());
            orderItemTeamRPG.setPrice(0);
            orderItemTeamRPG.setTeamStatus((byte)OrderTeamStatus.ALREADY_STATUS.getCode());
            orderItemTeamRPG.setZoneAcrossCode(RPGTeamStartOrderVO.getZoneAcrossCode());
            orderItemTeamRPG.setZoneAcrossName(RPGTeamStartOrderVO.getZoneAcrossName());
            //暴鸡副本位信息
            orderItemTeamRPG.setRaidLocationCode(rpgBaojiInfoVO.getRaidLocationCode());
            orderItemTeamRPG.setRaidLocationName(rpgBaojiInfoVO.getRaidLocationName());
            //3.依次入库
            orderRepository.insertOrder(order);
            orderItemTeamRPG.setOrderId(order.getId());
            orderItemTeamRPGRepository.insertSelective(orderItemTeamRPG);
        }
    }
}
