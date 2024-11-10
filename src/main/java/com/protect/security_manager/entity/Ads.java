package com.protect.security_manager.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ads")
public class Ads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column(name = "is_marked_for_publish", nullable = false)
    private boolean isMarkedForPublish;

    // Constructors
    public Ads() {}

    public Ads(LocalDate startDate, LocalDate endDate, boolean isMarkedForPublish) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isMarkedForPublish = isMarkedForPublish;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public boolean isMarkedForPublish() {
        return isMarkedForPublish;
    }

    public void setMarkedForPublish(boolean markedForPublish) {
        isMarkedForPublish = markedForPublish;
    }
}
