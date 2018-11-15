package com.adwork.microservices.users.entity;

import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_account",
       indexes = {@Index(name="email_index", columnList="email", unique=true)})
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="email")
    @NotEmpty(message="Email is mandatory for User")
    private String email;

    @Column(name="name")
    private String name;

    @Column(name="role")
    private UserRole role;

    @Column(name="password")
    private String password;

    @Column(name="locked")
    private boolean locked;

    @Column(name="registered_date")
    private Date dateRegistered;

    @Column(name="loged_date")
    private Date lastLoggedDate;

    @Column(name="updated_date")
    private Date updatedDate;

    @Column(name="rating_value")
    private Double ratingValue;

    @Column(name="rating_count")
    private Long ratingCount;

    public UserAccount() {}

    public UserAccount(String email, String password, UserRole role) {
        this.email = email;
        this.name = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public List<UserRole> getRoles() {
        return Arrays.asList(role);
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Date getLastLoggedDate() {
        return lastLoggedDate;
    }

    public void setLastLoggedDate(Date lastLoggedDate) {
        this.lastLoggedDate = lastLoggedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getName() + ": id=" + id + ", email="+ email + "]";
    }
}
