/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.kaihei.esportingplus.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "投诉订单创建参数", description = "投诉订单创建参数")
public final class ComplainCreateParam implements Serializable {

    private static final long serialVersionUID = 1783402123062749704L;

    @NotEmpty(message = "订单序列号bizOrderSequeue不能为空")
    @ApiModelProperty(value = "订单序列号bizOrderSequeue", required = true, position = 1, example = "20180820163345228956821526941696")
    private String bizOrderSequeue;

    @NotEmpty(message = "投诉类型type不能为空")
    @ApiModelProperty(value = "投诉类型type", required = true, position = 2, example = "0")
    private Integer complainType;

    @ApiModelProperty(value = "投诉内容complainContent（可选）", required = false, position = 3, example = "其他原因")
    private String complainContent;

    @NotEmpty(message = "凭证图片complainImgUrl不能为空")
    @ApiModelProperty(value = "凭证图片不能为空", required = true, position = 4, example = "https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKb")
    private List<String> complainImgUrlList;

    public String getBizOrderSequeue() {
        return bizOrderSequeue;
    }

    public void setBizOrderSequeue(String bizOrderSequeue) {
        this.bizOrderSequeue = bizOrderSequeue;
    }

    public Integer getComplainType() {
        return complainType;
    }

    public void setComplainType(Integer complainType) {
        this.complainType = complainType;
    }

    public String getComplainContent() {
        return complainContent;
    }

    public void setComplainContent(String complainContent) {
        this.complainContent = complainContent;
    }

    public List<String> getComplainImgUrlList() {
        return complainImgUrlList;
    }

    public void setComplainImgUrlList(List<String> complainImgUrlList) {
        this.complainImgUrlList = complainImgUrlList;
    }

    @Override
    public String toString() {
        return "ComplainCreateParam{" +
                "bizOrderSequeue='" + bizOrderSequeue + '\'' +
                ", complainType=" + complainType +
                ", complainContent='" + complainContent + '\'' +
                ", complainImgUrlList=" + complainImgUrlList +
                '}';
    }
}
