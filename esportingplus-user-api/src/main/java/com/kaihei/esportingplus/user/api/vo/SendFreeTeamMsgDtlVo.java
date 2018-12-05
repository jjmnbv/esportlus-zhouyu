package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @description:
 * @date: 2018/10/17 20:39
 */
public class SendFreeTeamMsgDtlVo implements Serializable {
    private static final long serialVersionUID = -1288555626895495463L;
    /**
     * 主标题
     */
    private String title;
    /**
     * 副标题
     */
    private String tips;
    /**
     * 内容
     */
    private List<SendMessageDataVo> service_infos;

    //private String order_id;
    /**
     * 消息类型
     * 1.跳转服务订单详情（旧版本）需要参数服务详情id，取order_id字段
     * 2.跳转免费车队首页          本地处理跳转逻辑
     * 3.五星好评跳车队详情        需要参数车队id，取extra字段
     * 4.其他（不跳转类型）        不跳转
     */
    private Integer messageType;
    /**
     * 跳转参数
     */
    private String extra;

    public SendFreeTeamMsgDtlVo(){
    }

    public SendFreeTeamMsgDtlVo(String title, String tips,List<SendMessageDataVo> service_infos, Integer messageType, String extra) {
        this.title = title;
        this.tips = tips;
        this.service_infos = service_infos;
        this.extra = extra;
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public List<SendMessageDataVo> getService_infos() {
        return service_infos;
    }

    public void setService_infos(List<SendMessageDataVo> service_infos) {
        this.service_infos = service_infos;
    }
}
