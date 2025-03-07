package com.ptit.asset.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "inventory")
public class Inventory extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "time", unique = true, nullable = false)
    private Instant time;

    @NotNull
    @Column(name = "in_check", nullable = false)
    private Boolean inCheck;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Instant endTime;


    /***********************************************************/

    public Inventory() {
    }

    public Inventory(@NotNull Instant time, @NotNull Boolean inCheck, @NotNull Instant startTime, @NotNull Instant endTime) {
        this.time = time;
        this.inCheck = inCheck;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Boolean getInCheck() {
        return inCheck;
    }

    public void setInCheck(Boolean inCheck) {
        this.inCheck = inCheck;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", time=" + time +
                ", inCheck=" + inCheck +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
