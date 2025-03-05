package com.emura_group.schema_based_tenant_management.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepresentantDetailsDto {
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
    private Boolean isNineaVerified;
}

