package com.kaihei.esportingplus.riskrating.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity(name = "risk_device_white")
@Table(name = "risk_device_white")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskDeviceWhite extends AbstractEntity{

    private static final long serialVersionUID = 341276058586903895L;

    /***
     * 设备唯一识别号
     */
    @Column(columnDefinition = " varchar(100) NOT NULL COMMENT '设备唯一识别号'", length = 100)
    private String deviceNo;
    /***
     * 用户uid
     */
    @Column(columnDefinition = "varchar(200) NOT NULL DEFAULT '' COMMENT '设备描述'", length = 200)
    private String deviceDesc ;

    /***
     * 白名单设备状态，0 禁用 1启用
     */
    @Column(columnDefinition = "tinyint(4) NOT NULL DEFAULT '1' COMMENT '白名单设备状态 0,禁用，1启用'", length = 4)
    private Integer whiteStatus;

    /***
     * 操作员id
     */
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '操作员id'", length = 20)
    private Long operaterId;
    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getDeviceDesc() {
        return deviceDesc;
    }

    public void setDeviceDesc(String deviceDesc) {
        this.deviceDesc = deviceDesc;
    }

    public Integer getWhiteStatus() {
        return whiteStatus;
    }

    public void setWhiteStatus(Integer whiteStatus) {
        this.whiteStatus = whiteStatus;
    }

    public Long getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(Long operaterId) {
        this.operaterId = operaterId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
