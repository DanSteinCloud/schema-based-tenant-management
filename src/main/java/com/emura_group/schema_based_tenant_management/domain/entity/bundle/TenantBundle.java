package com.emura_group.schema_based_tenant_management.domain.entity.bundle;

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
@Table(name = "tenant-bundle" , schema = "common")
public class TenantBundle {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "bundle_id")
    private String bundle_id;

    // @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE}, mappedBy = "representantBundle")
//    @JoinTable(name = "representant_bundle", joinColumns = @JoinColumn(name = "bundle_id"),
//            inverseJoinColumns = @JoinColumn(name = "representant_id"))
//    @JsonIgnore
//    private Set<Representant> representants = new HashSet<>();
}
