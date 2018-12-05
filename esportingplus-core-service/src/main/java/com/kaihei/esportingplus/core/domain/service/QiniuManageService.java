package com.kaihei.esportingplus.core.domain.service;

import com.kaihei.esportingplus.core.api.vo.QiniuImageCheckVo;
import com.kaihei.esportingplus.core.api.vo.QiniuTokenVo;

import java.util.List;

/**
 * @author zl.zhao
 * @description: 七牛服务
 * @date: 2018/10/29 19:35
 */
public interface QiniuManageService {
    /**
     * 七牛图片上传（获取七牛token）
     *
     * @param tokenType "thumbnail_uploadtoken";//头像上传
     *                  "idcard_uploadtoken";//身份证上传
     *                  "screenshot_uploadtoken";//截图上传
     *                  "highest_uploadtoken";//最高段位截图证明
     *                  "applyrefund_uploadtoken";//投诉截图上传
     *                  "feedback_uploadtoken";//反馈与建议上传图
     *                  "cert_album_uploadtoken";//成为暴娘三张图片
     *                  "album_uploadtoken";//个人相册
     *                  "video_uploadtoken";//个人视频
     *                  "discovery_uploadtoken";//发布陪玩的相片
     *                  "gameresults_uploadtoken";//暴鸡赛结果截图
     *                  "discovery_video_uploadtoken";//动态的视频
     * @param pictureNo
     * @param serialNo
     * @return
     */
    public QiniuTokenVo getTokenByTokenType(String tokenType, String gameOrderId, String pictureNo, String serialNo);

    /**
     * 七牛鉴黄，鉴暴恐
     * @param url
     */
    //public QiniuImageCheckVo checkImage(String url);

    /**
     * 鉴暴恐
     * @param url
     * @return
     */
    public QiniuImageCheckVo checkQterrorImage(String url);

    /**
     * 鉴黄
     * @param url
     * @return
     */
    public QiniuImageCheckVo checkQpulpImage(String url);

    /**
     * （首次注册）上传头像获取token
     *
     * @param
     * @return
     */
    public QiniuTokenVo getTokenByAvatar();
}
