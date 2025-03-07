package com.ptit.asset.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/*
*** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "additional")
public class Additional extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "time", unique = true, nullable = false)
    private Instant time;

    @NotNull
    @Column(name = "in_process", nullable = false)
    private Boolean inProcess;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    /***********************************************************/
    public Additional() {
    }

    public Additional(@NotNull Instant time, @NotNull Boolean inProcess, User user, Organization organization) {
        this.time = time;
        this.inProcess = inProcess;
        this.user = user;
        this.organization = organization;
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

    public Boolean getInProcess() {
        return inProcess;
    }

    public void setInProcess(Boolean inProcess) {
        this.inProcess = inProcess;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "Additional{" +
                "id=" + id +
                ", time=" + time +
                ", inProcess=" + inProcess +
                ", user=" + user +
                ", organization=" + organization +
                '}';
    }
}
