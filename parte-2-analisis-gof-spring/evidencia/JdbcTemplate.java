package org.springframework.jdbc.core;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Fragmento de evidencia de org.springframework.jdbc.core.JdbcTemplate
 * Modulo: spring-jdbc
 * Patron: Template Method (Comportamiento)
 * SOLID asociado: SRP (Responsabilidad Unica)
 */
public class JdbcTemplate {

    public <T> T execute(StatementCallback<T> action) {
        Connection con = DataSourceUtils.getConnection(getDataSource());
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            // Paso variable delegado en el callback dentro del esqueleto inmutable
            return action.doInStatement(stmt);
        } catch (Exception ex) {
            throw translateException(ex);
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con);
        }
    }
}
