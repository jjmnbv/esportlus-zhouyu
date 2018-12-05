package com.kaihei.esportingplus.gamingteam.api.vo;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
public class TeamImCmdMsgVO {

    @NotBlank(message = "发送者不能为空")
    private String sender;
    @NotEmpty(message = "接受者不能为空")
    private List<String> reciever;
    @NotNull(message = "模版Id不能为空")
    private Integer templateId;
    @NotNull(message = "消息内容不能为空")
    private TeamImCmdMsgData data;
    private String pushContent;
    private List<String> toUsers;

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

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public TeamImCmdMsgData getData() {
        return data;
    }

    public void setData(TeamImCmdMsgData data) {
        this.data = data;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public List<String> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<String> toUsers) {
        this.toUsers = toUsers;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
