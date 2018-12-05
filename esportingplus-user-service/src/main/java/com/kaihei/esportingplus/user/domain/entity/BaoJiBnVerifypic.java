package com.kaihei.esportingplus.user.domain.entity;

import javax.persistence.*;

@Table(name = "baoji_bnverifypic")
public class BaoJiBnVerifypic {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String uid;

    private String picture1;

    private String picture2;

    private String picture3;

    @Column(name = "live_video")
    private String liveVideo;

    private String video;

    public BaoJiBnVerifypic(Integer id, String uid, String picture1, String picture2, String picture3, String liveVideo, String video) {
        this.id = id;
        this.uid = uid;
        this.picture1 = picture1;
        this.picture2 = picture2;
        this.picture3 = picture3;
        this.liveVideo = liveVideo;
        this.video = video;
    }

    public BaoJiBnVerifypic() {
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
     * @return picture1
     */
    public String getPicture1() {
        return picture1;
    }

    /**
     * @param picture1
     */
    public void setPicture1(String picture1) {
        this.picture1 = picture1 == null ? null : picture1.trim();
    }

    /**
     * @return picture2
     */
    public String getPicture2() {
        return picture2;
    }

    /**
     * @param picture2
     */
    public void setPicture2(String picture2) {
        this.picture2 = picture2 == null ? null : picture2.trim();
    }

    /**
     * @return picture3
     */
    public String getPicture3() {
        return picture3;
    }

    /**
     * @param picture3
     */
    public void setPicture3(String picture3) {
        this.picture3 = picture3 == null ? null : picture3.trim();
    }

    /**
     * @return live_video
     */
    public String getLiveVideo() {
        return liveVideo;
    }

    /**
     * @param liveVideo
     */
    public void setLiveVideo(String liveVideo) {
        this.liveVideo = liveVideo == null ? null : liveVideo.trim();
    }

    /**
     * @return video
     */
    public String getVideo() {
        return video;
    }

    /**
     * @param video
     */
    public void setVideo(String video) {
        this.video = video == null ? null : video.trim();
    }
}