package com.kaihei.esportingplus.api.vo.freeteam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import net.bytebuddy.asm.Advice.AllArguments;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 免费车队第三方分享文案配置
 * @author liangyi
 */
@Validated
@ApiModel(description = "免费车队第三方分享文案配置")
@Builder
@AllArgsConstructor
public class FreeTeamEscrowShareTextVO implements Serializable {

    private static final long serialVersionUID = -5841056251127381724L;

    /**
     * 分享文案标题
     */
    @NotEmpty
    @ApiModelProperty(value = "分享文案标题不能为空",
            required = true, position = 1, example = "没时间解释了, 快上车!")
    private String shareTitle;

    /**
     * 分享文案内容
     */
    @NotEmpty
    @ApiModelProperty(value = "分享文案内容不能为空",
            required = true, position = 2, example = "你根本不是司机!")
    private String shareContent;

    public FreeTeamEscrowShareTextVO() {
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}