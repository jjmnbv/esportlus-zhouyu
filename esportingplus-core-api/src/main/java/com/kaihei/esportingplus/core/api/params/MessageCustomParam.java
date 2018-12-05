package com.kaihei.esportingplus.core.api.params;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author liuyang
 * @Description 自定义消息参数
 * @Date 2018/11/1 18:22
 **/
@Validated
public class MessageCustomParam extends MessageParam {

    /**
     * 自定义code
     */
    @NotBlank(message = "code 不能为空")
    private String code;

    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 单聊（1） 群聊（2） 系统（3）
     */
    @ApiModelProperty(value = "单聊（1） 群聊（2） 系统（3）", required = true)
    @Range(max = 3, min = 1, message = "参数有有误，取值区间为：单聊（1） 群聊（2） 系统（3）")
    private Integer type;
    /**
     * uid列表, 只用是群消息, 发送给群里面的指
     */
    private List<String> toUsers;

    /**
     * 自己是否接收消息
     */
    private Boolean toSelf;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<String> toUsers) {
        this.toUsers = toUsers;
    }

    public Boolean getToSelf() {
        return toSelf;
    }

    public void setToSelf(Boolean toSelf) {
        this.toSelf = toSelf;
    }
}
