package com.kaihei.esportingplus.user.thirdparty.service;

import com.google.common.collect.Maps;
import com.kaihei.esportingplus.api.feign.ChickenpointUseConfigClient;
import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.api.feign.FreeTeamTypeServiceClient;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.payment.api.feign.WithdrawServiceClient;
import com.kaihei.esportingplus.user.UserServiceApplication;
import com.kaihei.esportingplus.user.api.vo.SendFreeTeamMessageVo;
import com.kaihei.esportingplus.user.api.vo.SendFreeTeamMsgDtlVo;
import com.kaihei.esportingplus.user.api.vo.SendMessageDataVo;
import com.kaihei.esportingplus.user.api.vo.SendMessageVo;
import com.kaihei.esportingplus.user.external.message.SendMessageService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SendMessageServiceImplTest {

    @Autowired
    private SendMessageService messageService;

    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }


    @Autowired
    private ChickenpointUseConfigClient chickenpointUseConfigClient;

    @Autowired
    private WithdrawServiceClient withdrawServiceClient;

    @Autowired
    private FreeTeamTypeServiceClient teamTypeServiceClient;

    @Autowired
    private DictionaryClient dictionaryClient;


    @Test
    public void sendMessage() {
        SendMessageVo messageVo = new SendMessageVo();
        messageVo.setSender("bjdj_system");
        List<String> reciever = Lists.newArrayList();
        //reciever.add("13a8f0e4");
        reciever.add("2");
        reciever.add("3");
        reciever.add("4");
        messageVo.setReciever(reciever);
        messageVo.setTemplateId("1");
        Map<String,Object> dataVo = Maps.newHashMap();
        dataVo.put("content","test");
        dataVo.put("extra","test");
        messageVo.setData(dataVo);
        messageVo.setToSelf(Boolean.FALSE);
        messageService.sendMessage(messageVo);
    }

    @Test
    public void SendFreeTeamMessage() {
        String game = null;
        ResponsePacket<FreeTeamTypeDetailVO> data =
                teamTypeServiceClient.getFreeTeamTypeById(222);
        if(data == null){
//            logger.info("cmd=SendMessageServiceImpl.send | msg={} | req={}",
//                    "sendMessage success", json);
            game = data.getData().getGame().getName();
        }
        ResponsePacket<DictBaseVO<Object>> s = dictionaryClient.findById(49);
        String dan = s.getData().getName();
        FreeTeamTypeDetailVO vo1 =  data.getData();
        SendFreeTeamMessageVo vo = new SendFreeTeamMessageVo();
        vo.setReciever(Arrays.asList("bab792e1"));
        List<SendMessageDataVo> dataArr = new LinkedList<>();
        SendFreeTeamMsgDtlVo msg = new SendFreeTeamMsgDtlVo("鸡分奖励-获得五星好评",
                "", dataArr, 3, "501aa796");
        vo.setData(msg);

        SendMessageDataVo awardPoint = new SendMessageDataVo("奖励鸡分", "10");
        SendMessageDataVo reviewer = new SendMessageDataVo("评价用户","测试");
        Integer starNum = Integer.parseInt("50")/10;//入参值为10-50 需计算星数
        SendMessageDataVo star = new SendMessageDataVo("评价等级", starNum.toString());
        SendMessageDataVo orderContent = new SendMessageDataVo("订单信息", game +" | "+
                dan +" | "+ "1局");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SendMessageDataVo orderTime = new SendMessageDataVo("订单时间", sdf.format(new Date()));
        dataArr.add(awardPoint);
        dataArr.add(reviewer);
        dataArr.add(star);
        dataArr.add(orderContent);
        dataArr.add(orderTime);


        /*String s = "报告老板！您的好友成功邀请%s位新朋友加入暴鸡电竞，奖励您%s次免费车队机会，好友邀请越多，奖励越多哦~";
        String content = String.format(s, 100, 10);

        SendFreeTeamMessageVo vo = new SendFreeTeamMessageVo();
        vo.setReciever(Arrays.asList("bab792e1"));

        //成功邀请新人通知
        List<SendMessageDataVo> dataArr = new LinkedList<>();
        SendFreeTeamMsgDtlVo msg = new SendFreeTeamMsgDtlVo("邀请好友奖励通知",content,dataArr, MessageSubType.FREE_TEAM.getCode(),"501aa796");
        vo.setData(msg);

        SendMessageDataVo nickName =new SendMessageDataVo("好友昵称","boss");
        SendMessageDataVo awardFrequency = new SendMessageDataVo("奖励次数","3");
        SendMessageDataVo remainFrequency = new SendMessageDataVo("剩余次数","5");
        dataArr.add(nickName);
        dataArr.add(awardFrequency);
        dataArr.add(remainFrequency);*/

        messageService.SendFreeTeamMessage(vo);
    }
}