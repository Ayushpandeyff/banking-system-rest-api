package com.banking.banking_system.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message="firstname is required")
    private String firstName;
    @Column(nullable = false)
    @NotBlank(message="lastname is required")
    private String lastName;

    @Column(unique = true,nullable = false)
    @Email(message = "provide a valid email")
    @NotBlank(message = "emial is required")
    private String email;
    @Column(nullable = false)
    @NotBlank(message="password is required")
    private String password;

@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role=Role.CUSTOMER;
private String phonenumber;
@Column(nullable = false)
    private Boolean isActive=true;
@CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String phonenumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
