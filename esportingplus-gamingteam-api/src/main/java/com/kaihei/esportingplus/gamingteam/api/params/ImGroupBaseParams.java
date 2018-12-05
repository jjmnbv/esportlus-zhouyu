package com.kaihei.esportingplus.gamingteam.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhangfang
 */
@Validated
@ApiModel("群组基础参数")
public class ImGroupBaseParams implements Serializable {

    private static final long serialVersionUID = -8775436278962984418L;
    @ApiModelProperty(value = "用户uid", required = true, position = 1, example = "")
    @NotBlank(message = "用户uid不能为空")
    private String uid;
    @ApiModelProperty(value = "群组id", required = true, position = 1, example = "")
    @NotBlank(message = "群组id不能为空")
    private String groupId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
