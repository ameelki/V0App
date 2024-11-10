package com.protect.security_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "person_image")
public class PersonImageEntity extends BaseImageEntity<String> {

    public PersonImageEntity() {}

    public PersonImageEntity(String url, String fileName, String personId) {
        super(url, fileName, personId);
    }

    public String getPersonId() {
        return getOwnerId();
    }

    public void setPersonId(String personId) {
        setOwnerId(personId);
    }
}
