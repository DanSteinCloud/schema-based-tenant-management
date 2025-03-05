package com.emura_group.schema_based_tenant_management.domain.entity.nontenant;

import com.emura_group.schema_based_tenant_management.domain.entity.bundle.NonTenantBundle;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "non-tenant", schema = "common")
public class NonTenant {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "representant_id", nullable = false)
    private String representantId;

    @Column(name = "companyname", nullable = false)
    @NotBlank(message="Company Name is required")
    private String companyName;

    @Column(name = "companyemail")
    private String email;

    @Column(name = "firstname", nullable = false)
    @NotBlank(message="firstName is required")
    private String firstName;

    @Column(name = "lastname", nullable = false)
    @NotBlank(message="lastName is required")
    private String lastName;

    @Column(name = "responsability_in_company")
    private String responsabilityInCompany;

    //@Basic
    @Column(name = "registrationdate", updatable = false, nullable = false)
    private LocalDateTime registrationDate;

    @CreationTimestamp
    @Column(name = "activatedat")
    private LocalDateTime activatedAt;

    @UpdateTimestamp
    @Column(name = "modifiedat")
    private LocalDateTime modifiedAt;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "isactivated", columnDefinition = "boolean default false")
    private Boolean isActivated;

    @Column(columnDefinition = "boolean default false", name = "isemailverified")
    private Boolean isEmailVerified;

    @Column(columnDefinition = "boolean default false", name = "hasavalidcontrat")
    private Boolean hasAValidContrat;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
//    @JoinTable(name = "non_tenant_bundle", schema = "assiganto", joinColumns = @JoinColumn(name = "nonTenantBundle_id"),
//            inverseJoinColumns = @JoinColumn(name = "nonTenantBundle_id"))
//    private Set<NonTenantBundle> nontenantBundle = new HashSet<>();
}
