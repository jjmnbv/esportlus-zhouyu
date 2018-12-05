package com.kaihei.esportingplus.core.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.ThumbnailVerifyResultType;
import com.kaihei.esportingplus.common.file.FileUploadService;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.MD5Util;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.core.api.enums.QiniuTokenTypeEnum;
import com.kaihei.esportingplus.core.api.vo.QiniuImageCheckVo;
import com.kaihei.esportingplus.core.api.vo.QiniuTokenVo;
import com.kaihei.esportingplus.core.config.QiniuConfig;
import com.kaihei.esportingplus.core.constant.QiniuConstants;
import com.kaihei.esportingplus.core.domain.service.QiniuManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author zl.zhao
 * @description:
 * @date: 2018/10/29 19:35
 */
@Service
public class QiniuManageServiceImpl implements QiniuManageService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private QiniuConfig qiniuConfig;

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Override
    public QiniuTokenVo getTokenByTokenType(String tokenType, String gameOrderId, String pictureNo, String serialNo) {
        if (tokenType == null) {
            logger.error("cmd=QiniuManageServiceImpl.getTokenByTokenType | msg={} | req={}",
                    "参数错误", tokenType);
            return null;
        }

        //调用七牛获取token
        return getUpToken(tokenType, gameOrderId, pictureNo, serialNo);
    }

    /**
     * 鉴暴恐
     * @param url
     * @return
     */
    @Override
    public QiniuImageCheckVo checkQterrorImage(String url) {
        QiniuImageCheckVo vo = new QiniuImageCheckVo();
        vo.setVerify(Boolean.TRUE);

        String requestUrl = setRequestUrl(url, "qterror");
        ResponseEntity<String> result = restTemplateExtrnal.getForEntity(requestUrl, String.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            try {
                String imageResult = result.getBody();
                JSONObject jsonObject = JSONObject.parseObject(imageResult);
                if (jsonObject.getString("code").equals("0")) {
                    JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.getString("result"));
                    switch (jsonObject1.getString("label")){
                        //0正常
                        case "0":
                            vo.setVerifyCode(ThumbnailVerifyResultType.NORMAL.getCode());
                            vo.setVerify(Boolean.TRUE);
                            break;
                        //暴恐
                        case "1":
                            vo.setVerifyCode(ThumbnailVerifyResultType.VIOLENCE.getCode());
                            vo.setVerify(Boolean.FALSE);
                            break;
                        default:
                            break;
                    }
                    vo.setReview(jsonObject1.getString("review"));
                    vo.setScore(jsonObject1.getFloat("score"));
                }
                return vo;
            } catch (Exception e) {
                logger.error("cmd=QiniuManageServiceImpl.checkQterrorImage error:{}, url={}",
                                    e.getMessage(), url);
                return vo;
            }
        }
        return vo;
    }

    /**
     * 鉴黄
     * @param url
     * @return
     */
    @Override
    public QiniuImageCheckVo checkQpulpImage(String url) {
        QiniuImageCheckVo vo = new QiniuImageCheckVo();
        vo.setVerify(Boolean.TRUE);

        String requestUrl = setRequestUrl(url, "qpulp");
        ResponseEntity<String> result = restTemplateExtrnal.getForEntity(requestUrl, String.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            try {
                String imageResult = result.getBody();
                JSONObject jsonObject = JSONObject.parseObject(imageResult);
                if (jsonObject.getString("code").equals("0")) {
                    JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.getString("result"));
                    switch (jsonObject1.getString("label")){
                        //0色情
                        case "0":
                            vo.setVerifyCode(ThumbnailVerifyResultType.PORNOGRAPHY.getCode());
                            vo.setVerify(Boolean.FALSE);
                            break;
                        //1性感
                        case "1":
                            vo.setVerifyCode(ThumbnailVerifyResultType.SEXY.getCode());
                            vo.setVerify(Boolean.FALSE);
                            break;
                        //2正常
                        case "2":
                            vo.setVerifyCode(ThumbnailVerifyResultType.NORMAL.getCode());
                            vo.setVerify(Boolean.TRUE);
                            break;
                    }
                    vo.setReview(jsonObject1.getString("review"));
                    vo.setScore(jsonObject1.getFloat("score"));
                }
                return vo;
            } catch (Exception e) {
                logger.error("cmd=QiniuManageServiceImpl.checkQterrorImage error:{}, url={}",
                        e.getMessage(), url);
                return vo;
            }
        }
        return vo;
    }

    /**
     * （首次注册）上传头像获取token
     * @param
     * @return
     */
    @Override
    public QiniuTokenVo getTokenByAvatar() {
        String key = "tn-" + UUID.randomUUID() + "-" + getStringRandom(8) + ".png";
        String bucket = QiniuTokenTypeEnum.THUMBNAIL_UPLOADTOKEN.getBucket();
        String qiniuToken = fileUploadService.getUploadToken(bucket, key);
        String domain = qiniuConfig.getBaojiPubHttps();
        QiniuTokenVo vo = new QiniuTokenVo(qiniuToken, key, domain);
        return vo;
    }

    /**
     * 配置url
     * @param url
     * @return
     */
    private String setRequestUrl(String url, String key) {
        String requestUrl;
        if(url.contains("?")) {
            requestUrl = url + "&" + key;
        }else {
            requestUrl = url + "?" + key;
        }
        return requestUrl;
    }

    /**
     * 根据不同type配置相应的七牛文件目录
     * @param tokenType
     * @param gameOrderId
     * @param pictureNo
     * @param serialNo
     * @return
     */
    private QiniuTokenVo getUpToken(String tokenType, String gameOrderId, String pictureNo, String serialNo) {
        logger.info("cmd=QiniuManageServiceImpl.getUpToken | msg={} | tokenType={} | gameOrderId={} |　pictureNo={} |　serialNo={}",
                                "开始获取token", tokenType, gameOrderId, pictureNo, serialNo);

        String uid = UserSessionContext.getUser().getUid();
        String bucket = QiniuTokenTypeEnum.getBucketByCode(tokenType);
        String key = null;
        String qiniuToken = null;
        String domain = null;
        switch (tokenType) {
            // 调用七牛获取token
            case QiniuConstants.UPLOADTOKEN_THUMBNAIL:
                key = UserSessionContext.getUser().getAvatar();
                //覆盖之前的头像
                if(ObjectTools.isNotEmpty(key)) {
                    qiniuToken = fileUploadService.getUploadTokenToCover(bucket, key);
                    domain = qiniuConfig.getBaojiPubHttps();
                } else{
                    //新建一个头像
                    key = "tn-" + UUID.randomUUID() + "-" + getStringRandom(8) + ".png";
                    qiniuToken = fileUploadService.getUploadToken(bucket, key);
                    domain = qiniuConfig.getBaojiPubHttps();
                }
                break;
            case QiniuConstants.UPLOADTOKEN_IDCARD:
                key = "id-" + md5(uid) + ".png";
                qiniuToken = fileUploadService.getUploadTokenToCover(bucket, key);
                domain = qiniuConfig.getBaojiDomain();
                break;
            case QiniuConstants.UPLOADTOKEN_SCREENSHOT:
                key = "ss-" + uid + "-" + gameOrderId + ".png";
                qiniuToken = fileUploadService.getUploadToken(bucket, key);
                domain = qiniuConfig.getBaojiDomain();
                break;
            case QiniuConstants.UPLOADTOKEN_HIGHEST:
                key = "highest-" + uid + "-" + UUID.randomUUID().toString() + ".png";
                qiniuToken = fileUploadService.getUploadToken(bucket, key);
                domain = qiniuConfig.getBaojiPub();
                break;
            case QiniuConstants.UPLOADTOKEN_APPLYREFUND:
                key = "af-" + gameOrderId + "-" +  pictureNo + ".png";
                qiniuToken = fileUploadService.getUploadToken(bucket, key);
                domain = qiniuConfig.getBaojiDomain();
                break;
            case QiniuConstants.UPLOADTOKEN_FEEDBACK:
                key = uid + "-feedback-" + getStringRandom(8) + ".png";
                qiniuToken = fileUploadService.getUploadTokenToCover(bucket, key);
                domain = qiniuConfig.getBaojiPubHttps();
                break;
            case QiniuConstants.UPLOADTOKEN_BECOMEBAONIANG_IMG:
                key = "album-" + md5(uid) + "-" + serialNo + ".png";
                qiniuToken = fileUploadService.getUploadTokenToCover(bucket, key);
                domain = qiniuConfig.getBnPubBucket();
                break;
            case QiniuConstants.UPLOADTOKEN_USER_IMG:
                key = "album-" + md5(uid) + "-" + getStringRandom(8) + ".png";
                qiniuToken = fileUploadService.getUploadTokenToCover(bucket, key);
                domain = qiniuConfig.getBnPubBucket();
                break;
            case QiniuConstants.UPLOADTOKEN_USER_VIDEO:
                key = "video-" + md5(uid) + "-" + serialNo+ ".mp4";
                qiniuToken = fileUploadService.getUploadTokenToCover(bucket, key);
                domain = qiniuConfig.getBnPubBucket();
                break;
            case QiniuConstants.UPLOADTOKEN_DISCOVERY:
                key = "discovery-" + md5(uid) + "-" + getStringRandom(8) + ".png";
                qiniuToken = fileUploadService.getUploadTokenToCover(bucket, key);
                domain = qiniuConfig.getDiscoveryBucket();
                break;
            case QiniuConstants.UPLOADTOKEN_ORDER_FINSISH:
                key = uid + "-gameresults-" + getStringRandom(8) + ".png";
                qiniuToken = fileUploadService.getUploadTokenToCover(bucket, key);
                domain = qiniuConfig.getBaojiPubHttps();
                break;
            case QiniuConstants.UPLOADTOKEN_DISCOVERY_VIDEO:
                key = "discovery_video_" + md5(uid) + "-" + getStringRandom(8) + ".png";
                qiniuToken = fileUploadService.getUploadToken(bucket, key);
                domain = qiniuConfig.getDiscoveryVideoBucket();
                break;
            default:
                break;
        }

        logger.info("cmd=QiniuManageServiceImpl.getUpToken | msg={} | tokenType={} | gameOrderId={} |　pictureNo={} |　serialNo={}",
                "获取token结束", tokenType, gameOrderId, pictureNo, serialNo);

        QiniuTokenVo vo = new QiniuTokenVo(qiniuToken, key, domain);
        return vo;
    }

    /**
     * 对用户uid加密
     * @param uid
     * @return
     */
    public static String md5(String uid) {
        //加密后的字符串
        String encodeStr = MD5Util.MD5Encode(uid, "utf-8");
        return encodeStr;
    }

    /**
     *  生成随机数
     */
    public static String getStringRandom(int length) {
            String val = "";
            Random random = new Random();

            //参数length，表示生成几位随机数
            for(int i = 0; i < length; i++) {
                String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
                //输出字母还是数字
                if( "char".equalsIgnoreCase(charOrNum) ) {
                    //输出是大写字母还是小写字母
                    int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    val += (char)(random.nextInt(26) + temp);
                } else if( "num".equalsIgnoreCase(charOrNum) ) {
                    val += String.valueOf(random.nextInt(10));
                }
            }
            return val;
        }
}
