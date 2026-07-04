package com.app.sakila.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "manager_staff_id", nullable = false)
    private Integer managerStaffId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @PrePersist @PreUpdate
    protected void onUpdate() { lastUpdate = LocalDateTime.now(); }

    public Store() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getManagerStaffId() { return managerStaffId; }
    public void setManagerStaffId(Integer managerStaffId) { this.managerStaffId = managerStaffId; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
}
