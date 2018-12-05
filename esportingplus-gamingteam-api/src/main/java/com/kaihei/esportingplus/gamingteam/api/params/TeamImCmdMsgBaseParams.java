package com.kaihei.esportingplus.gamingteam.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel("车队通知基础参数类")
public class TeamImCmdMsgBaseParams implements Serializable{

    private static final long serialVersionUID = -3853430109745872385L;
    /**
     * 接受者uid或者uid列表，如果是群消息，则为群组id列表
     */
    @NotEmpty(message = "接受者不能为空")
    @ApiModelProperty(value = "消息接受者，一般为群组id", required = true, position = 1)
    private List<String> reciever;

    @ApiModelProperty(value = "仅用于ios, ios app 被杀掉之后, 推送通知", required = false, position = 1)
    private String pushContent;
    /**
     * uid列表，只适用于群组里的uid
     */
    @ApiModelProperty(value = "消息接受者，群组中的具体用户id", required = false, position = 1)
    private List<String> toUsers;

    @NotBlank(message = "车队序列号不能为空")
    @ApiModelProperty(value = "车队序列号", required = true, position = 1)
    private String teamSequence;

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

    public List<String> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<String> toUsers) {
        this.toUsers = toUsers;
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
