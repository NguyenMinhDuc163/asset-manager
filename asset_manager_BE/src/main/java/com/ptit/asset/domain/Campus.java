package com.ptit.asset.domain;

import com.ptit.asset.domain.enumeration.CampusType;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "campus")
public class Campus extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank @Size(min = 2, max = 15)
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "ntext")
    private String description;

    @NotNull @NotBlank @Size(min = 10, max = 15)
    @Column(name = "contact_phone", unique = true)
    private String contactPhone;

    @Email @NotNull @NotBlank @Size(min = 10, max = 50)
    @Column(name = "contact_email", unique = true)
    private String contactEmail;

    @Column(name = "campus_type")
    @Enumerated(EnumType.STRING)
    private CampusType campusType;

    @Column(name = "location", columnDefinition = "ntext")
    private String location;

    @Column(name = "map_url", length = 5000)
    private String mapUrl;

    /***********************************************************/

    public Campus() {
    }

    public Campus(@NotNull @NotBlank @Size(min = 2, max = 15) String name, String description, @NotNull @NotBlank @Size(min = 10, max = 15) String contactPhone, @Email @NotNull @NotBlank @Size(min = 10, max = 50) String contactEmail, CampusType campusType, String location, String mapUrl) {
        this.name = name;
        this.description = description;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.campusType = campusType;
        this.location = location;
        this.mapUrl = mapUrl;
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

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public CampusType getCampusType() {
        return campusType;
    }

    public void setCampusType(CampusType campusType) {
        this.campusType = campusType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    @Override
    public String toString() {
        return "Campus{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", campusType=" + campusType +
                ", location='" + location + '\'' +
                ", mapUrl='" + mapUrl + '\'' +
                '}';
    }
}
