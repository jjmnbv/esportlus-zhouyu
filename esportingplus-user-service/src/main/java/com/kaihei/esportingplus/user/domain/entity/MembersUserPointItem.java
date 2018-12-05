package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "members_user_point_item")
public class MembersUserPointItem {
    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     */
    @Column(name = "item_type")
    private Integer itemType;

    /**
     * 积分明细值
     */
    @Column(name = "item_acmount")
    private Integer itemAcmount;

    /**
     * 积分来源操作用户
     */
    @Column(name = "operation_user_id")
    private Integer operationUserId;

    /**
     * 鸡分明细说明
     */
    private String instructions;

    /**
     * 创建时间
     */
    @Column(name = "create_datetime")
    private Date createDatetime;

    /**
     * 车队标识符
     */
    private String slug;

    public MembersUserPointItem(Integer id, Integer userId, Integer itemType, Integer itemAcmount, Integer operationUserId, String instructions, Date createDatetime, String slug) {
        this.id = id;
        this.userId = userId;
        this.itemType = itemType;
        this.itemAcmount = itemAcmount;
        this.operationUserId = operationUserId;
        this.instructions = instructions;
        this.createDatetime = createDatetime;
        this.slug = slug;
    }

    public MembersUserPointItem() {
        super();
    }

    /**
     * 获取自增主键
     *
     * @return id - 自增主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置自增主键
     *
     * @param id 自增主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     *
     * @return item_type - 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     */
    public Integer getItemType() {
        return itemType;
    }

    /**
     * 设置明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     *
     * @param itemType 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     */
    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    /**
     * 获取积分明细值
     *
     * @return item_acmount - 积分明细值
     */
    public Integer getItemAcmount() {
        return itemAcmount;
    }

    /**
     * 设置积分明细值
     *
     * @param itemAcmount 积分明细值
     */
    public void setItemAcmount(Integer itemAcmount) {
        this.itemAcmount = itemAcmount;
    }

    /**
     * 获取积分来源操作用户
     *
     * @return operation_user_id - 积分来源操作用户
     */
    public Integer getOperationUserId() {
        return operationUserId;
    }

    /**
     * 设置积分来源操作用户
     *
     * @param operationUserId 积分来源操作用户
     */
    public void setOperationUserId(Integer operationUserId) {
        this.operationUserId = operationUserId;
    }

    /**
     * 获取鸡分明细说明
     *
     * @return instructions - 鸡分明细说明
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * 设置鸡分明细说明
     *
     * @param instructions 鸡分明细说明
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions == null ? null : instructions.trim();
    }

    /**
     * 获取创建时间
     *
     * @return create_datetime - 创建时间
     */
    public Date getCreateDatetime() {
        return createDatetime;
    }

    /**
     * 设置创建时间
     *
     * @param createDatetime 创建时间
     */
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * 获取车队标识符
     *
     * @return slug - 车队标识符
     */
    public String getSlug() {
        return slug;
    }

    /**
     * 设置车队标识符
     *
     * @param slug 车队标识符
     */
    public void setSlug(String slug) {
        this.slug = slug == null ? null : slug.trim();
    }
}