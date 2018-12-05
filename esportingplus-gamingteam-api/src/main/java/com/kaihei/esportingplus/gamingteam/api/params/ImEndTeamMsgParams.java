package com.kaihei.esportingplus.gamingteam.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 车队服务结束通知
 * @author zhangfang
 */
@Validated
@ApiModel("队长结束车队服务消息")
public class ImEndTeamMsgParams extends ImGroupBaseParams {

    private static final long serialVersionUID = -680117672339630503L;
    /**
     * 提示语内容
     */
    @NotBlank(message = "提示语不能为空")
    private String msgContent;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
