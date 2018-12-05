package com.kaihei.esportingplus.core.api.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @Author liuyang
 * @Description 用户标签参数
 * @Date 2018/11/12 14:27
 **/
@Validated
public class UserTagParam  implements Serializable {

    @NotEmpty(message = "用户 Id不能为空")
    private List<String> userIds;

    @NotEmpty(message = "用户tag不能为空")
    private List<String> tags;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
