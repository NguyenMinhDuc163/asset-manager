package com.ptit.asset.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "liquidate")
public class Liquidate extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "time", unique = true, nullable = false)
    private Instant time;

    @JsonIgnoreProperties({"phone","email","username","password","roles"})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "done", nullable = false)
    private Boolean done;

    /***********************************************************/

    public Liquidate() {
    }

    public Liquidate(@NotNull Instant time, User user, Boolean done) {
        this.time = time;
        this.user = user;
        this.done = done;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Liquidate{" +
                "id=" + id +
                ", time=" + time +
                ", user=" + user +
                ", done=" + done +
                '}';
    }
}
