package com.emura_group.schema_based_tenant_management.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "representant" , schema = "common")
public class Representant {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "country")
    private String country;

    @Column(columnDefinition = "boolean default false")
    private Boolean isCompany;

    @Column(name = "companyName", nullable = false)
    @NotBlank(message="Company Name is required")
    private String companyName;

    @Column(name = "companyEmail")
    private String companyEmail;

    @Column(name = "ninea")
    private String ninea;

    @Column(name = "firstName", nullable = false)
    @NotBlank(message="firstName is required")
    private String firstName;

    @Column(name = "lastName", nullable = false)
    @NotBlank(message="lastName is required")
    private String lastName;

    @Column(name = "responsability_in_company")
    private String responsabilityInCompany;

    //@Basic
    @Column(name = "registrationDate", updatable = false, nullable = false)
    private LocalDateTime registrationDate;

    @CreationTimestamp
    private LocalDateTime activatedAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @Column(name = "telephone")
    private String telephone;

    @Column(columnDefinition = "boolean default false")
    private Boolean isActivated;

    @Column(columnDefinition = "boolean default false")
    private Boolean isEmailVerified;

    @Column(columnDefinition = "boolean default false")
    private Boolean isNineaVerified;

    @Column(columnDefinition = "boolean default false")
    private Boolean hasAValidContrat;


//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "representant_bundle",
//            joinColumns = { @JoinColumn(name = "representant_id") },
//            inverseJoinColumns = { @JoinColumn(name = "bundle_id") })
//    private List<Bundle> representantBundle;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "representant_bundle", schema = "common", joinColumns = @JoinColumn(name = "representant_id"),
            inverseJoinColumns = @JoinColumn(name = "bundle_bundle"))
    private Set<Bundle> representantBundle = new HashSet<>();

    public Representant(String id, String country, boolean isCompany, String companyname, String companyEmail, String firstname, String lastname, String responsabilityInCompany, LocalDateTime registrationdate, LocalDateTime localDateTime, LocalDateTime localDateTime1, String telephone, boolean isActivated, boolean isEmailVerified, boolean isNineaVerified, boolean hasavalidContrat) {
    }
}
