package com.kaihei.esportingplus.security.domain.service.iml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.kaihei.esportingplus.common.config.AliYunScanConfig;
import com.kaihei.esportingplus.common.config.QiNiuConfig;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.security.domain.service.IContentProcessService;
import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.security.enums.SuggestionType;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("contentProcessService")
public class ContentProcessServiceImpl implements IContentProcessService {
    //todo 申请keySecret
    private static final String charset = "UTF-8";
    @Autowired
    private AliYunScanConfig aliYunScanConfig;
    @Autowired
    private  IAcsClient client;


    @Override
    public void textScan(String content) {
        //构造请求
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding(charset);
        textScanRequest.setRegionId(aliYunScanConfig.getRegionId());
        //定义任务组
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        //构造任务
        Map<String, Object> task = new LinkedHashMap<String, Object>();
        task.put("dataId", UUID.randomUUID().toString());
        task.put("content", content);
        //加入任务组
        tasks.add(task);
        //文本检测场景：antispam（垃圾检测）和keyword（关键词检测）
        JSONObject data = new JSONObject();
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        try {
            textScanRequest.setHttpContent(data.toJSONString().getBytes(charset), charset, FormatType.JSON);
            //设置超时时间，必填
            textScanRequest.setConnectTimeout(3000);
            textScanRequest.setReadTimeout(6000);
            //调用服务
            HttpResponse httpResponse = client.doAction(textScanRequest);
            //响应成功
            if(httpResponse.isSuccess()){
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), charset));
                System.out.println(JSON.toJSONString(scrResponse, true));
                //响应码200，代表解析成功
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    //迭代解析结果
                    for (Object taskResult : taskResults) {
                        //响应码200，代表任务解析成功
                        if(200 == ((JSONObject)taskResult).getInteger("code")){
                            JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject)sceneResult).getString("scene");
                                String suggestion = ((JSONObject)sceneResult).getString("suggestion");
                                //根据scene和suggetion做相关处理
                                //suggetion类型:pass：文本正常、review：需要人工审核、block：文本违规
                                System.out.println("args = [" + scene + "] args = ["+suggestion + "]");
                                if(suggestion.equalsIgnoreCase(SuggestionType.REVIEW.getCode())){
                                    throw new BusinessException(BizExceptionEnum.ALI_SCAN_SUGGESTION_REVIEW);
                                }else if(suggestion.equalsIgnoreCase(SuggestionType.BLOCK.getCode())){
                                    throw new BusinessException(BizExceptionEnum.ALI_SCAN_SUGGESTION_BLOCK);
                                }
                            }
                        }else{//响应码非200，代表任务解析失败
                            //System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                            throw new BusinessException(BizExceptionEnum.ALI_SCAN_TASK_PROCESS_FAIL);
                        }
                    }
                } else {//响应码非200，代表解析失败
                    //System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                    throw new BusinessException(BizExceptionEnum.ALI_SCAN_DETECT_NOT_SUCCESS);
                }
            }else{//响应失败
                //System.out.println("response not success. status:" + httpResponse.getStatus());
                throw new BusinessException(BizExceptionEnum.ALI_SCAN_RESPONSE_NOT_SUCCESS);
            }
        } catch (ServerException e) {
            throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
        }catch (BusinessException e){
            throw e;
        }catch (ClientException e) {
            throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
        }
    }
}
