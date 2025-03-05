package com.emura_group.schema_based_tenant_management.payload;

import com.emura_group.schema_based_tenant_management.domain.model.Bundle;
import com.emura_group.schema_based_tenant_management.domain.model.CompanyDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto {
    private String firstName;
    private String lastName;
    private String companyEmail;
    private Boolean isCompany;
    private String companyName;
    private String  ninea;
    private String responsabilityInCompany;
    private LocalDateTime registrationDate;
    private LocalDateTime activatedAt;
    private LocalDateTime modifiedAt;
    private String telephone;
    private Boolean isActivated;
    private Boolean isEmailVerified;
    private Set<Bundle> representantBundle;
    private Boolean isNineaVerified;
    private Boolean hasAValidContrat;
    private Set<CompanyDocument> companyDocs;
}
