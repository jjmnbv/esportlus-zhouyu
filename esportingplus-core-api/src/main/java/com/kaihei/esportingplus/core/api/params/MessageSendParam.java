package com.kaihei.esportingplus.core.api.params;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * @Author liuyang
 * @Description 发送消息参数
 * @Date 2018/10/29 11:30
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class MessageSendParam extends MessageParam{

    /** 模板id*/
    @NotNull(message = "模板id不能为空")
    private int templateId;

    /** 模板数据*/
    private Map data;

    /** uid列表, 只用是群消息, 发送给群里面的指定的uid*/
    private List<String> toUsers;

    /** 自己是否接收消息*/
    private Boolean toSelf;

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
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
