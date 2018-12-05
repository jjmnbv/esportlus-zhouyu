package com.kaihei.esportingplus.api.vo.freeteam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ApiModel(description = "底部Tab内部服务内容配置模型")
public class FootTabItemConfigVO implements Serializable {

    private static final long serialVersionUID = 4831125987272908953L;

    @ApiModelProperty(value = "所属tabId", position = 1, example = "1")
    @NotNull(message = "所属tabId不能为空")
    private Integer tabId;

    @ApiModelProperty(value = "所属tab名称", position = 2, example = "暴鸡")
    @NotBlank(message = "所属tab名称不能为空")
    private String tabName;

    @ApiModelProperty(value = "所属tab编码", position = 2, example = "ios_foot_tab")
    @NotBlank(message = "所属tab编码不能为空")
    private String tabCode;

    @ApiModelProperty(value = "有效状态, 0:禁用，1：启用, 默认值", position = 2, example = "ios_foot_tab")
    @NotBlank(message = "有效状态, 0:禁用，1：启用, 默认值")
    private Byte status;

    @ApiModelProperty(value = "默认落地页是否可配", position = 8,example = "0")
    private String activeLandingPage;

    @ApiModelProperty(value = "tab内服务排序是否可配", position = 9,example = "0")
    private String activeTabInsideOrder;

    @ApiModelProperty(value = "所属tab图标", position = 3, example = "http://xxxx.png")
    private String tabIconSelected;

    @ApiModelProperty(value = "所属tab未选中图标", position = 3, example = "http://xxxx.png")
    private String tabIconDiselect;

    @ApiModelProperty(value = "默认tab: 打开app后的默认展示tab", position = 10,example = "0")
    private String activeDefaultTab;

    @NotEmpty(message = "底部tab内部服务内容不能为空")
    @ApiModelProperty(value = "车队队员列表", required = true, position = 4)
    private List<FootTabItemVO> footTabItems;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}