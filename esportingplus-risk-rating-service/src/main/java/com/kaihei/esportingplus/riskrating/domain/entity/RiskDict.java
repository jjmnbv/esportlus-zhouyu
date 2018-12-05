package com.kaihei.esportingplus.riskrating.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 风控服务字典表
 **/
@Entity(name = "risk_dict")
@Table(name = "risk_dict")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskDict extends AbstractEntity {

    private static final long serialVersionUID = 1377835422947076638L;
    /***
     * 分组code
     */
    @Column(columnDefinition = "varchar(30) NOT NULL COMMENT '分组code'", length = 30)
    private String groupCode;
    /***
     * 分组名称
     */
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '分组名称'", length = 100)
    private String groupName;

    /***
     * 单项code
     */
    @Column(columnDefinition = "varchar(60) NOT NULL COMMENT '单项code'", length = 60)
    private String itemCode;

    /***
     * 单项name
     */
    @Column(columnDefinition = "varchar(100) NOT NULL COMMENT '单项name'", length = 100)
    private String itemName;

    /***
     * 参数值
     */
    @Column(columnDefinition = "varchar(200) NOT NULL COMMENT '参数值'", length = 200)
    private String itemValue;

    /***
     * 充值金额（单位为元，两位小数）
     */
    @Column(columnDefinition = "tinyint(4) NOT NULL DEFAULT '1' COMMENT '0,不生效，1生效'", length = 4)
    private Integer dictStatus;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public Integer getDictStatus() {
        return dictStatus;
    }

    public void setDictStatus(Integer dictStatus) {
        this.dictStatus = dictStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
