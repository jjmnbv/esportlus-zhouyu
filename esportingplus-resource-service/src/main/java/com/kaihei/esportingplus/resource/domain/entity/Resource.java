package com.kaihei.esportingplus.resource.domain.entity;

public class Resource {
    private Long id;

    private String name;

    public Resource(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Resource() {
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