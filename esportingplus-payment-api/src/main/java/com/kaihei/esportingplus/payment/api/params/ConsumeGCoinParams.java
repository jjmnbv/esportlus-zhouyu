package com.kaihei.esportingplus.payment.api.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 暴鸡币消费接口参数
 * @author: xiaolijun
 **/
@ApiModel(value = "暴鸡币消费接口参数", description = "暴鸡币消费接口参数")
@Validated
public class ConsumeGCoinParams implements Serializable {

	private static final long serialVersionUID = -1420578774468972448L;

	// 打赏用户ID
    @NotBlank(message = "打赏用户ID不能为空")
    @ApiModelProperty(value = " 打赏用户ID", required = true, position = 1, example = "223867704556126208")
    @JsonProperty("sourceUserId")
    private String sourceUserId;
    
    // 被打赏用户ID
    @NotBlank(message = "被打赏用户ID不能为空")
    @ApiModelProperty(value = "被打赏用户ID", required = true, position = 2, example = "223867704556126208")
    @JsonProperty("receiveUserId")
    private String receiveUserId;
    
    // 描述
    @ApiModelProperty(value = "描述", required = false, position = 3, example = "描述")
    private String body;
    
    // 暴鸡币消费额
    @NotNull(message = "暴鸡币金额不能为空")
    @ApiModelProperty(value = "暴鸡币金额", required = true, position = 4, example = "11.00")
    @JsonProperty("gcoinAmount")
    private BigDecimal gcoinAmount;
    
    // 被打赏者收益暴击值额
    @NotNull(message = "暴击值金额不能为空")
    @ApiModelProperty(value = "暴击值金额", required = true, position = 5, example = "11.00")
    @JsonProperty("starlightAmount")
    private BigDecimal starlightAmount;
    
    // 操作终端（001表示PC端、002表示Android、003表示iOS）
    @NotBlank(message = "操作终端不能为空")
    @ApiModelProperty(value = "操作终端", required = true, position = 6, example = "001")
    @JsonProperty("sourceId")
    private String sourceId;

    // 主题
    @NotBlank(message = "主题不能为空")
    @ApiModelProperty(value = "主题", required = true, position = 7, example = "主题")
    @JsonProperty("subject")
    private String subject;

    public String getSubject() {
      return subject;
    }

    public void setSubject(String subject) {
      this.subject = subject;
    }

    public String getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(String sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public BigDecimal getStarlightAmount() {
		return starlightAmount;
	}

	public void setStarlightAmount(BigDecimal starlightAmount) {
		this.starlightAmount = starlightAmount;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public BigDecimal getGcoinAmount() {
		return gcoinAmount;
	}

	public void setGcoinAmount(BigDecimal gcoinAmount) {
		this.gcoinAmount = gcoinAmount;
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
