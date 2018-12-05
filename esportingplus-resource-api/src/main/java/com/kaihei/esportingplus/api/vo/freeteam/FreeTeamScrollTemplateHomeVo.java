package com.kaihei.esportingplus.api.vo.freeteam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
@ApiModel("免费车队首页滚动模板")
public class FreeTeamScrollTemplateHomeVo {

    /**
     * 滚动文字id
     */
    @ApiModelProperty(value = "模板id", required = false, position = 1, example = "")
    private Integer templateId;
    /**
     * 滚动文字模板
     */
    @ApiModelProperty(value = "模板内容", required = false, position = 1, example = "")
    private String scrollTemplate;

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getScrollTemplate() {
        return scrollTemplate;
    }

    public void setScrollTemplate(String scrollTemplate) {
        this.scrollTemplate = scrollTemplate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
