package com.kaihei.esportingplus.user.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel("修改用户头像参数")
public class UpdateAvatarParams implements Serializable {

    private static final long serialVersionUID = 8445566910314622483L;
    @ApiModelProperty(value = "用户uid", required = true, position = 1, example = "")
    private String uid;
    @ApiModelProperty(value = "用户头像", required = true, position = 1, example = "")
    private String avatar;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
