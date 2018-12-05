package com.kaihei.esportingplus.user.domain.entity;

import javax.persistence.*;

@Table(name = "baoji_baojitag")
public class BaojiBaoJiTag {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "tag_type")
    private Short tagType;

    private Short value;

    private String description;

    private String extras;

    private String subtitle;

    public BaojiBaoJiTag(Integer id, Short tagType, Short value, String description, String extras, String subtitle) {
        this.id = id;
        this.tagType = tagType;
        this.value = value;
        this.description = description;
        this.extras = extras;
        this.subtitle = subtitle;
    }

    public BaojiBaoJiTag() {
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
     * @return tag_type
     */
    public Short getTagType() {
        return tagType;
    }

    /**
     * @param tagType
     */
    public void setTagType(Short tagType) {
        this.tagType = tagType;
    }

    /**
     * @return value
     */
    public Short getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(Short value) {
        this.value = value;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * @return extras
     */
    public String getExtras() {
        return extras;
    }

    /**
     * @param extras
     */
    public void setExtras(String extras) {
        this.extras = extras == null ? null : extras.trim();
    }

    /**
     * @return subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * @param subtitle
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? null : subtitle.trim();
    }
}