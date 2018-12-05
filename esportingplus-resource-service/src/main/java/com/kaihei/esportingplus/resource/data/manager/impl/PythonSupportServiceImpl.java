package com.kaihei.esportingplus.resource.data.manager.impl;

import com.kaihei.esportingplus.api.params.BaojiCertifyParams;
import com.kaihei.esportingplus.api.vo.BaojiCertifyResult;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.resource.data.manager.PythonSupportService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author liangyi
 */
@Service("pythonSupportService")
public class PythonSupportServiceImpl implements PythonSupportService {

    private static final Logger log = LoggerFactory.getLogger(PythonSupportServiceImpl.class);

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Value("${python.baojicertify.url}")
    private String baojiCertifyUrl;

    @Override
    public BaojiCertifyResult getBaojiCertifyInfo(BaojiCertifyParams baojiCertifyParams) {
        log.info(">> 开始调用Python接口获取暴鸡认证信息, 参数: {}", baojiCertifyParams);
        long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity<>(
                JacksonUtils.toJsonWithSnake(baojiCertifyParams), headers);
        ResponsePacket baojiCertifyInfoResp =
                restTemplateExtrnal.postForObject(baojiCertifyUrl, httpEntity, ResponsePacket.class);
        log.info(">> 调用Python接口获取暴鸡认证信息完成,返回结果为:{},耗时: {}ms",
                baojiCertifyInfoResp, (System.currentTimeMillis()-start));
        if (!baojiCertifyInfoResp.responseSuccess()) {
            log.error(">> 调用Python接口获取暴鸡认证信息错误!参数: {}, 返回结果: {}",
                    baojiCertifyParams, baojiCertifyInfoResp);
            throw new BusinessException(
                    baojiCertifyInfoResp.getCode(), baojiCertifyInfoResp.getMsg());
        }
        String resultData = JacksonUtils.toJsonWithSnake(baojiCertifyInfoResp.getData());
        BaojiCertifyResult baojiCertifyResult = JacksonUtils
                .toBeanWithSnake(resultData, BaojiCertifyResult.class);
        return baojiCertifyResult;
    }
}
