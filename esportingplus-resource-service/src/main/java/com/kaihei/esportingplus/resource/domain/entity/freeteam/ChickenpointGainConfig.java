package com.kaihei.esportingplus.resource.domain.entity.freeteam;

import com.kaihei.esportingplus.common.data.Castable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;

@Builder
@Table(name = "chickenpoint_gain_config")
public class ChickenpointGainConfig implements Castable {

    /**
     * 主键 id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 免费车队类型Id
     */
    @Column(name = "free_team_type_id")
    private Integer freeTeamTypeId;

    /**
     * 是否支持获取鸡分 1支持 0不支持
     */
    @Column(name = "suport_gain_chickenpoint")
    private Integer suportGainChickenpoint;

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

    public ChickenpointGainConfig(Integer id, Integer freeTeamTypeId, Integer suportGainChickenpoint, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.freeTeamTypeId = freeTeamTypeId;
        this.suportGainChickenpoint = suportGainChickenpoint;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public ChickenpointGainConfig() {
        super();
    }

    /**
     * 获取主键 id
     *
     * @return id - 主键 id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键 id
     *
     * @param id 主键 id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取免费车队类型Id
     *
     * @return free_team_type_id - 免费车队类型Id
     */
    public Integer getFreeTeamTypeId() {
        return freeTeamTypeId;
    }

    /**
     * 设置免费车队类型Id
     *
     * @param freeTeamTypeId 免费车队类型Id
     */
    public void setFreeTeamTypeId(Integer freeTeamTypeId) {
        this.freeTeamTypeId = freeTeamTypeId;
    }

    /**
     * 获取是否支持获取鸡分 1支持 0不支持
     *
     * @return suport_gain_chickenpoint - 是否支持获取鸡分 1支持 0不支持
     */
    public Integer getSuportGainChickenpoint() {
        return suportGainChickenpoint;
    }

    /**
     * 设置是否支持获取鸡分 1支持 0不支持
     *
     * @param suportGainChickenpoint 是否支持获取鸡分 1支持 0不支持
     */
    public void setSuportGainChickenpoint(Integer suportGainChickenpoint) {
        this.suportGainChickenpoint = suportGainChickenpoint;
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
}