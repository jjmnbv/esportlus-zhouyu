package com.kaihei.esportingplus.gamingteam.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 加入用户组，包含发送消息提示
 *
 * @author zhangfang
 */
@Builder
@Validated
@ApiModel("离开群组通知")
@NoArgsConstructor
@AllArgsConstructor
public class ImGroupLeavelParams extends ImGroupBaseParams {

    /**
     * 欢迎的提示语内容
     */
    @NotBlank(message = "对他人的提示语不能为空")
    @ApiModelProperty(value = "对他人的提示语", required = true, position = 1)
    private String toOtheMsgContent;
    /**
     * 离开类型,1主动，2被动
     */
    @NotNull(message = "离开方式，1主动，2被动")
    @ApiModelProperty(value = "离开方式，1主动，2被动", required = true, position = 1)
    private Integer leaveType;
    /**
     * 离开时，自己的提示语，在被动离开时需填写，主动不用填写
     */
    @ApiModelProperty(value = "自己的提示语，在被动离开时需填写，主动不用填写", required = true, position = 1)
    private String toSelfMsgContent;

    @NotBlank(message = "车队序列号不能为空")
    @ApiModelProperty(value = "车队序列号", required = true, position = 1)
    private String teamSequence;
    /**
     * 推送的成员uid
     */
    @NotEmpty(message = "队员uid不能为空")
    @ApiModelProperty(value = "成员uid集合", required = true, position = 1)
    private List<String> members;

    public String getToOtheMsgContent() {
        return toOtheMsgContent;
    }

    public void setToOtheMsgContent(String toOtheMsgContent) {
        this.toOtheMsgContent = toOtheMsgContent;
    }

    public Integer getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    public String getToSelfMsgContent() {
        return toSelfMsgContent;
    }

    public void setToSelfMsgContent(String toSelfMsgContent) {
        this.toSelfMsgContent = toSelfMsgContent;
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
