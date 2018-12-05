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
 * 车队类型 APP端查询参数
 * @author liangyi
 */
@Validated
@ApiModel(description = "车队类型APP端查询参数")
@Builder
@AllArgsConstructor
public class TeamTypeAppQueryParams implements Serializable {

    private static final long serialVersionUID = -3660626780984936462L;

    /**
     * 车队类型 id
     */
    @ApiModelProperty(value = "车队类型id",
            required = false, position = 1, example = "1")
    private Integer teamTypeId;

    /**
     * 车队类型分类 {@link com.kaihei.esportingplus.common.enums.TeamCategoryEnum}
     */
    @ApiModelProperty(value = "车队类型分类",
            required = false, position = 2, example = "0")
    private Integer category;

    /**
     * 用户身份 {@link com.kaihei.esportingplus.common.enums.UserIdentityEnum}
     */
    @ApiModelProperty(value = "用户身份",
            required = true, position = 3, example = "1")
    @NotNull(message = "用户身份不能为空")
    private Integer userIdentity;

    /**
     * 车队类型应用场景 {@link FreeTeamTypeSceneEnum}
     */
    @ApiModelProperty(value = "车队类型应用场景",
            required = true, position = 4, example = "1")
    @NotNull(message = "车队类型应用场景不能为空")
    @Range(min = 1, max = 2, message = "车队类型应用场景值为1~2")
    private Integer teamTypeScene;

    public TeamTypeAppQueryParams() {
    }

    public Integer getTeamTypeId() {
        return teamTypeId;
    }

    public void setTeamTypeId(Integer teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Integer getTeamTypeScene() {
        return teamTypeScene;
    }

    public void setTeamTypeScene(Integer teamTypeScene) {
        this.teamTypeScene = teamTypeScene;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}