package com.emura_group.schema_based_tenant_management.domain.entity.bundle;

import com.emura_group.schema_based_tenant_management.domain.entity.nontenant.NonTenant;
import com.emura_group.schema_based_tenant_management.domain.model.Representant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@Entity
@Table(name = "non_tenant_bundle" , schema = "common")
public class NonTenantBundle {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "bundle_id")
    private String bundle_id;

//    @ManyToOne
//    @JoinColumn(name = "nonTenant_id")
//    private NonTenant nonTenant;
}
