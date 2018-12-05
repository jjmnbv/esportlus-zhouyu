package com.kaihei.esportingplus.core.api.params;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author liuyang
 * @Description 黑名单参数
 * @Date 2018/10/26 16:16
 **/
@Validated
public class UserBlackParam {

    /**用户 Id*/
    @NotBlank(message = "用户id不能为空")
    private String userId;

    /** 被加黑的用户Id*/
    @NotEmpty(message = "被加黑的用户Id不能为空")
    private List<String> blackUserIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getBlackUserId() {
        return blackUserIds;
    }

    public List<String> getBlackUserIds() {
        return blackUserIds;
    }

    public void setBlackUserIds(List<String> blackUserIds) {
        this.blackUserIds = blackUserIds;
    }
}
