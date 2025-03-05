package com.emura_group.schema_based_tenant_management.mapper;

import com.emura_group.schema_based_tenant_management.domain.model.Representant;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RepresentantMapper implements RowMapper<Representant> {

    @Override
    public Representant mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Representant(
                rs.getString("id"),
                rs.getString("country"),
                rs.getBoolean("isCompany"),
                rs.getString("companyname"),
                rs.getString("company_email"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("responsability_in_company"),
                rs.getTimestamp("registrationdate").toLocalDateTime(),
                rs.getTimestamp("activated_at") != null ? rs.getTimestamp("activated_at").toLocalDateTime() : null,
                rs.getTimestamp("modified_at") != null ? rs.getTimestamp("modified_at").toLocalDateTime() : null,
                rs.getString("telephone"),
                rs.getBoolean("is_activated"),
                rs.getBoolean("is_email_verified"),
                rs.getBoolean("is_ninea_verified"),
                rs.getBoolean("hasavalid_contrat")
        );
    }
}