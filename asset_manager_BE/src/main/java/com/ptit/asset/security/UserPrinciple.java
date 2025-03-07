package com.ptit.asset.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ptit.asset.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
public class UserPrinciple implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String fullName;
    private String phone;
    private String email;

    private String username;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;


    /*----------------------------------------*/
    public UserPrinciple(Long id,
                         String fullName,
                         String phone,
                         String email,
                         String username,
                         String password,
                         Collection<? extends GrantedAuthority> authorities){
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrinciple build(User user){
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());

        return new UserPrinciple(
                user.getId(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
    /*----------------------------------------*/

    /* ------- addition ------- */
    public Long getId(){
        return id;
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
    /* ------- addition ------- */


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrinciple userPrinciple = (UserPrinciple) o;
        return Objects.equals(id, userPrinciple.id);
    }

}
