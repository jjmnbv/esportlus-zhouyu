package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.SmallProgramContent;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.gamingteam.api.enums.WxPushReasonEnum;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamOrderCancelParams;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamOrderPushData;
import com.kaihei.esportingplus.gamingteam.api.params.WxTeamStartParams;
import com.kaihei.esportingplus.gamingteam.api.vo.ValueFieldVO;
import com.kaihei.esportingplus.gamingteam.api.vo.WxSmallProgramPushData;
import com.kaihei.esportingplus.gamingteam.config.WxSmallProgramPushConfig;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.http.NoHttpResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WxSmallProgramPushServiceImpl implements WxSmallProgramPushService {

    @Autowired
    private WxSmallProgramPushConfig wxSmallProgramPushConfig;
    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> pushOrderEnd(WxTeamEndParams pushParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, pushParams);

        List<WxTeamOrderPushData> orders = pushParams.getOrders();
        List<WxSmallProgramPushData> pushDataList = new ArrayList<>();
        for (WxTeamOrderPushData order : orders) {
            //组装基本数据
            WxSmallProgramPushData pushData = this
                    .madePushData(order.getUid(), wxSmallProgramPushConfig.getOrderEndTemplateId(),
                            StringUtils.formatTemplate(wxSmallProgramPushConfig.getOrderEndPage(),
                                    order.getOrderSequence()));
            // 组装推送消息内容
            Map<String, ValueFieldVO> data = new HashMap<>();
            data.put("keyword1", this.madeValueFieldVo(
                    StringUtils.formatTemplate(SmallProgramContent.MONEY_FORMAT,
                            new BigDecimal(order.getOrderMoney())
                                    .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)
                                    .toString())));
            data.put("keyword2", this.madeValueFieldVo(StringUtils
                    .formatTemplate(SmallProgramContent.TEAM_END_ORDER_DETAILS_FORMAT,
                            pushParams.getGameName(), pushParams.getRaidName())));
            data.put("keyword3", this.madeValueFieldVo(order.getOrderSequence()));
            data.put("keyword4", this.madeValueFieldVo(StringUtils
                    .formatTemplate(SmallProgramContent.TEAM_LEADER_DESC_FORMAT,
                            pushParams.getLeaderName())));
            pushData.setData(data);
            pushDataList.add(pushData);
        }
        //组装完毕，发送请求
        HttpEntity<String> formEntity = new HttpEntity<String>(
                JacksonUtils.toJsonWithSnake(pushDataList),
                getHeaders());
        return restTemplateExtrnal.postForObject(
                wxSmallProgramPushConfig.getPushDomain() + wxSmallProgramPushConfig.getPushUri(),
                formEntity,
                ResponsePacket.class);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> pushOrderCancel(WxTeamOrderCancelParams cancelParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, cancelParams);
        List<WxTeamOrderPushData> orders = cancelParams.getOrders();
        List<WxSmallProgramPushData> pushDataList = new ArrayList<>();
        WxPushReasonEnum reasonEnum = WxPushReasonEnum.getByCode(cancelParams.getReasonType());
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, reasonEnum);
        for (WxTeamOrderPushData order : orders) {
            //组装基本数据
            WxSmallProgramPushData pushData = this.madePushData(order.getUid(),
                    wxSmallProgramPushConfig.getOrderCancelTemplateId(),
                    wxSmallProgramPushConfig.getOrderCancelPage());
            // 组装推送消息内容
            Map<String, ValueFieldVO> data = new HashMap<>();
            data.put("keyword1", this.madeValueFieldVo(
                    StringUtils.formatTemplate(SmallProgramContent.MONEY_FORMAT,
                            new BigDecimal(order.getOrderMoney())
                                    .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)
                                    .toString())));
            data.put("keyword2", this.madeValueFieldVo(reasonEnum.getDesc()));
            data.put("keyword3", this.madeValueFieldVo(StringUtils
                    .formatTemplate(SmallProgramContent.TEAM_END_ORDER_DETAILS_FORMAT,
                            cancelParams.getGameName(), cancelParams.getRaidName())));
            data.put("keyword4", this.madeValueFieldVo(order.getOrderSequence()));
            data.put("keyword5",
                    this.madeValueFieldVo(SmallProgramContent.TEAM_ORDER_CANCEL_CONTENT));
            pushData.setData(data);
            pushDataList.add(pushData);
        }
        //组装完毕，发送请求
        HttpEntity<String> formEntity = new HttpEntity<String>(
                JacksonUtils.toJsonWithSnake(pushDataList),
                getHeaders());
        return restTemplateExtrnal.postForObject(
                wxSmallProgramPushConfig.getPushDomain() + wxSmallProgramPushConfig.getPushUri(),
                formEntity,
                ResponsePacket.class);
    }

    @Override
    @Retryable(value =Exception.class,exclude = BusinessException.class,maxAttempts = 5, backoff =@Backoff(delay = 100))
    public ResponsePacket<Void> pushTeamStart(WxTeamStartParams startParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, startParams);
        List<WxTeamOrderPushData> orders = startParams.getOrders();
        List<WxSmallProgramPushData> pushDataList = new ArrayList<>();
        for (WxTeamOrderPushData order : orders) {
            //组装基本数据
            WxSmallProgramPushData pushData = this
                    .madePushData(order.getUid(), wxSmallProgramPushConfig.getTeamStartTemplateId(),
                            StringUtils.formatTemplate(wxSmallProgramPushConfig.getTeamStartPage(),
                                    startParams.getTeamSequence()));
            // 组装推送消息内容
            Map<String, ValueFieldVO> data = new HashMap<>();
            data.put("keyword1", this.madeValueFieldVo(StringUtils
                    .formatTemplate(SmallProgramContent.TEAM_START_TITLE_FORMAT,
                            startParams.getGameName(), startParams.getRaidName(),
                            startParams.getZoneAcrossName(), new BigDecimal(order.getOrderMoney())
                                    .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)
                                    .toString())));
            data.put("keyword2", this.madeValueFieldVo(order.getOrderSequence()));
            data.put("keyword3",
                    this.madeValueFieldVo(SmallProgramContent.TEAM_START_DESC_CONTENT));
            pushData.setData(data);
            pushDataList.add(pushData);
        }
        //组装完毕，发送请求
        HttpEntity<String> formEntity = new HttpEntity<String>(
                JacksonUtils.toJsonWithSnake(pushDataList),
                getHeaders());
        return restTemplateExtrnal.postForObject(
                wxSmallProgramPushConfig.getPushDomain() + wxSmallProgramPushConfig.getPushUri(),
                formEntity,
                ResponsePacket.class);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return headers;
    }

    private WxSmallProgramPushData madePushData(String uid, String templateId, String page) {
        WxSmallProgramPushData pushData = new WxSmallProgramPushData();
        pushData.setTouser(uid);
        pushData.setTemplateId(templateId);
        pushData.setPage(page);
        return pushData;
    }

    private ValueFieldVO madeValueFieldVo(String value) {
        ValueFieldVO keyword = new ValueFieldVO();
        keyword.setValue(value);
        return keyword;
    }
}
