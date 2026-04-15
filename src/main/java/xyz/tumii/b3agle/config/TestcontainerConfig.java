package xyz.tumii.b3agle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Configures a PostgreSQL container for development and testing.
 * This provides a real PostgreSQL database instead of H2.
 */
@Configuration
@Profile({"dev-postgres", "test-postgres"})
public class TestcontainerConfig {

    private static PostgreSQLContainer<?> postgresContainer;

    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        // Use PostgreSQL 16 Alpine image
        postgresContainer = new PostgreSQLContainer<>(
                DockerImageName.parse("postgres:16-alpine"))
                .withDatabaseName("b3agle")
                .withUsername("dev")
                .withPassword("dev")
                .withExposedPorts(5432)
                .withReuse(true); // Reuse container for faster startup

        postgresContainer.start();
        return postgresContainer;
    }

    /**
     * Provides the JDBC URL for the running PostgreSQL container.
     */
    public static String getJdbcUrl() {
        return postgresContainer.getJdbcUrl();
    }
}