package com.kaihei.esportingplus.customer.center.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.api.enums.EvaluateTypeEnum;
import com.kaihei.esportingplus.api.params.EvaluateCreateParam;
import com.kaihei.esportingplus.api.params.EvaluateQueryParam;
import com.kaihei.esportingplus.api.vo.EvaluateAppraiserVo;
import com.kaihei.esportingplus.api.vo.EvaluateContentVo;
import com.kaihei.esportingplus.api.vo.EvaluateListVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.file.WordFilterService;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.customer.center.data.repository.CustomerOrderEvaluateRepository;
import com.kaihei.esportingplus.customer.center.domain.entity.CustomerOrderEvaluate;
import com.kaihei.esportingplus.customer.center.domain.service.IEvaluateService;
import com.kaihei.esportingplus.gamingteam.api.feign.PVPFreeTeamServiceClient;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamMemberInfoVO;
import com.kaihei.esportingplus.marketing.api.event.TeamFreeObtainstarEvent;
import com.kaihei.esportingplus.marketing.api.event.TeamMember;
import com.kaihei.esportingplus.marketing.api.feign.UserTaskFreeTeamServiceClient;
import com.kaihei.esportingplus.user.api.feign.UserServiceClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 评价服务实现类
 *
 * @author yangshidong
 * @date 2018/11/15
 */
@Service
public class EvaluateServiceImpl implements IEvaluateService {

    Logger logger = LoggerFactory.getLogger(EvaluateServiceImpl.class);

    @Autowired
    private CustomerOrderEvaluateRepository customerOrderEvaluateRepository;

    @Autowired
    private UserTaskFreeTeamServiceClient userTaskFreeTeamServiceClient;

    @Autowired
    private WordFilterService wordFilterService;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private PVPFreeTeamServiceClient pvpFreeTeamServiceClient;

    @Value("${evaluate.star.maxValue}")
    private int maxStar;

    @Value("${evaluate.content.maxLength}")
    private int maxContentLength;

    @Override
    public void submitEvaluate(EvaluateCreateParam evaluateCreateParam) {
        logger.error("cmd=EvaluateServiceImpl>>submitEvaluate begin | evaluateCreateParam={}", JSON.toJSON(evaluateCreateParam));
        //校验当前用户是否能够评论
        validateEstimatorWithOrder(evaluateCreateParam);
        //校验评论内容的合法性
        validateEvaluateParam(evaluateCreateParam);
        //获取用户上下文信息
        UserSessionContext userSessionContext = UserSessionContext.getUser();
        //根据不同的订单类型设置不同的关联资源id
        String relateSourceId = getRelateSourceId(evaluateCreateParam);

        // 免费车队评价则调用免费车队增加暴鸡鸡分接口
        if (evaluateCreateParam.getOrderType().getCode() == EvaluateTypeEnum.PREMADE_EVALUATE.getCode()) {
            List<TeamMember> memberList = getTeamMemberListForFreeTeam(evaluateCreateParam);
            TeamFreeObtainstarEvent teamFreeObtainstarEvent = new TeamFreeObtainstarEvent();
            teamFreeObtainstarEvent.setCommentUserid(userSessionContext.getId() + "");
            teamFreeObtainstarEvent.setDan(evaluateCreateParam.getLaobanDan());
            teamFreeObtainstarEvent.setGameNum((int) evaluateCreateParam.getGameRound());
            teamFreeObtainstarEvent.setStar(evaluateCreateParam.getStar() + "");
            //调用接口中未用到订单时间，防止前端传过来字符串形式再转日期格式导致异常，此处传空
            teamFreeObtainstarEvent.setOrderTime(null);
            teamFreeObtainstarEvent.setFreeTeamType(evaluateCreateParam.getPremadeTypeId());
            teamFreeObtainstarEvent.setMembers(memberList);
            teamFreeObtainstarEvent.setSlug(evaluateCreateParam.getPremadeSlug());
            userTaskFreeTeamServiceClient.obtainstar(teamFreeObtainstarEvent);
        }

        CustomerOrderEvaluate orderEvaluate = CustomerOrderEvaluate.builder()
                .orderType((short) evaluateCreateParam.getOrderType().getCode())
                .orderId(evaluateCreateParam.getOrderId())
                .authorId(userSessionContext.getId())
                .authorUid(userSessionContext.getUid())
                .content(evaluateCreateParam.getContent())
                .createTime(new Date())
                .deleteTime(null)
                .isDeleted(false)
                .star(evaluateCreateParam.getStar())
                .updateTime(new Date())
                .baojiUid(evaluateCreateParam.getBaojiUid())
                .baojiType(evaluateCreateParam.getBaojiType())
                .baojiLevel(evaluateCreateParam.getBaojiLevel())
                .gameResult(evaluateCreateParam.getGameResult())
                .gameRound(evaluateCreateParam.getGameRound())
                .orderFinishTime(evaluateCreateParam.getOrderFinishTime())
                .relateSourceId(relateSourceId)
                .relateSoruceDesc(evaluateCreateParam.getOrderType().getDesc())
                .build();
        customerOrderEvaluateRepository.insertSelective(orderEvaluate);
    }

    @Override
    public EvaluateListVo getEvaluateList(EvaluateQueryParam evaluateQueryParam) {
        logger.error("cmd=EvaluateServiceImpl>>getEvaluateList begin | evaluateQueryParam={}", JSON.toJSON(evaluateQueryParam));
        UserSessionContext userSessionContext = UserSessionContext.getUser();
        short orderType = (short) evaluateQueryParam.getEvaluateType().getCode();
        int orderId = evaluateQueryParam.getId();
        evaluateQueryParam.setOrderType(orderType);
        //如果是列表查询，则需要将订单id置为0，按照列表逻辑查询
        if (evaluateQueryParam.getIsAimedOrder() == 1) {
            evaluateQueryParam.setOrderId(0);
        }
        /**
         * 暴鸡侧查询
         * 1.需要将当前用户的id赋值给baojiId,查询出该暴鸡用户下该类型的所有的单的评论
         * 2.如果为暴击侧自定义技能查询则需要将自定义技能id赋值给relateSourceId,同时将orderId赋值为0
         */
        if (evaluateQueryParam.getIsBaoji() == 1) {
            evaluateQueryParam.setBaojiUid(userSessionContext.getUid());
            if (evaluateQueryParam.getEvaluateType() != EvaluateTypeEnum.ORDER_EVALUATE) {
                evaluateQueryParam.setRelateSourceId(orderId + "");
            }
        } else {
            evaluateQueryParam.setAuthorId(userSessionContext.getId());
        }
        if (evaluateQueryParam.getPage() == 0 && evaluateQueryParam.getPageCount() == 0) {
            evaluateQueryParam.setPageCount(10);
        }
        Page<CustomerOrderEvaluate> page = PageHelper
                .startPage(evaluateQueryParam.getPage(), evaluateQueryParam.getPageCount())
                .doSelectPage(() -> customerOrderEvaluateRepository.selectEvaluateList(evaluateQueryParam));
        List<CustomerOrderEvaluate> list = page.getResult();
        List<EvaluateContentVo> evaluateContentVoList = new ArrayList<>();
        //查询每条评价的评价人username,thumbnail
        list.stream().forEach(t -> {
            ResponsePacket<UserSessionContext> responsePacket = userServiceClient.getUserInfosByUid(t.getAuthorUid());
            if (!responsePacket.responseSuccess()) {
                throw new BusinessException(responsePacket.getCode(), responsePacket.getMsg());
            }
            String thumbnail = responsePacket.getData().getAvatar();
            String username = responsePacket.getData().getUsername();
            evaluateContentVoList.add(EvaluateContentVo.builder()
                    .appraiser(EvaluateAppraiserVo.builder().username(username).thumbnail(thumbnail).build())
                    .content(t.getContent())
                    .createTime(t.getCreateTime())
                    .orderId(t.getOrderId())
                    .star(t.getStar())
                    .build());
        });
        EvaluateListVo evaluateListVo = EvaluateListVo.builder().evaluateList(evaluateContentVoList).evaluateCount(evaluateContentVoList.size()).build();
        return evaluateListVo;
    }

    /**
     * 根据不同的订单类型获取不同的关联对象id
     * 普通单：暴鸡的uid
     * 车队单  车队的id
     * 自定义技能单  自定义技能的id
     */
    private String getRelateSourceId(EvaluateCreateParam evaluateCreateParam) {
        if (evaluateCreateParam.getOrderType().getCode() == EvaluateTypeEnum.ORDER_EVALUATE.getCode()) {
            //普通单则返回暴鸡uid
            return evaluateCreateParam.getBaojiUid();
        } else if (evaluateCreateParam.getOrderType().getCode() == EvaluateTypeEnum.PREMADE_EVALUATE.getCode()) {
            //车队单则返回车队id
            return evaluateCreateParam.getPremadeId() + "";
        } else if (evaluateCreateParam.getOrderType().getCode() == EvaluateTypeEnum.SERVICE_EVALUATE.getCode()) {
            //自定义技能单则返回自定义技能id
            return evaluateCreateParam.getCustomSkillId() + "";
        } else {
            throw new BusinessException(BizExceptionEnum.PARAM_VALID_FAIL);
        }
    }

    /**
     * 校验提交评价参数合法性
     */
    private void validateEvaluateParam(EvaluateCreateParam evaluateCreateParam) {
        int star = evaluateCreateParam.getStar();
        String content = evaluateCreateParam.getContent();
        List legalStarList = new ArrayList();
        for (int i = 1; i <= maxStar; i++) {
            legalStarList.add(i);
        }
        if (!legalStarList.contains(star)) {
            logger.error("cmd=EvaluateServiceImpl>>validateEvaluateContent error |  errorMessage={} | evaluateCreateParam={}",
                    BizExceptionEnum.EVALUATE_STAR_ILLEGAL.getErrMsg(), JSON.toJSON(evaluateCreateParam));
            throw new BusinessException(BizExceptionEnum.EVALUATE_STAR_ILLEGAL);
        }
        if (content.length() > maxContentLength) {
            logger.error("cmd=EvaluateServiceImpl>>validateEvaluateContent error |  errorMessage={} | evaluateCreateParam={}",
                    BizExceptionEnum.EVALUATE_CONTENT_LENGTH_OVER.getErrMsg(), JSON.toJSON(evaluateCreateParam));
            throw new BusinessException(BizExceptionEnum.EVALUATE_CONTENT_LENGTH_OVER);
        }
        try {
            if (!wordFilterService.checkWord(content)) {
                logger.error("cmd=EvaluateServiceImpl>>validateEvaluateContent error |  errorMessage={} | evaluateCreateParam={}",
                        BizExceptionEnum.EVALUATE_CONTENT_ILLEGAL.getErrMsg(), JSON.toJSON(evaluateCreateParam));
                throw new BusinessException(BizExceptionEnum.EVALUATE_CONTENT_ILLEGAL);
            }
        } catch (Exception e) {
            throw new BusinessException(BizExceptionEnum.EVALUATE_CONTENT_ILLEGAL);
        }
    }

    /**
     * 校验评价者与对应订单关系
     * 1.普通单则校验老板id是否是评论用户
     * 2.车队单则校验评论用户是否是车队成员
     * 3.自定义技能单则校验服务是否完成
     */
    private void validateEstimatorWithOrder(EvaluateCreateParam evaluateCreateParam) {
        int orderType = evaluateCreateParam.getOrderType().getCode();
        if (orderType == EvaluateTypeEnum.PREMADE_EVALUATE.getCode()) {
            //如果是车队订单评论则校验评论用户是否是车队成员
            ResponsePacket<PVPFreeTeamMemberInfoVO> responsePacket = pvpFreeTeamServiceClient.getTeamMemberInfo(null, evaluateCreateParam.getOrderId() + "");
            if (!responsePacket.responseSuccess()) {
                logger.error("cmd=EvaluateServiceImpl>>validateEstimatorWithOrder failed | evaluateCreateParam={} | " +
                        "errorMessage={}", JSON.toJSONString(evaluateCreateParam), "调用免费车队服务获取车队成员信息异常！返回异常信息=" + responsePacket.getMsg());
                throw new BusinessException(responsePacket.getCode(), responsePacket.getMsg());
            }
            List<PVPFreeTeamMemberVO> memberInfoList = responsePacket.getData().getMemberInfoList();
            if (!memberInfoList.stream().anyMatch(t -> t.getUid().equals(UserSessionContext.getUser().getUid()))) {
                logger.error("cmd=EvaluateServiceImpl>>validateEstimatorWithOrder false | evaluateCreateParam={} | " +
                        "errorMessage={}", JSON.toJSONString(evaluateCreateParam), BizExceptionEnum.EVALUATE_NOT_TEAM_MEMBER.getErrMsg());
                throw new BusinessException(BizExceptionEnum.EVALUATE_NOT_TEAM_MEMBER);
            }
        }
    }

    /**
     * 如果是免费车队评论，则调用免费车队服务获取车队成员信息拼装城TeamMember对象，调用鸡分接口传参
     */
    private List<TeamMember> getTeamMemberListForFreeTeam(EvaluateCreateParam evaluateCreateParam) {
        ResponsePacket<PVPFreeTeamMemberInfoVO> responsePacket = pvpFreeTeamServiceClient.getTeamMemberInfo(null, evaluateCreateParam.getOrderId() + "");
        if (!responsePacket.responseSuccess()) {
            logger.error("cmd=EvaluateServiceImpl>>validateEstimatorWithOrder failed | orderId={} | " +
                    "errorMessage={}", evaluateCreateParam.getOrderId(), "调用免费车队服务获取车队成员信息异常！返回异常信息=" + responsePacket.getMsg());
            throw new BusinessException(responsePacket.getCode(), responsePacket.getMsg());
        }
        List<PVPFreeTeamMemberVO> memberInfoList = responsePacket.getData().getMemberInfoList();
        List<TeamMember> memberList = memberInfoList.stream().map(t -> {
            TeamMember teamMember = new TeamMember();
            teamMember.setUserIdentity(t.getUserIdentity());
            teamMember.setBaojiLevel(t.getBaojiLevel());
            teamMember.setUid(t.getUid());
            teamMember.setDan(evaluateCreateParam.getLaobanDan());
            return teamMember;
        }).collect(Collectors.toList());
        return memberList;
    }
}
