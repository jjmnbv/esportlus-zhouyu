package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.IpAddressUtil;
import com.kaihei.esportingplus.payment.service.CloudAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: chenzhenjun
 * @Date: 2018/11/06 17:07
 * @Description: 云账户提现回调通知
 */
@RestController
@RequestMapping("/v1/yunpay/notify")
public class CloudPayCallBackController {

    private static final Logger logger = LoggerFactory.getLogger(CloudPayCallBackController.class);

    @Autowired
    private CloudAccountService cloudAccountService;

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    @ResponseBody
    public String receiveCloudNotify(HttpServletRequest request) {
        String retCode = "";
        Map<String, String> requestMap = null;
        try {
            requestMap = convertRequestParamsToMap(request);
            retCode = cloudAccountService.receiveCloudNotify(requestMap);
            logger.debug("CloudPayCallBackController >> notify >> 出参 >> " + retCode);
        } catch (BusinessException e) {
            logger.error("CloudPayCallBackController >> Exception :  " + e.getErrMsg());
            ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }
        return retCode;
    }

    // 将request中的参数转换成Map
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }
        return retMap;
    }

}
