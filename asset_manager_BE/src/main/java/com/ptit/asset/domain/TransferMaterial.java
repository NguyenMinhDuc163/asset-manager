package com.ptit.asset.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "transfer_material")
public class TransferMaterial extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "time", nullable = false)
    private Instant time;

    @Size(min = 2)
    @Column(name = "reason", columnDefinition = "ntext")
    private String reason;

    @JsonIgnoreProperties({"typePlace","campus","department"})
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "place_from_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_from_id") // allow null to support for the first setup place for material
    private Place placeFrom;

    @JsonIgnoreProperties({"typePlace","campus","department"})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "place_target_id", nullable = false)
    private Place placeTarget;

    @JsonIgnoreProperties({"product","additional","currentPlace","user"})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /***********************************************************/

    public TransferMaterial() {
    }

    public TransferMaterial(@NotNull Instant time, @Size(min = 2) String reason, Place placeFrom, Place placeTarget, Material material, User user) {
        this.time = time;
        this.reason = reason;
        this.placeFrom = placeFrom;
        this.placeTarget = placeTarget;
        this.material = material;
        this.user = user;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Place getPlaceFrom() {
        return placeFrom;
    }

    public void setPlaceFrom(Place placeFrom) {
        this.placeFrom = placeFrom;
    }

    public Place getPlaceTarget() {
        return placeTarget;
    }

    public void setPlaceTarget(Place placeTarget) {
        this.placeTarget = placeTarget;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TransferMaterial{" +
                "id=" + id +
                ", time=" + time +
                ", reason='" + reason + '\'' +
                ", placeFrom=" + placeFrom +
                ", placeTarget=" + placeTarget +
                ", material=" + material +
                ", user=" + user +
                '}';
    }
}
