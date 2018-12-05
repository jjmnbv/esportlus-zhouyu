package com.kaihei.esportingplus.resource.domain.entity.freeteam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队结算方式
 *
 * @author liangyi
 */
@Table(name = "team_settlement_mode")
@Builder
@AllArgsConstructor
public class TeamSettlementMode implements Serializable {

    private static final long serialVersionUID = -232876495439853492L;

    /**
     * 车队结算方式主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 所属车队类型id
     */
    @Column(name = "team_type_id")
    private Integer teamTypeId;

    /**
     * 结算类型id,来自数据字典(小时/局数)
     */
    @Column(name = "settlement_type_id")
    private Integer settlementTypeId;

    /**
     * 结算数量
     */
    @Column(name = "settlement_number")
    private BigDecimal settlementNumber;

    /**
     * 排序,值越大越靠前
     */
    @Column(name = "order_index")
    private Integer orderIndex;

    /**
     * 状态, 0:失效,1:有效
     */
    private Byte status;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    public TeamSettlementMode() {
        super();
    }

    /**
     * 获取车队结算方式主键id
     *
     * @return id - 车队结算方式主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置车队结算方式主键id
     *
     * @param id 车队结算方式主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取所属车队类型id
     *
     * @return team_type_id - 所属车队类型id
     */
    public Integer getTeamTypeId() {
        return teamTypeId;
    }

    /**
     * 设置所属车队类型id
     *
     * @param teamTypeId 所属车队类型id
     */
    public void setTeamTypeId(Integer teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    /**
     * 获取结算类型id,来自数据字典(小时/局数)
     *
     * @return settlement_type_id - 结算类型id,来自数据字典(小时/局数)
     */
    public Integer getSettlementTypeId() {
        return settlementTypeId;
    }

    /**
     * 设置结算类型id,来自数据字典(小时/局数)
     *
     * @param settlementTypeId 结算类型id,来自数据字典(小时/局数)
     */
    public void setSettlementTypeId(Integer settlementTypeId) {
        this.settlementTypeId = settlementTypeId;
    }

    /**
     * 获取结算数量
     *
     * @return settlement_number - 结算数量
     */
    public BigDecimal getSettlementNumber() {
        return settlementNumber;
    }

    /**
     * 设置结算数量
     *
     * @param settlementNumber 结算数量
     */
    public void setSettlementNumber(BigDecimal settlementNumber) {
        this.settlementNumber = settlementNumber;
    }

    /**
     * 获取排序,值越大越靠前
     *
     * @return order_index - 排序,值越大越靠前
     */
    public Integer getOrderIndex() {
        return orderIndex;
    }

    /**
     * 设置排序,值越大越靠前
     *
     * @param orderIndex 排序,值越大越靠前
     */
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    /**
     * 获取状态, 0:失效,1:有效
     *
     * @return status - 状态, 0:失效,1:有效
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态, 0:失效,1:有效
     *
     * @param status 状态, 0:失效,1:有效
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}