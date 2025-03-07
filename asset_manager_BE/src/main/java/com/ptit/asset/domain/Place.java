package com.ptit.asset.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "place")
public class Place extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank @Size(min = 3, max = 20)
    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name_specification", columnDefinition = "ntext")
    private String nameSpecification;

    @Column(name = "description", columnDefinition = "ntext")
    private String description;

    @Column(name = "floor")
    private int floor;

    @Column(name = "direction", columnDefinition = "ntext")
    private String direction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_place_id", nullable = false)
    private TypePlace typePlace;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    /***********************************************************/

    public Place() {
    }

    public Place(@NotNull @NotBlank @Size(min = 3, max = 20) String code, String nameSpecification, String description, int floor, String direction, TypePlace typePlace, Campus campus, Department department) {
        this.code = code;
        this.nameSpecification = nameSpecification;
        this.description = description;
        this.floor = floor;
        this.direction = direction;
        this.typePlace = typePlace;
        this.campus = campus;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameSpecification() {
        return nameSpecification;
    }

    public void setNameSpecification(String nameSpecification) {
        this.nameSpecification = nameSpecification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public TypePlace getTypePlace() {
        return typePlace;
    }

    public void setTypePlace(TypePlace typePlace) {
        this.typePlace = typePlace;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", nameSpecification='" + nameSpecification + '\'' +
                ", description='" + description + '\'' +
                ", floor=" + floor +
                ", direction='" + direction + '\'' +
                ", typePlace=" + typePlace +
                ", campus=" + campus +
                ", department=" + department +
                '}';
    }
}
