package com.ptit.asset.domain;

import com.ptit.asset.domain.enumeration.MaterialStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "material")
public class Material extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank @Size(min = 5)
    @Column(name = "credential_code", unique = true, nullable = false, length = 100)
    private String credentialCode;

//    @JsonIgnore
//    @Column(name = "qr_code", unique = true, nullable = false)
//    private Blob qrCode;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MaterialStatus status;

    @NotNull
    @Column(name = "time_start_depreciation", nullable = false)
    private Instant timeStartDepreciation;

    @Column(name = "is_have_include")
    private Boolean haveInclude;

    @Size(min = 5)
    @Column(name = "parent_code", length = 100)
    private String parentCode;

    // Embedded space ===================================
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "additional_id", nullable = false)
    private Additional additional;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "current_place_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_place_id") // allow null because add without assign => assign a place after
    private Place currentPlace;

//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") // allow null because add without assign => assign for user after
    private User user;

    /***********************************************************/
    public Material() {
    }

    public Material(@NotNull @NotBlank @Size(min = 5) String credentialCode, MaterialStatus status, @NotNull Instant timeStartDepreciation, Boolean haveInclude, @Size(min = 5) String parentCode, Additional additional, Product product, Place currentPlace, User user) {
        this.credentialCode = credentialCode;
        this.status = status;
        this.timeStartDepreciation = timeStartDepreciation;
        this.haveInclude = haveInclude;
        this.parentCode = parentCode;
        this.additional = additional;
        this.product = product;
        this.currentPlace = currentPlace;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCredentialCode() {
        return credentialCode;
    }

    public void setCredentialCode(String credentialCode) {
        this.credentialCode = credentialCode;
    }

    public MaterialStatus getStatus() {
        return status;
    }

    public void setStatus(MaterialStatus status) {
        this.status = status;
    }

    public Instant getTimeStartDepreciation() {
        return timeStartDepreciation;
    }

    public void setTimeStartDepreciation(Instant timeStartDepreciation) {
        this.timeStartDepreciation = timeStartDepreciation;
    }

    public Boolean getHaveInclude() {
        return haveInclude;
    }

    public void setHaveInclude(Boolean haveInclude) {
        this.haveInclude = haveInclude;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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

    public Place getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(Place currentPlace) {
        this.currentPlace = currentPlace;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", credentialCode='" + credentialCode + '\'' +
                ", status=" + status +
                ", timeStartDepreciation=" + timeStartDepreciation +
                ", haveInclude=" + haveInclude +
                ", parentCode='" + parentCode + '\'' +
                ", additional=" + additional +
                ", product=" + product +
                ", currentPlace=" + currentPlace +
                ", user=" + user +
                '}';
    }
}
