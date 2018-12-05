package com.kaihei.esportingplus.gamingteam.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhangfang
 */
@Validated
@ToString
@ApiModel("解散群组通知")
public class ImDismissGroupParams extends ImGroupBaseParams {

    private static final long serialVersionUID = 8203503314651907354L;

    /**
     * 提示语内容
     */
    @NotBlank(message = "提示语不能为空")
    private String msgContent;
    /**
     * 系统解散需要发给队长
     */
    private boolean sendLeader =false;
    /**
     * 推送的成员uid
     */
    @NotEmpty(message = "队员uid不能为空")
    @ApiModelProperty(value = "成员uid集合", required = true, position = 1)
    private List<String> members;

    @NotBlank(message = "车队序列号不能为空")
    @ApiModelProperty(value = "车队序列号", required = true, position = 1)
    private String teamSequence;

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public boolean isSendLeader() {
        return sendLeader;
    }

    public void setSendLeader(boolean sendLeader) {
        this.sendLeader = sendLeader;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
