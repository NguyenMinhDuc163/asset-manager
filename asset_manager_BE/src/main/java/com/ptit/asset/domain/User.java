package com.ptit.asset.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "users")
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank @Size(min = 5, max = 200)
    @Column(name = "full_name", nullable = false, columnDefinition = "ntext")
    private String fullName;

    @NotNull @NotBlank @Size(min = 10, max = 15)
    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Email @NotNull @NotBlank @Size(min = 10, max = 50)
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotNull @NotBlank @Size(min = 5, max = 50)
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @NotNull @NotBlank @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.EAGER)// when set optional = false : a non-null relationship must always exist
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /***********************************************************/

    public User() {
    }

    public User(@NotNull @NotBlank @Size(min = 5, max = 200) String fullName, @NotNull @NotBlank @Size(min = 10, max = 15) String phone, @Email @NotNull @NotBlank @Size(min = 10, max = 50) String email, @NotNull @NotBlank @Size(min = 5, max = 50) String username, @NotNull @NotBlank String password, @NotNull Boolean active, Department department, Set<Role> roles) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.active = active;
        this.department = department;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", department=" + department +
                ", roles=" + roles +
                '}';
    }
}
