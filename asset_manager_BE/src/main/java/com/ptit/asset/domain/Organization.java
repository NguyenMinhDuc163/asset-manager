package com.ptit.asset.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Entity
@Table(name = "organization")
public class Organization extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank @Size(min = 2, max = 15)
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "contact", columnDefinition = "ntext")
    private String contact;

    /***********************************************************/

    public Organization() {
    }

    public Organization(@NotNull @NotBlank @Size(min = 2, max = 15) String name, String contact) {
        this.name = name;
        this.contact = contact;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
