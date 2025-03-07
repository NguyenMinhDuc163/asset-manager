package com.ptit.asset.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "liquidate_material",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"liquidate_id","material_id"})
    }
)
public class LiquidateMaterial extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties("user")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "liquidate_id", nullable = false)
    private Liquidate liquidate;

    @JsonIgnoreProperties({"product","additional","currentPlace","user"})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_id", unique = true, nullable = false)
    private Material material; // unique make sure one material are liquidated once

    /***********************************************************/

    public LiquidateMaterial() {
    }

    public LiquidateMaterial(Liquidate liquidate, Material material) {
        this.liquidate = liquidate;
        this.material = material;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Liquidate getLiquidate() {
        return liquidate;
    }

    public void setLiquidate(Liquidate liquidate) {
        this.liquidate = liquidate;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "LiquidateMaterial{" +
                "id=" + id +
                ", liquidate=" + liquidate +
                ", material=" + material +
                '}';
    }
}
