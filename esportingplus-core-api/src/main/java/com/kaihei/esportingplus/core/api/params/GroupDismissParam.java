package com.kaihei.esportingplus.core.api.params;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/11/16 14:27
 **/
@Validated
public class GroupDismissParam {

    @NotBlank(message = "操作用户id不能为空")
    private String userId;

    @NotBlank(message = "要解散的群id不能为空")
    private String groupId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
