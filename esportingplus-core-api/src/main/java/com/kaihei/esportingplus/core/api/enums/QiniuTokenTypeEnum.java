package com.kaihei.esportingplus.core.api.enums;

/**
 * @author zl.zhao
 * @description:七牛token类型枚举
 * @date: 2018/10/25 14:29
 */
public enum QiniuTokenTypeEnum {
    /**
     * 头像上传
     */
    THUMBNAIL_UPLOADTOKEN("thumbnail_uploadtoken", "头像上传", "baoji-pub"),

    /**
     * 身份证上传
     */
    IDCARD_UPLOADTOKEN("idcard_uploadtoken", "身份证上传","baoji"),
    /**
     * 截图上传
     */
    SCREENSHOT_UPLOADTOKEN("screenshot_uploadtoken", "截图上传","'baoji'"),

    /**
     * 最高段位截图证明
     */
    HIGHEST_UPLOADTOKEN("highest_uploadtoken", "最高段位截图证明", "baoji-pub"),
    /**
     * 投诉截图上传
     */
    applyrefund_uploadtoken("applyrefund_uploadtoken", "投诉截图上传","baoji"),

    /**
     * 反馈与建议上传图
     */
    FEEDBACK_UPLOADTOKEN("feedback_uploadtoken", "反馈与建议上传图","baoji-pub"),
    /**
     * 成为暴娘三张图片
     */
    CERT_ALBUM_UPLOADTOKEN("cert_album_uploadtoken", "成为暴娘三张图片","bn-pub"),

    /**
     * 个人相册
     */
    ALBUM_UPLOADTOKEN("album_uploadtoken", "个人相册","bn-pub"),
    /**
     * 个人视频
     */
    VIDEO_UPLOADTOKEN("video_uploadtoken", "个人视频","bn-pub"),

    /**
     * 发布陪玩的相片
     */
    DISCOVERY_UPLOADTOKEN("discovery_uploadtoken", "发布陪玩的相片","discover"),
    /**
     * 暴鸡赛结果截图
     */
    GAMERESULTS_UPLOADTOKEN("gameresults_uploadtoken", "暴鸡赛结果截图","baoji-pub"),

    /**
     * 动态的视频
     */
    DISCOVERY_VIDEO_UPLOADTOKEN("discovery_video_uploadtoken", "动态的视频","discovery-video");

    private String code;
    private String msg;
    private String bucket;

    QiniuTokenTypeEnum(String code, String msg, String bucket) {
        this.code = code;
        this.msg = msg;
        this.bucket = bucket;
    }
    public String getCode() {
        return code;
    }
    public String getBucket() {
        return bucket;
    }
    public String getMsg(){
        return msg;
    }


    public static String getBucketByCode(String code){
        QiniuTokenTypeEnum[] values = QiniuTokenTypeEnum.values();
        for (QiniuTokenTypeEnum qiniuTokenTypeEnum : values) {
            if (qiniuTokenTypeEnum.getCode().equals(code)) {
                return qiniuTokenTypeEnum.getBucket();
            }
        }
        return null;
    }
}
