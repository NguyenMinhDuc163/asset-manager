package com.ptit.asset.domain;

import com.ptit.asset.domain.enumeration.ProductType;
import com.ptit.asset.domain.enumeration.TimeAllocationType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "product")
public class Product extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank @Size(min = 2, max = 200)
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "ntext")
    private String description;

    //quantity can infer by child table

    @Column(name = "origin", columnDefinition = "ntext")
    private String origin;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "time_allocation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeAllocationType timeAllocationType; // consider should be used?

    @Column(name = "allocation_duration", nullable = false)
    private int allocationDuration; // consider should be used?

    @Column(name = "depreciation_rate", nullable = false)
    private Double depreciationRate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "calculation_unit", nullable = false)
    private CalculationUnit calculationUnit;

    /***********************************************************/

    public Product() {
    }

    public Product(@NotNull @NotBlank @Size(min = 2, max = 200) String name, String description, String origin, ProductType type, TimeAllocationType timeAllocationType, int allocationDuration, Double depreciationRate, Category category, CalculationUnit calculationUnit) {
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.type = type;
        this.timeAllocationType = timeAllocationType;
        this.allocationDuration = allocationDuration;
        this.depreciationRate = depreciationRate;
        this.category = category;
        this.calculationUnit = calculationUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public TimeAllocationType getTimeAllocationType() {
        return timeAllocationType;
    }

    public void setTimeAllocationType(TimeAllocationType timeAllocationType) {
        this.timeAllocationType = timeAllocationType;
    }

    public int getAllocationDuration() {
        return allocationDuration;
    }

    public void setAllocationDuration(int allocationDuration) {
        this.allocationDuration = allocationDuration;
    }

    public Double getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(Double depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CalculationUnit getCalculationUnit() {
        return calculationUnit;
    }

    public void setCalculationUnit(CalculationUnit calculationUnit) {
        this.calculationUnit = calculationUnit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", origin='" + origin + '\'' +
                ", type=" + type +
                ", timeAllocationType=" + timeAllocationType +
                ", allocationDuration=" + allocationDuration +
                ", depreciationRate=" + depreciationRate +
                ", category=" + category +
                ", calculationUnit=" + calculationUnit +
                '}';
    }
}
