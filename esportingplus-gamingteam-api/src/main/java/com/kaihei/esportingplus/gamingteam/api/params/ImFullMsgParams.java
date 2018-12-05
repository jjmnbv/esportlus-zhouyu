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

/**
 * @author
 */
@Validated
@ApiModel("满员通知")
public class ImFullMsgParams implements Serializable {

    private static final long serialVersionUID = 3179123686464650808L;
    @ApiModelProperty(value = "群组id", required = true, position = 1, example = "")
    @NotBlank(message = "群组id不能为空")
    private String groupId;
    /**
     * 提示语内容
     */
    @NotBlank(message = "提示语不能为空")
    private String msgContent;

    @NotBlank(message = "车队序列号不能为空")
    @ApiModelProperty(value = "车队序列号", required = true, position = 1)
    private String teamSequence;

    @NotEmpty(message = "队员uid不能为空")
    @ApiModelProperty(value = "成员uid集合", required = true, position = 1)
    private List<String> members;
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
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
