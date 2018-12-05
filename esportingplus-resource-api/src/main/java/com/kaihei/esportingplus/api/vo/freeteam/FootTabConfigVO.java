package com.kaihei.esportingplus.api.vo.freeteam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ApiModel(description = "底部Tab配置模型")
public class FootTabConfigVO implements Serializable {

    private static final long serialVersionUID = 4831125987272908953L;

    @ApiModelProperty(value = "id", position = 1, example = "1")
    private Integer id;

    @ApiModelProperty(value = "所属分类Code: 小程序=mp_foot_tab "
            + "| ios=ios_foot_tab | android=android_foot_tab", position = 2, example = "mp_foot_tab")
    @NotBlank(message = "所属分类Code不能为空")
    private String categoryCode;

    @ApiModelProperty(value = "原始名称", position = 3, example = "暴鸡")
    @NotBlank(message = "原始名称不能为空")
    private String name;

    @ApiModelProperty(value = "当前名称", position = 4, example = "暴鸡")
    @NotBlank(message = "当前名称不能为空")
    private String curremtName;

    @ApiModelProperty(value = "选中的图标,七牛图片地址", position = 5, example = "http://xxxx.png")
    @URL(message = "url地址有误")
    @NotBlank(message = "选中的图标不能为空")
    private String iconSelected;

    @ApiModelProperty(value = "未选中的图标,七牛图片地址", position = 5, example = "http://xxxx.png")
    @URL(message = "url地址有误")
    @NotBlank(message = "未选中的图标不能为空")
    private String iconDiselect;

    @ApiModelProperty(value = "排序号", position = 7, example = "1")
    @NotNull(message = "排序号不能为空")
    private Integer orderIndex;

    @ApiModelProperty(value = "默认落地页是否可配", position = 8,example = "0")
    @NotBlank(message = "默认落地页是否可配不能为空")
    private String activeLandingPage;

    @ApiModelProperty(value = "tab内服务排序是否可配", position = 9,example = "0")
    @NotBlank(message = "tab内服务排序是否可配不能为空")
    private String activeTabInsideOrder;

    @ApiModelProperty(value = "默认tab: 打开app后的默认展示tab", position = 10,example = "0")
    @NotBlank(message = "默认tab不能为空")
    private String activeDefaultTab;

    @ApiModelProperty(value = "有效状态, 0:禁用，1：启用, 默认值", position = 6, example = "1")
    @Range(min = 0,max = 1,message = "状态有有误：要求区间0-1")
    @NotNull(message = "状态不能为空")
    private Byte status;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}