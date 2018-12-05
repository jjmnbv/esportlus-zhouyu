package com.kaihei.esportingplus.api.params.freeteam;

import com.kaihei.esportingplus.api.enums.FreeTeamTypeSceneEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

/**
 * 免费车队类型 APP端查询参数
 * @author liangyi
 */
@Validated
@ApiModel(description = "免费车队类型APP端查询参数")
@Builder
@AllArgsConstructor
public class FreeTeamTypeAppQueryParams implements Serializable {

    private static final long serialVersionUID = -3660626780984936462L;

    /**
     * 车队类型 id
     */
    @ApiModelProperty(value = "免费车队类型id",
            required = false, position = 1, example = "222")
    private Integer freeTeamTypeId;

    /**
     * 用户身份 {@link com.kaihei.esportingplus.common.enums.UserIdentityEnum}
     */
    @ApiModelProperty(value = "用户身份",
            required = true, position = 2, example = "1")
    @NotNull(message = "用户身份不能为空")
    private Integer userIdentity;

    /**
     * 车队类型应用场景 {@link FreeTeamTypeSceneEnum}
     */
    @ApiModelProperty(value = "免费车队类型应用场景",
            required = true, position = 3, example = "1")
    @NotNull(message = "免费车队类型应用场景不能为空")
    @Range(min = 1, max = 2, message = "免费车队类型应用场景值为1~2")
    private Integer freeTeamTypeScene;

    public FreeTeamTypeAppQueryParams() {
    }

    public Integer getFreeTeamTypeId() {
        return freeTeamTypeId;
    }

    public void setFreeTeamTypeId(Integer freeTeamTypeId) {
        this.freeTeamTypeId = freeTeamTypeId;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Integer getFreeTeamTypeScene() {
        return freeTeamTypeScene;
    }

    public void setFreeTeamTypeScene(Integer freeTeamTypeScene) {
        this.freeTeamTypeScene = freeTeamTypeScene;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}