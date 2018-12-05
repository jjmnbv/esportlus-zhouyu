package com.kaihei.esportingplus.marketing.domian.service.handler;

import com.kaihei.esportingplus.api.enums.DictionaryCodeEnum;
import com.kaihei.esportingplus.api.feign.ChickenpointGainConfigClient;
import com.kaihei.esportingplus.api.feign.TaskConfigServiceClient;
import com.kaihei.esportingplus.api.vo.freeteam.InvitionShareConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.marketing.api.event.TeamFinishOrderEvent;
import com.kaihei.esportingplus.marketing.api.event.TeamMember;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserInvitingRelation;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserTaskAccumualte;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserFreeCouponsService;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserTaskService;
import com.kaihei.esportingplus.marketing.event.FriendFinishedTeamIMEvent;
import com.kaihei.esportingplus.riskrating.api.feign.FreeTeamClient;
import com.kaihei.esportingplus.user.api.feign.UserDataServiceClient;
import com.kaihei.esportingplus.user.api.feign.UserInfoServiceClient;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author xiekeqing
 */
@Component("teamFreeFinishEventHandler")
public class TeamFreeFinishEventHandler extends
        AbstractUserEventHandler<TeamFinishOrderEvent> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int STATUS_OFF = 0;

    @Autowired
    private MarketUserFreeCouponsService marketUserFreeCouponsService;
    @Autowired
    private ChickenpointGainConfigClient chickenpointGainConfigClient;

    @Autowired
    private TaskConfigServiceClient taskConfigServiceClient;

    @Autowired
    private MarketUserTaskService marketUserTaskService;

    @Autowired
    private UserInfoServiceClient userInfoServiceClient;

    @Autowired
    private FreeTeamClient freeTeamClient;

    @Autowired
    private UserDataServiceClient userDataServiceClient;

    @Override
    public boolean process(TeamFinishOrderEvent teamFreeEvent) {
        //处理鸡分任务
// 任务里不加鸡分了        processPointTask(teamFreeEvent);

        //处理完成车队任务
        processFinishedTeamTask(teamFreeEvent);

        //调用风控
        processRiskrating(teamFreeEvent);

        /*processAddUserFreeDataInfo(teamFreeEvent);*/
        return false;
    }

    /**
     * 开免费车队邀请任务
     *
     * @param teamFreeEvent 免费车队事件相关参数
     */
    private boolean processFinishedTeamTask(TeamFinishOrderEvent teamFreeEvent) {
        String json = JacksonUtils.toJson(teamFreeEvent);
        // 从别的系统找出邀请配置
        //TODO 调用配置服务
        ResponsePacket<InvitionShareConfigVo> resp = taskConfigServiceClient
                .findShareTaskConfig(
                        DictionaryCodeEnum.FRIENDS_FINISH_TEAM_REWARD.getCode());
        if (resp == null || resp.getData() == null) {
            logger.info("cmd=TeamFreeFinishEventHandler.process | msg=没有找到好友完成车队奖励配置任务 | req={}",
                    json);
        }
        InvitionShareConfigVo config = resp.getData();
        if (STATUS_OFF == config.getStatus()) {//1.检查奖励任务是不是已经下线了
            logger.info(
                    "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg=任务下线了 | req={}",
                    json);
            return false;
        }
        int finishedTime = 1;
        for (TeamMember member : teamFreeEvent.getTeamMemberVOS()) {
            ResponsePacket<MembersUserVo> userResp = userInfoServiceClient
                    .getMembersUserByUid(member.getUid());
            if (!userResp.responseSuccess()) {
                logger.error(
                        "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | req={}",
                        "用户服务熔断", json);
                continue;
            }
            if (userResp.getData() == null) {
                logger.error(
                        "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | uid={}",
                        "该用户没有找到对应信息，不进行奖励操作", member.getUid());
                continue;
            }
            Integer userId = userResp.getData().getId();
            //找出邀请关系
            MarketUserInvitingRelation relation = marketUserTaskService
                    .findRelationByUserId(userId);
            if (relation == null) {
                logger.info(
                        "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg=该用户没有邀请人，不进行奖励操作 | userId={}",
                        userId);
                continue;
            }
            MarketUserTaskAccumualte accumulate = marketUserTaskService
                    .getOrCreateAccumulate(userId,
                            MarketUserTaskAccumualte.FINISHED_TEAM,
                            config.getOnlineTime());//4：好友完成车队奖励
            if (accumulate.getAwardSnapshot() != 0) {
                //好友完成车队任务只奖励一次，有奖励快照则跳过
                continue;
            }

            int accumulateCount = 0;
            if (accumulate.getBatchDate().compareTo(config.getOnlineTime()) == 0) {
                accumulateCount = accumulate.getAccumulate() + finishedTime;
            } else {
                marketUserTaskService.createAccumulateHistroyByAcc(accumulate);//把记录变为历史记录
                accumulate.setBatchDate(config.getOnlineTime());
                accumulateCount = finishedTime;
            }

            int awardCount = Integer.valueOf(config.getRewardFreeCount());
            accumulate.setAccumulate(accumulateCount);
            if (awardCount != 0) {
                accumulate.setAwardSnapshot(accumulateCount);//有兑奖情况出现时才更新兑奖快照
                // 好友达到奖励人数对邀请人进行免单操作
                ResponsePacket<MembersUserVo> parentUserResp = userInfoServiceClient
                        .getMembersUserById(relation.getInvitingUserid());
                if (!parentUserResp.responseSuccess()) {
                    logger.error(
                            "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | req={}",
                            "用户服务熔断", json);
                    continue;
                }
                if (parentUserResp.getData() == null) {
                    logger.error(
                            "cmd=TeamFreeFinishEventHandler.processFinishedTeamTask | msg={} | uid={}",
                            "没有找到邀请人对应信息，不进行奖励操作", member.getUid());
                    continue;
                }
                marketUserFreeCouponsService
                        .addUserFreeCoupons(parentUserResp.getData().getUid(), awardCount, null, 1,
                                2);
                //异步通知
                EventBus.post(new FriendFinishedTeamIMEvent(awardCount, userId,
                        relation.getInvitingUserid()));
            }
            marketUserTaskService.updateAccumulate(accumulate);
        }
        return true;
    }

    /**
     * 开免费车队鸡分奖励，具体过程如下三步
     * <p>1.解析车队成员，分类暴鸡、老板身份</p>
     * <p>2.从配置中心获取奖励的鸡分值</p>
     * <p>3.调用增加鸡分接口</p>
     *
     * @param teamFreeEvent 免费车队事件相关参数
     */
   /* private boolean processPointTask(TeamFreeEvent teamFreeEvent) {
        //解析车队成员，返回结构为<暴鸡,老板列表>
        Pair<TeamMember, List<TeamMember>> teamMemberPair = TeamFreeParser.parseTeamMembers(
                teamFreeEvent);

        //遍历游戏局数,获取奖励鸡分，因只有一个暴鸡，所以总奖励鸡分值全部分配给该暴鸡
        Integer awardPointNumber = 0;
        for (Integer gameResult : teamFreeEvent.getGameResult()) {
            awardPointNumber = awardPointNumber + getAwardPoint(teamFreeEvent.getFreeTeamType(),
                    gameResult, teamMemberPair);
        }
        //根据uid查询userID
        ResponsePacket<MembersUserVo> userResp = userInfoServiceClient.getMembersUserByUid(teamMemberPair.getLeft().getUid());
        Integer userId = userResp.getData().getId();
        //增加鸡分
        userPointServiceClient.incrPoint(userId, awardPointNumber,
                UserPointItemType.TEAM_DRIVE.getCode(), new Integer(0), teamFreeEvent.getSlug());

        return true;
    }*/

    /**
     * 根据车队游戏配置信息以及结果获取对应的奖励鸡分值
     * <p>游戏类型+游戏结果+暴鸡等级+老板段位</p>
     * <p>返回值为null说明未配置对应奖励值</p>
     *
     * @param freeTeamType 车队类型类型
     * @param gameResult 游戏结果, 0: 胜利 1: 失败 2: 未打
     * @param teamMemberPair 车队成员对，结构为<暴鸡,老板列表>
     * @return Integer
     */
    /*private Integer getAwardPoint(String freeTeamType, Integer gameResult,
            Pair<TeamMember, List<TeamMember>> teamMemberPair) {
        final AwardPointNumberHolder awardPointNumberHolder = new AwardPointNumberHolder();

        //遍历老板列表，分别获取奖励鸡分，计算总值
        teamMemberPair.getRight().forEach(member -> {
            Integer awardPointNumber = getAwardPoint(freeTeamType, gameResult,
                    teamMemberPair.getLeft().getLevel(), member.getDan());
            if (awardPointNumber != null) {
                //奖励总鸡分值为空则以当次值初始化，不为空则累加
                Integer totalNumber =
                        (awardPointNumberHolder.getAwardPointNumber() == null) ? awardPointNumber
                                : awardPointNumberHolder.getAwardPointNumber() + awardPointNumber;
                awardPointNumberHolder.setAwardPointNumber(totalNumber);
            }
        });

        return awardPointNumberHolder.getAwardPointNumber();
    }*/

    /**
     * 奖励鸡分值装载类，使用lambda无法修改外部参数时，使用装载类来实现。
     */
    class AwardPointNumberHolder {

        private Integer awardPointNumber = null;

        public Integer getAwardPointNumber() {
            return awardPointNumber;
        }

        public void setAwardPointNumber(Integer awardPointNumber) {
            this.awardPointNumber = awardPointNumber;
        }
    }

    /**
     * 根据游戏信息以及老板段位查询对应奖励积分
     * <p>游戏类型+游戏结果+暴鸡等级+老板段位</p>
     * <p>返回值为null说明未配置对应奖励值</p>
     *
     * @param gameResult 游戏结果, 0: 胜利 1: 失败 2: 未打
     * @param level 暴鸡等级
     * @param dan 老板段位
     * @return Integer
     */
    private Integer getAwardPoint(String freeTeamType, Integer gameResult, String level,
            String dan) {
        //读取鸡分奖励配置

        //TODO 写死不报错、由前回传
        int settlementType = 1;

        ResponsePacket<Integer> awardPoint = chickenpointGainConfigClient.
                findChickenpointGainConfigValue(
                        freeTeamType == null ? null : Integer.valueOf(freeTeamType),
                        dan == null ? null : Integer.valueOf(dan),
                        gameResult, level == null ? null : Integer.valueOf(level), settlementType);
        if (null == awardPoint) {
            throw new BusinessException(BizExceptionEnum.USER_AWARD_POINT_NOT_FOUND);
        }

        return awardPoint.getData();
    }

    /**
     * 调用风控
     *
     * @param teamFreeEvent 免费车队事件相关参数
     */
    private boolean processRiskrating(TeamFinishOrderEvent teamFreeEvent) {
        //解析车队成员，返回结构为<暴鸡,老板列表>
        Pair<TeamMember, List<TeamMember>> teamMemberPair = TeamFreeParser.parseTeamMembers(
                teamFreeEvent);

        //拼接所有老板uid
        StringBuffer uid = new StringBuffer();
        for (TeamMember member : teamMemberPair.getRight()) {
            uid.append(member.getUid()).append(",");
        }
        String uids = uid.substring(0, uid.lastIndexOf(","));

        //调用风控
        freeTeamClient.updateChanceTimes(uids);

        return true;
    }

    /**
     * 新增免费车队数据
     * @param teamFreeEvent
     * @return
     */
    private boolean processAddUserFreeDataInfo(TeamFinishOrderEvent teamFreeEvent){
        List<TeamMember> teamMemberList = teamFreeEvent.getTeamMemberVOS();
        List<String> acceptList = new ArrayList<>();
        List<String> placeList = new ArrayList<>();
        for (TeamMember teamMember: teamMemberList) {
            String uid = teamMember.getUid();
            int identity = teamMember.getUserIdentity();
            if (identity == UserIdentityEnum.BOSS.getCode()){
                //老板
                placeList.add(uid);
            }else{
                //暴鸡
                acceptList.add(uid);
            }
        }
        userDataServiceClient.incrUserFreeData(acceptList,placeList);
        return true;
    }

}
