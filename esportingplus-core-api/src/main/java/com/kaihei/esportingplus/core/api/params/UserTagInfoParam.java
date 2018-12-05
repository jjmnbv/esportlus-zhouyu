package com.kaihei.esportingplus.core.api.params;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @program: esportingplus
 * @description: 用户标签入参
 * @author: xusisi
 * @create: 2018-12-04 17:01
 **/
@Validated
public class UserTagInfoParam {

    /***
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    @ApiModelProperty(name = "标签名称", required = true)
    private String tagName;

    /***
     * 包含用户标签信息的TXT文件路径
     */
    @NotBlank(message = "文件路径不能为空")
    @ApiModelProperty(name = "文件路径", required = true)
    private String filePath;

    /***
     * tag主键ID
     */
    @ApiModelProperty(name = "tagId")
    private Integer tagId;

    @NotBlank(message = "数据操作人不能为空")
    @ApiModelProperty(name = "operator", required = true)
    private String operator;

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
