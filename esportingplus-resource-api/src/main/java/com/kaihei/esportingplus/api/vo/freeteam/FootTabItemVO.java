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
@ApiModel(description = "底部Tab内部服务配置模型")
public class FootTabItemVO implements Serializable {

    private static final long serialVersionUID = 4831125987272908953L;

    @ApiModelProperty(value = "id", position = 1, example = "1")
    private Integer id;

    @ApiModelProperty(value = "原始名称", position = 3, example = "暴鸡")
    @NotBlank(message = "原始名称不能为空")
    private String name;

    @ApiModelProperty(value = "编码", position = 3, example = "111")
    private String code;

    @ApiModelProperty(value = "当前名称", position = 4, example = "暴鸡")
    @NotBlank(message = "当前名称不能为空")
    private String curremtName;

    @ApiModelProperty(value = "图标,七牛图片地址", position = 5, example = "http://xxxx.png")
    @URL(message = "url地址有误")
    @NotBlank(message = "图标不能为空")
    private String icon;

    @ApiModelProperty(value = "所属tab的id(dic父id)", position = 2, example = "1")
    @NotNull(message = "所属tab的id不能为空")
    private Integer tabId;

    @ApiModelProperty(value = "有效状态, 0:下架，1：上架, 默认值", position = 5, example = "1")
    @Range(min = 0,max = 1,message = "状态有有误：要求区间0-1")
    @NotNull(message = "状态不能为空")
    private Byte status;

    @ApiModelProperty(value = "关联地址", position = 4, example = "前端落地页的id")
    @NotBlank(message = "关联地址不能为空")
    private String refLink;

    @ApiModelProperty(value = "排序号", position = 6, example = "1")
    private Integer orderIndex;

    @ApiModelProperty(value = "是否为默认落地页", position = 8,example = "0")
    @Range(min = 0,max = 1,message = "是否为默认落地页有有误：要求区间0-1")
    private Integer activeLandingPage;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}