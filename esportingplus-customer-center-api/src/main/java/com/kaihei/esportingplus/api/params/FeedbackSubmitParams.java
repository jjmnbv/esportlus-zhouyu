package com.kaihei.esportingplus.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 反馈与投诉提交参数
 *
 * @author yangshidong
 * @date 2018/12/3
 */
@Validated
@ApiModel("反馈与投诉提交参数")
public class FeedbackSubmitParams implements Serializable {

    private static final long serialVersionUID = 9154762619012968189L;

    @NotBlank(message = "反馈内容不能为空")
    @ApiModelProperty(value = "反馈内容", required = true, position = 0, example = "暴鸡水平有待提高")
    private String content;

    @ApiModelProperty(value = "图片url", required = false, position = 1, example = "https://www.baidu.com")
    private String url;

    @ApiModelProperty(value = "联系电话", required = false, position = 2, example = "13541526352")
    private String phone;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
