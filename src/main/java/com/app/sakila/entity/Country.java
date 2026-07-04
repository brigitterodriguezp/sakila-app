package com.app.sakila.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String country;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @PrePersist @PreUpdate
    protected void onUpdate() { lastUpdate = LocalDateTime.now(); }

    public Country() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
}
