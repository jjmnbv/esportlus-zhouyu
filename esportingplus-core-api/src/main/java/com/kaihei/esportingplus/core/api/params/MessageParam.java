package com.kaihei.esportingplus.core.api.params;

import com.kaihei.esportingplus.core.api.enums.MessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import lombok.Data;

/**
 * @Author liuyang
 * @Date 2018/11/1 18:08
 **/
@Data
public class MessageParam {
    /**
     * 发送者uid
     */
    @ApiModelProperty(value = "发送者uid", required = true)
    @NotBlank(message = "发送者uid不能为空")
    private String sender;

    /**
     * 接受者
     */
    @ApiModelProperty(value = "接受者", required = true)
    @NotEmpty(message = "接受者不能为空")
    private List<String> reciever;

    /** 消息类型*/
    @NotNull(message = "消息类型不能为空")
    private MessageTypeEnum messageType;

    /**消息子类型*/
    private MessageSubType msgSubType;

    /**
     * 定义显示的 Push 内容，如果 objectName 为融云内置消息类型时，则发送后用户一定会收到 Push 信息。 如果为自定义消息，则 pushContent 为自定义消息显示的 Push 内容，如果不传则用户不会收到 Push 通知。(可选)
     */
    @ApiModelProperty(value = "定义显示的 Push 内容，如果 objectName 为融云内置消息类型时，则发送后用户一定会收到 Push 信息。 如果为自定义消息，则 pushContent 为自定义消息显示的 Push 内容，如果不传则用户不会收到 Push 通知。(可选)", required = false)
    private String pushContent;

    @ApiModelProperty(value = "针对 iOS 平台为 Push 通知时附加到 payload 中，客户端获取远程推送内容时为 appData 查看详细，Android 客户端收到推送消息时对应字段名为 pushData。(可选)", required = false)
    private String pushData;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReciever() {
        return reciever;
    }

    public void setReciever(List<String> reciever) {
        this.reciever = reciever;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getPushData() {
        return pushData;
    }

    public void setPushData(String pushData) {
        this.pushData = pushData;
    }

    public MessageTypeEnum getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypeEnum messageType) {
        this.messageType = messageType;
    }

    public MessageSubType getMsgSubType() {
        return msgSubType;
    }

    public void setMsgSubType(MessageSubType msgSubType) {
        this.msgSubType = msgSubType;
    }
}
