package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.ImContent;
import com.kaihei.esportingplus.common.constant.ImKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.gamingteam.api.enums.ImMemberOutReasonEnum;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupBaseParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgOutMemberParams;
import com.kaihei.esportingplus.gamingteam.api.params.UserNameImCmdMsgParams;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamImCmdMsgContent;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamImCmdMsgData;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamImCmdMsgVO;
import com.kaihei.esportingplus.gamingteam.config.ImConfig;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImServiceImpl implements ImService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImServiceImpl.class);
    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;
    @Autowired
    private ImConfig imConfig;

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> createGroup(ImGroupCommonParams params) {
        HttpEntity<String> formEntity = new HttpEntity<String>(JacksonUtils.toJsonWithSnake(params),
                checkDataAndGetHeaders(params));
        String result = restTemplateExtrnal
                .postForObject(imConfig.getImDomain() + imConfig.getCreateGroupUri(), formEntity,
                        String.class);
        return JacksonUtils.toBean(result, ResponsePacket.class);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> joinGroup(ImGroupCommonParams params) {
        HttpEntity<String> formEntity = new HttpEntity<String>(JacksonUtils.toJsonWithSnake(params),
                checkDataAndGetHeaders(params));
        String result = restTemplateExtrnal
                .postForObject(imConfig.getImDomain() + imConfig.getJoinGroupUri(), formEntity,
                        String.class);
        return JacksonUtils.toBean(result, ResponsePacket.class);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> leaveGroup(ImGroupBaseParams params) {
        HttpEntity<String> formEntity = new HttpEntity<String>(JacksonUtils.toJsonWithSnake(params),
                checkDataAndGetHeaders(params));
        String result = restTemplateExtrnal
                .postForObject(imConfig.getImDomain() + imConfig.getLeaveGroupUri(), formEntity,
                        String.class);
        return JacksonUtils.toBean(result, ResponsePacket.class);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> dismissGroup(ImGroupBaseParams params) {
        HttpEntity<String> formEntity = new HttpEntity<String>(JacksonUtils.toJsonWithSnake(params),
                checkDataAndGetHeaders(params));
        String result = restTemplateExtrnal
                .postForObject(imConfig.getImDomain() + imConfig.getDismissGroupUri(), formEntity,
                        String.class);
        return JacksonUtils.toBean(result, ResponsePacket.class);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> startTeam(TeamImCmdMsgBaseParams params) {
        return sendCmdMsg(params, ImKey.TEAM_START_KEY, ImContent.TEAM_START_CONTENT);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> outTeam(TeamImCmdMsgOutMemberParams params) {
        ImMemberOutReasonEnum imMemberOutReasonEnum =
                ImMemberOutReasonEnum.getByCode(params.getOutReasonType());
        ValidateAssert.hasNotNull(BizExceptionEnum.TEAM_MEMBER_OUT_REASON_NOT_EMPTY,imMemberOutReasonEnum);
        return sendCmdMsg(params, ImKey.TEAM_LEADER_OUT_MEMBER_KEY,
                imMemberOutReasonEnum.getDesc());
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> dismissTeam(TeamImCmdMsgBaseParams params) {
        return sendCmdMsg(params, ImKey.TEAM_DISMISS_KEY, ImContent.TEAM_DISMISS_CONTENT);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> changeTeamCount(TeamImCmdMsgBaseParams params) {
        return sendCmdMsg(params, ImKey.TEAM_COUNT_CHANGE, null);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> endTeamServer(TeamImCmdMsgBaseParams params) {
        return sendCmdMsg(params, ImKey.END_TEAM_SERVER, ImContent.END_TEAM_SERVER_CONTENT);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> paidFinish(TeamImCmdMsgBaseParams params) {
        return sendCmdMsg(params, ImKey.PAID_FINISH_KEY, null);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> confirmInTeam(TeamImCmdMsgBaseParams params) {
        return sendCmdMsg(params, ImKey.CONFIRM_IN_TEAM, null);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> joinTeam(UserNameImCmdMsgParams params) {
        return sendCmdMsg(params, ImKey.JOIN_TEAM_KEY,
                String.format(ImContent.MEMBER_JOIN_TEAM_CONTENT, params.getUserName()));
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> exitTeam(UserNameImCmdMsgParams params) {
        return sendCmdMsg(params, ImKey.EXIT_TEAM_KEY,
                String.format(ImContent.LEAVE_TEAM_CONTENT, params.getUserName()));
    }

    private ResponsePacket<Void> sendCmdMsg(TeamImCmdMsgBaseParams params, String name,
            String content) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params, name);
        TeamImCmdMsgVO vo = BeanMapper.map(params, TeamImCmdMsgVO.class);
        vo.setTemplateId(imConfig.getCmdTemplateId());
        vo.setSender(imConfig.getSysUser());
        TeamImCmdMsgData data = new TeamImCmdMsgData();
        data.setName(name);
        TeamImCmdMsgContent msgContent = new TeamImCmdMsgContent();
        msgContent.setContent(content);
        msgContent.setTeamSequence(params.getTeamSequence());
        data.setData(JacksonUtils.toJsonWithSnake(msgContent));
        vo.setData(data);
        String sendJson = JacksonUtils.toJsonWithSnake(vo);
        HttpEntity<String> formEntity = new HttpEntity<String>(sendJson,
                checkDataAndGetHeaders(vo));
        LOGGER.info(">> send im cmd msg,send content:{}",sendJson);
        return restTemplateExtrnal
                .postForObject(imConfig.getImDomain() + imConfig.getSendMsgUri(), formEntity,
                        ResponsePacket.class);
    }

    private HttpHeaders checkDataAndGetHeaders(Object object) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, object);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return headers;
    }

}
