package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;

public class ResourceVO implements Serializable {

    private static final long serialVersionUID = 1449700714607030362L;

    private Long id;

    private String name;

    public ResourceVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ResourceVO() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}