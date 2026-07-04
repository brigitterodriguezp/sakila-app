package com.app.sakila.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @PrePersist @PreUpdate
    protected void onUpdate() { lastUpdate = LocalDateTime.now(); }

    public Language() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
}
