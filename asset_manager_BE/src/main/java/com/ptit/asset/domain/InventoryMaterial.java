package com.ptit.asset.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "inventory_material",
//    uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"inventory_id","material_id","user_id","place_id"}),
//        @UniqueConstraint(columnNames = {"inventory_id","material_id"})
//    }
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"inventory_id","material_id","user_id"}),
            @UniqueConstraint(columnNames = {"inventory_id","material_id"})
    }
)
public class InventoryMaterial extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @JsonIgnoreProperties({"product","additional","currentPlace","user"})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @JsonIgnoreProperties({"department","roles"})
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @JsonIgnoreProperties({"typePlace","campus","department"})
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "place_id", nullable = false)
//    private Place place;

    /***********************************************************/

    public InventoryMaterial() {
    }

    public InventoryMaterial(Inventory inventory, Material material, User user) {
        this.inventory = inventory;
        this.material = material;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
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
        return "InventoryMaterial{" +
                "id=" + id +
                ", inventory=" + inventory +
                ", material=" + material +
                ", user=" + user +
                '}';
    }
}
