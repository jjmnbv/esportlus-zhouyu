package com.kaihei.esportingplus.payment.domain.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Definition Base abstract Entity class
 *
 * @author haycco
 */
@MappedSuperclass
public abstract class AbstractBillEntity implements Serializable, Comparable<AbstractBillEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * entity id
     */
    @Id
    @Column(name = "ID")
    protected Long id;

    /**
     * Update date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "datetime COMMENT '最近更新时间'")
    protected Date lastModifiedDate;

    /**
     * Creation date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, columnDefinition = "datetime COMMENT '创建时间'")
    protected Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @PrePersist
    void onCreate() {
        if (getCreateDate() == null) {
            this.setCreateDate(new Timestamp((new Date()).getTime()));
        }
    }

    @PreUpdate
    void onPersist() {
        if (getLastModifiedDate() == null) {
            this.setLastModifiedDate(new Timestamp((new Date()).getTime()));
        }
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
    public int compareTo(AbstractBillEntity entity) {
        return (int) (this.getId() - entity.getId());
    }
}
