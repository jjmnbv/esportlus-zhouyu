package com.kaihei.esportingplus.marketing.controller;

import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.enums.DictionaryCodeEnum;
import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.marketing.api.event.UserEventHandler;
import com.kaihei.esportingplus.marketing.api.event.UserExemptionEvent;
import com.kaihei.esportingplus.marketing.api.feign.UserNewVersionExemptionClient;
import com.kaihei.esportingplus.marketing.api.vo.ExemptionTaskVo;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zl.zhao
 * @description:新版本用户免单接口
 * @date: 2018/10/25 17:31
 */
@RestController
@RequestMapping("/version")
public class UserNewVersionExemptionController implements UserNewVersionExemptionClient {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "marketUserExemptionEventHandler")
    UserEventHandler marketUserExemptionEventHandler;

    @Autowired
    private DictionaryClient dictionaryClient;

    /**
     * 新版本用户启动免单
     */
    @Override
    public ResponsePacket<ExemptionTaskVo> exemptionTask(@RequestBody UserExemptionEvent params) {
        Boolean result = marketUserExemptionEventHandler.handle(params);
        ExemptionTaskVo exemptionTaskVo = new ExemptionTaskVo();
        exemptionTaskVo.setStatus(result);
        if(result) {
            //获取首页图片配置
            try {
                ResponsePacket<DictBaseVO<Object>> dictionary = dictionaryClient
                        .findByCodeAndCategoryCode(
                                DictionaryCategoryCodeEnum.FREE_TEAM_CONFIG.getCode(),
                                DictionaryCodeEnum.FREE_TEAM_CONFIG_CODE.getCode(), null);
                if (dictionary == null || dictionary.getData() == null) {
                    logger.info("cmd=UserNewVersionExemptionController.exemptionTask | msg=任务下线了 | req={}", JacksonUtils.toJson(params));
                }
                Map dictionaryValue = (Map) dictionary.getData().getValue();
                Map indexAlert = (Map) dictionaryValue.get("free_team_index_alert");
                String url = indexAlert.get("alert_url").toString();
                if (Integer.valueOf(indexAlert.get("alert_switch").toString()) == 1) {//开关为开启状态
                    exemptionTaskVo.setAlertUrl(url);
                }
            } catch (Exception e){
                logger.error("获取奖励配置图片失败!",e);
            }
        }
        return ResponsePacket.onSuccess(exemptionTaskVo);
    }
}
