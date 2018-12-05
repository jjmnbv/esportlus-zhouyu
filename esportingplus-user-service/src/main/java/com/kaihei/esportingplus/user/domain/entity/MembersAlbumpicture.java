package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "members_albumpicture")
public class MembersAlbumpicture {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 图片路径
     */
    @Column(name = "picture_path")
    private String picturePath;

    /**
     * 图片状态
     */
    private Integer status;

    /**
     * 权重
     */
    private Integer weights;

    /**
     * 审核时间
     */
    @Column(name = "verify_datetime")
    private Date verifyDatetime;

    /**
     * 上传时间
     */
    @Column(name = "create_datetime")
    private Date createDatetime;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 审核人id
     */
    @Column(name = "verify_user_id")
    private Integer verifyUserId;

    public MembersAlbumpicture(Integer id, String picturePath, Integer status, Integer weights, Date verifyDatetime, Date createDatetime, Integer userId, Integer verifyUserId) {
        this.id = id;
        this.picturePath = picturePath;
        this.status = status;
        this.weights = weights;
        this.verifyDatetime = verifyDatetime;
        this.createDatetime = createDatetime;
        this.userId = userId;
        this.verifyUserId = verifyUserId;
    }

    public MembersAlbumpicture() {
        super();
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return picture_path
     */
    public String getPicturePath() {
        return picturePath;
    }

    /**
     * @param picturePath
     */
    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath == null ? null : picturePath.trim();
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return weights
     */
    public Integer getWeights() {
        return weights;
    }

    /**
     * @param weights
     */
    public void setWeights(Integer weights) {
        this.weights = weights;
    }

    /**
     * @return verify_datetime
     */
    public Date getVerifyDatetime() {
        return verifyDatetime;
    }

    /**
     * @param verifyDatetime
     */
    public void setVerifyDatetime(Date verifyDatetime) {
        this.verifyDatetime = verifyDatetime;
    }

    /**
     * @return create_datetime
     */
    public Date getCreateDatetime() {
        return createDatetime;
    }

    /**
     * @param createDatetime
     */
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return verify_user_id
     */
    public Integer getVerifyUserId() {
        return verifyUserId;
    }

    /**
     * @param verifyUserId
     */
    public void setVerifyUserId(Integer verifyUserId) {
        this.verifyUserId = verifyUserId;
    }
}