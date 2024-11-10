package com.protect.security_manager.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseImageEntity<ID> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String fileName;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @Column(nullable = false)
    private ID ownerId; // Champ générique pour ID

    public BaseImageEntity() {}

    public BaseImageEntity(String url, String fileName, ID ownerId) {
        this.url = url;
        this.fileName = fileName;
        this.uploadDate = LocalDateTime.now();
        this.ownerId = ownerId;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public ID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ID ownerId) {
        this.ownerId = ownerId;
    }
}
