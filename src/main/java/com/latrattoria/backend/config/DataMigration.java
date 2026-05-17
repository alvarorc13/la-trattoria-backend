package com.latrattoria.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataMigration implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DataMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            // Alter column to allow new enum values
            jdbcTemplate.execute(
                "ALTER TABLE Pedido MODIFY COLUMN estado ENUM('nuevo','en_camino') NOT NULL DEFAULT 'nuevo'"
            );
        } catch (Exception e) {
            // If ALTER fails (e.g. data incompatible), update data first with VARCHAR
            try {
                jdbcTemplate.execute("ALTER TABLE Pedido MODIFY COLUMN estado VARCHAR(50) NOT NULL DEFAULT 'nuevo'");
                jdbcTemplate.update("UPDATE Pedido SET estado = 'en_camino' WHERE estado NOT IN ('nuevo', 'en_camino')");
                jdbcTemplate.execute("ALTER TABLE Pedido MODIFY COLUMN estado ENUM('nuevo','en_camino') NOT NULL DEFAULT 'nuevo'");
            } catch (Exception ex) {
                System.out.println("DataMigration: Could not migrate estado column: " + ex.getMessage());
            }
        }
        int updated = jdbcTemplate.update(
            "UPDATE Pedido SET estado = 'en_camino' WHERE estado NOT IN ('nuevo', 'en_camino')"
        );
        if (updated > 0) {
            System.out.println("DataMigration: Updated " + updated + " pedidos to 'en_camino'");
        }
    }
}
