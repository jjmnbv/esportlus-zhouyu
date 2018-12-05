package com.kaihei.esportingplus.riskrating.api.vo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RiskDeviceWhiteVo implements Serializable{

    private static final long serialVersionUID = -4145952071943473160L;
    private Long id;
    /***
     * 设备唯一识别号
     */
    private String deviceNo;
    /***
     * 用户uid
     */
    private String deviceDesc ;

    /***
     * 白名单设备状态，0 禁用 1启用
     */
    private Integer whiteStatus;
    /**
     * 操作员id
     */
    private Long operaterId;
    private Date lastModifiedDate;
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
