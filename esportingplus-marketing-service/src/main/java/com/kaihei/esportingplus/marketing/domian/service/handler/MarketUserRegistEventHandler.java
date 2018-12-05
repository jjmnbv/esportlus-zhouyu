package com.kaihei.esportingplus.marketing.domian.service.handler;

import com.kaihei.esportingplus.api.enums.DictionaryCodeEnum;
import com.kaihei.esportingplus.api.feign.TaskConfigServiceClient;
import com.kaihei.esportingplus.api.vo.freeteam.InvitionShareConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.marketing.api.event.UserRegistEvent;
import com.kaihei.esportingplus.marketing.data.repository.MarketUserInvitingRelationRepository;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserInvitingRelation;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserTaskAccumualte;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserFreeCouponsService;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserTaskService;
import com.kaihei.esportingplus.marketing.event.FriendInvitFriendIMEvent;
import com.kaihei.esportingplus.user.api.feign.UserInfoServiceClient;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 邀请注册事件会触发2个任务： 1.邀请人免单操作  2.邀请人达到奖励人数对邀请人的邀请人进行免单操作
 *
 * @Auther: chen.junyong
 * @Date: 2018-10-09 10:04
 * @Description:
 */
@Component("marketUserRegistEventHandler")
public class MarketUserRegistEventHandler extends AbstractUserEventHandler<UserRegistEvent> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MarketUserInvitingRelationRepository marketUserInvitingRelationRepository;

    @Autowired
    private UserInfoServiceClient userInfoServiceClient;

    @Autowired
    private TaskConfigServiceClient taskConfigServiceClient;

    @Autowired
    private MarketUserTaskService marketUserTaskService;

    @Autowired
    private MarketUserFreeCouponsService marketUserFreeCouponsService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean process(UserRegistEvent userRegistEvent) {
        return invitTask(userRegistEvent);
    }

    private boolean invitTask(UserRegistEvent userRegistEvent) {
        String json = FastJsonUtils.toJson(userRegistEvent);
        ResponsePacket<MembersUserVo> userResp = userInfoServiceClient
                .getMembersUserByUid(userRegistEvent.getUid());
        if (!userResp.responseSuccess()) {
            logger.error(
                    "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | req={}",
                    "用户服务熔断", json);
            return true;
        }
        if (userResp.getData() == null) {
            logger.error(
                    "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | uid={}",
                    "没有找到邀请人对应信息，不进行奖励操作", userRegistEvent.getUid());
            return true;
        }
        Integer userId = userResp.getData().getId();
        userRegistEvent.setUserId(userId);
        ResponsePacket<MembersUserVo> invitedUserIdResp = userInfoServiceClient
                .getMembersUserByUid(userRegistEvent.getInvitedUid());
        if (!invitedUserIdResp.responseSuccess()) {
            logger.error(
                    "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | req={}",
                    "用户服务熔断", json);
            return true;
        }
        if (invitedUserIdResp.getData() == null) {
            logger.error(
                    "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | uid={}",
                    "没有找到被邀请人对应信息，不进行奖励操作", userRegistEvent.getInvitedUid());
            return true;
        }
        Integer invitedUserId = invitedUserIdResp.getData().getId();
        userRegistEvent.setInvitedUserId(invitedUserId);
        //1.检查是不是已经被邀请过了
        MarketUserInvitingRelation relationRecord = new MarketUserInvitingRelation();
        relationRecord.setUserId(invitedUserId);
        MarketUserInvitingRelation relation = marketUserInvitingRelationRepository
                .selectOne(relationRecord);
        if (relation != null) {
            logger.info("cmd=MarketUserRegistEventHandler.process | msg=该用户已经被邀请过了 | req={}",
                    json);
            return false;
        }
        relationRecord.setInvitingUserid(userId);
        //2插入邀请关系记录
        marketUserInvitingRelationRepository.insertSelective(relationRecord);

        List<InvitionShareConfigVo> configList = getConfigs();// 从别的系统找出邀请配置

//  暴暴没有了新用户注册奖励      processNewRegistTask(userRegistEvent, configList);

        //任务一：执行邀请好友免单奖励
        processInvitFriendTask(userRegistEvent, configList, relationRecord);

        //任务二：执行好友邀请好友免单奖励
        processFriendInvitFriendTask(userRegistEvent, configList, relationRecord);
        return true;
    }

    /*private boolean processNewRegistTask(UserRegistEvent userRegistEvent,
            List<InvitionShareConfigVo> configList) {
        //TODO 调用配置服务
        InvitionShareConfigVo newRegistConfig = null;*//*getTypeConfig(configList,
                ShareCategoryCodeEnum.USER_REGISTER.getCode());*//*

        if (newRegistConfig == null) {
            logger.info(
                    "cmd=MarketUserRegistEventHandler.processNewRegistTask | msg=没有找到新用户注册配置任务 | req={}",
                    FastJsonUtils.toJson(userRegistEvent));
        } else if (UserRegistEvent.STATUS_ON == newRegistConfig.getStatus()) {

            //新用户任务：新用户注册增加免单机会
            if (newRegistConfig.getRewardFreeCount() != 0) {
                MarketUserTaskAccumualte acc = marketUserTaskService
                        .getAccumulate(userRegistEvent.getUserId(),
                                MarketUserTaskAccumualte.USER_REGIST);
                if (acc != null) {
                    logger.info(
                            "cmd=MarketUserRegistEventHandler.processNewRegistTask | msg=该用户已经奖励过新用户奖励 | req={}",
                            FastJsonUtils.toJson(userRegistEvent));
                    return true;
                }

                boolean succ = false;// TODO 增加免费次数freeTeamService.callPythonToIncreFreeTimes(userRegistEvent.getUid(),
//                        newRegistConfig.getRewardFreeCount());
                if (succ) {
                    acc = new MarketUserTaskAccumualte();
                    acc.setUserId(userRegistEvent.getUserId());
                    acc.setType(MarketUserTaskAccumualte.USER_REGIST);
                    acc.setAccumulate(0);
                    acc.setAwardSnapshot(0);
                    acc.setBatchDate(newRegistConfig.getOnlineTime());
                    marketUserTaskService.createAccumulate(acc);
                }
            }
        } else {
            logger.info(
                    "cmd=MarketUserRegistEventHandler.processNewRegistTask | msg=新用户注册配置任务下线了 | req={}",
                    FastJsonUtils.toJson(userRegistEvent));
        }
        return true;

    }*/

    private void processFriendInvitFriendTask(UserRegistEvent userRegistEvent,
            List<InvitionShareConfigVo> configList,
            MarketUserInvitingRelation relation) {
        //TODO 调用配置服务
        InvitionShareConfigVo friendInviteFriendConfig = getTypeConfig(configList,
                DictionaryCodeEnum.FRIEND_INVITE_FRIENDS.getCode());
        if (friendInviteFriendConfig == null) {
            logger.info(
                    "cmd=MarketUserRegistEventHandler.processFriendInvitFriendTask | msg=没有找到对应配置任务 | req={}",
                    FastJsonUtils.toJson(userRegistEvent));
        } else if (UserRegistEvent.STATUS_ON == friendInviteFriendConfig.getStatus()) {
            //找出邀请人的邀请人
            MarketUserInvitingRelation parentRelationRecord = new MarketUserInvitingRelation();
            parentRelationRecord.setUserId(relation.getInvitingUserid());
            MarketUserInvitingRelation parentInviting = marketUserInvitingRelationRepository
                    .selectOne(parentRelationRecord);
            if (parentInviting != null) {
                //5. 对邀请人的邀请人免单操作
                ResponsePacket<MembersUserVo> parentUserResp = userInfoServiceClient
                        .getMembersUserById(parentInviting.getInvitingUserid());
                if (!parentUserResp.responseSuccess()) {
                    logger.error(
                            "cmd=MarketUserRegistEventHandler.processFriendInvitFriendTask | msg={} | req={}",
                            "用户服务熔断", JacksonUtils.toJson(userRegistEvent));
                    return;
                }
                if (parentUserResp.getData() == null) {
                    logger.error(
                            "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | uid={}",
                            "邀请人没有找到邀请人，不进行奖励操作", relation.getInvitingUserid());
                    return;
                }

                boolean succ = processTypeAward(userRegistEvent, friendInviteFriendConfig,
                        parentInviting, MarketUserTaskAccumualte.FRIEND_INVIT_FRIEND);
            }
        } else {
            logger.info("cmd=MarketUserRegistEventHandler.process | msg=好友邀请好友免单任务下线了 | req={}",
                    FastJsonUtils.toJson(userRegistEvent));
        }

    }

    private void processInvitFriendTask(UserRegistEvent userRegistEvent,
            List<InvitionShareConfigVo> configList,
            MarketUserInvitingRelation relation) {
        // TODO 调用配置服务
        InvitionShareConfigVo inviteFriendConfig = getTypeConfig(configList,
                DictionaryCodeEnum.INVITE_FRIENDS_REWARD.getCode());
        if (inviteFriendConfig == null) {
            logger.info("cmd=MarketUserRegistEventHandler.process | msg=没有找到邀请好友奖励配置任务 | req={}",
                    FastJsonUtils.toJson(userRegistEvent));
        } else if (UserRegistEvent.STATUS_ON == inviteFriendConfig.getStatus()) {
            boolean succ = processTypeAward(userRegistEvent, inviteFriendConfig, relation,
                    MarketUserTaskAccumualte.INVIT_FRIEND);
            if (succ) {
                marketUserTaskService.incrStatistics(userRegistEvent.getUserId(), 1L, null);
            }
        } else {
            logger.info("cmd=MarketUserRegistEventHandler.process | msg=邀请好友奖励配置任务下线了 | req={}",
                    FastJsonUtils.toJson(userRegistEvent));
        }
    }

    private boolean processTypeAward(UserRegistEvent event, InvitionShareConfigVo config,
            MarketUserInvitingRelation relation,
            int invitType) {
        MarketUserTaskAccumualte accumulate = marketUserTaskService
                .getOrCreateAccumulate(event.getUserId(), invitType,
                        config.getOnlineTime());//4：好友完成车队奖励
        int accumulateCount = 0, awardSnapshot = 0, incrAmount = 1;
        if (accumulate.getBatchDate().compareTo(config.getOnlineTime()) == 0) {
            accumulateCount = accumulate.getAccumulate() + incrAmount;
            awardSnapshot = accumulate.getAwardSnapshot();
        } else {
            marketUserTaskService.createAccumulateHistroyByAcc(accumulate);//把记录变为历史记录
            accumulate.setBatchDate(config.getOnlineTime());
            accumulateCount = incrAmount;
        }

        int calculateCount = accumulateCount - awardSnapshot;
        if (calculateCount < 0) {
            calculateCount = 0;
        }
        int mod = 0, times = 0, configCount = Integer.valueOf(config.getInviteCount()),
                awardCount = Integer.valueOf(config.getRewardFreeCount());
        accumulate.setAccumulate(accumulateCount);
        if (configCount != 0) {
            mod = calculateCount % configCount;
            times = calculateCount / configCount;
        }
        int totalAward = times * awardCount;
        if (totalAward != 0) {
            accumulate.setAwardSnapshot(accumulateCount - mod);//有兑奖情况出现时才更新兑奖快照
            // 好友达到奖励人数对邀请人进行免单操作
            ResponsePacket<MembersUserVo> userResp = userInfoServiceClient
                    .getMembersUserById(relation.getInvitingUserid());
            if (!userResp.responseSuccess()) {
                logger.error(
                        "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | req={}",
                        "用户服务熔断", JacksonUtils.toJson(event));
                return false;
            }
            if (userResp.getData() == null) {
                logger.error(
                        "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | uid={}",
                        "该用户没有找到对应信息，不进行奖励操作", relation.getInvitingUserid());
                return false;
            }
            MembersUserVo parentUser = userResp.getData();
            marketUserFreeCouponsService
                    .addUserFreeCoupons(parentUser.getUid(), totalAward, null, 1,
                            2);
            //好友邀请好友任务有奖励时，发送IM
            if (MarketUserTaskAccumualte.FRIEND_INVIT_FRIEND == invitType) {
                //异步发送邀请好友奖励通知
                EventBus.post(
                        new FriendInvitFriendIMEvent(config.getInviteCount(),
                                config.getRewardFreeCount(),
                                relation.getUserId(),
                                relation.getInvitingUserid()));
            }
        }
        marketUserTaskService.updateAccumulate(accumulate);
        return true;
    }

    private List<InvitionShareConfigVo> getConfigs() {
        ResponsePacket<List<InvitionShareConfigVo>> resp = taskConfigServiceClient
                .findAllShareTaskConfig();
        if (resp == null || resp.getData() == null) {
            logger.error(
                    "cmd=MarketUserRegistEventHandler.getConfigs | msg=调用分享任务配置返回空 | resp={}",
                    JacksonUtils.toJson(resp));
            return null;
        }
        return resp.getData();
    }

    private InvitionShareConfigVo getTypeConfig(List<InvitionShareConfigVo> list, String code) {
        if (list == null) {
            return null;
        }
        InvitionShareConfigVo config = null;
        for (InvitionShareConfigVo vo : list) {
            if (code.equals(vo.getCode())) {
                config = vo;
                break;
            }
        }
        return config;
    }

}
