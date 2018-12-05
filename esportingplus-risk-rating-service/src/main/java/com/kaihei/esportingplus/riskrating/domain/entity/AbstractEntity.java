package com.kaihei.esportingplus.riskrating.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaihei.esportingplus.common.data.Castable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Definition Base abstract Entity class
 * 
 * @author haycco
 *
 */
@MappedSuperclass
public abstract class AbstractEntity implements Castable, Comparable<AbstractEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * entity id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    protected Long id;

    /**
     * 
     * Update date
     */
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;

    /**
     * 
     * Creation date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    protected Date createDate;
    
    public Long getId(){
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获取最近更新时间
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * 设置最近更新时间
     * 
     * @param lastModifiedDate
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * 获取创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间戳
     * 
     * @param createDate
     */
    private void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @PrePersist
    void onCreate() {
        this.setCreateDate(new Timestamp((new Date()).getTime()));
    }

    @PreUpdate
    void onPersist() {
        this.setLastModifiedDate(new Timestamp((new Date()).getTime()));
    }
    
    /**
     * Returns whether this instance represents a new transient instance.
     * 
     * @return <tt>true</tt> if <code>id</code> is <tt>null</tt>
     */
    @JsonIgnore
    public boolean isNew() {
        return (this.getId() == 0);
    }

    @Override
    public int compareTo(AbstractEntity entity) {
        return (int) (this.getId() - entity.getId());
    }
}
