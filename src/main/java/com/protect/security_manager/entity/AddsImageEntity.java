package com.protect.security_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "adds_image")
public class AddsImageEntity extends BaseImageEntity<Integer> {

    public AddsImageEntity() {}

    public AddsImageEntity(String url, String fileName, Integer addsId) {
        super(url, fileName, addsId);
    }

    public Integer getAddsId() {
        return getOwnerId();
    }

    public void setAddsId(Integer addsId) {
        setOwnerId(addsId);
    }
}