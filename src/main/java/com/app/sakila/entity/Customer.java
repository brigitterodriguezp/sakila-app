package com.app.sakila.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(length = 50)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(nullable = false)
    private Boolean activebool = true;

    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    private Integer active;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDate.now();
        lastUpdate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() { lastUpdate = LocalDateTime.now(); }

    public Customer() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public Boolean getActivebool() { return activebool; }
    public void setActivebool(Boolean activebool) { this.activebool = activebool; }
    public LocalDate getCreateDate() { return createDate; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public Integer getActive() { return active; }
    public void setActive(Integer active) { this.active = active; }
}
