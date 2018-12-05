package com.kaihei.esportingplus.user.api.vo;

/**
 * @author liuyang
 * @Description
 * @Date 2018/12/1 17:31
 **/
public class UserRelationVo {

    private Integer id;
    private String uid;
    private String userName;
    /**
     * 性别：男-1，女-2，未知-3
     */
    private Integer sex;

    public UserRelationVo() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
