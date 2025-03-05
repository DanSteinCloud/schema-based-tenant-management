package com.emura_group.schema_based_tenant_management.repository.batch;

import com.emura_group.schema_based_tenant_management.domain.model.Representant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RepresentantRepository extends JpaRepository<Representant, String> {

    @Query("SELECT re FROM Representant re ")
    Page<Representant> findAllWithPagination(LocalDateTime yesterdayStart, LocalDateTime yesterdayEnd, Pageable pageable);

    @Query("SELECT COUNT(re) FROM Representant re ")
    long countAllRecords(LocalDateTime yesterdayStart, LocalDateTime yesterdayEnd);
}
