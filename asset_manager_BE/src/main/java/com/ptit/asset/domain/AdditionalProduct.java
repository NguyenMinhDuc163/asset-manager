package com.ptit.asset.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "additional_product",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"additional_id","product_id"})
    }
)
public class AdditionalProduct extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "additional_id", nullable = false)
    private Additional additional;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

//    @NotNull @NotBlank
//    @Column(name = "quantity", nullable = false)
//    private int quantity;
    // todo: this property can infer by count from table asset or tool

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    /***********************************************************/

    public AdditionalProduct() {
    }

    public AdditionalProduct(Additional additional, Product product, @NotNull Double price) {
        this.additional = additional;
        this.product = product;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Additional getAdditional() {
        return additional;
    }

    public void setAdditional(Additional additional) {
        this.additional = additional;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AdditionalProduct{" +
                "id=" + id +
                ", additional=" + additional +
                ", product=" + product +
                ", price=" + price +
                '}';
    }
}
