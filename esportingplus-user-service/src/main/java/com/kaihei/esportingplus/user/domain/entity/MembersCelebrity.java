package com.kaihei.esportingplus.user.domain.entity;

import javax.persistence.*;

@Table(name = "members_celebrity")
public class MembersCelebrity {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String username;

    private String uid;

    private String thumbnail;

    private Short sex;

    private String desc;

    private Short game;

    @Column(name = "order_count")
    private Integer orderCount;

    public MembersCelebrity(Integer id, String username, String uid, String thumbnail, Short sex, String desc, Short game, Integer orderCount) {
        this.id = id;
        this.username = username;
        this.uid = uid;
        this.thumbnail = thumbnail;
        this.sex = sex;
        this.desc = desc;
        this.game = game;
        this.orderCount = orderCount;
    }

    public MembersCelebrity() {
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
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * @return thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }

    /**
     * @return sex
     */
    public Short getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(Short sex) {
        this.sex = sex;
    }

    /**
     * @return desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * @return game
     */
    public Short getGame() {
        return game;
    }

    /**
     * @param game
     */
    public void setGame(Short game) {
        this.game = game;
    }

    /**
     * @return order_count
     */
    public Integer getOrderCount() {
        return orderCount;
    }

    /**
     * @param orderCount
     */
    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
}