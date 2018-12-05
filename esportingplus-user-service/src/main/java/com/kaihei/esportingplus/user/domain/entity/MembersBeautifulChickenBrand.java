package com.kaihei.esportingplus.user.domain.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "members_beautifulchickenbrand")
public class MembersBeautifulChickenBrand {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 鸡牌号
     */
    @Column(name = "chicken_id")
    private String chickenId;

    /**
     * 级别，1-8
     */
    private Integer level;

    /**
     * 状态：0，启用；1，禁用
     */
    private Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChickenId() {
        return chickenId;
    }

    public void setChickenId(String chickenId) {
        this.chickenId = chickenId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}