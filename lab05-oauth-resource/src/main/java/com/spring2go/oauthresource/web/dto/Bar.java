package com.spring2go.oauthresource.web.dto;

public class Bar {
    private long id;
    private String name;

    public Bar() {
        super();
    }

    public Bar(long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
