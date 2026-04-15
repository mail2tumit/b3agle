# PostgreSQL with Testcontainers for Runtime Development & Testing

This guide explains how to configure your Spring Boot application to use PostgreSQL via Testcontainers for both runtime development and testing. This approach gives you a real PostgreSQL database without needing a separate installation.

## Why Use Testcontainers?

Testcontainers provides lightweight, throwaway instances of common databases that run in Docker containers. Using this for development and testing gives you:

- **Real database behavior** - Not an in-memory approximation
- **No installation needed** - Just Docker
- **Isolated environments** - Each developer can have their own DB
- **Production-like** - Closer to production behavior
- **Consistent** - Same version across all development and test environments

## Configuration Overview

### 1. Dependencies

Update `build.gradle` to include Testcontainers dependencies:

```gradle
dependencies {
    // ... existing dependencies
    
    // Testcontainers for runtime and testing
    implementation 'org.springframework.boot:spring-boot-testcontainers'
    implementation 'org.testcontainers:postgresql:1.21.4'
    implementation 'org.testcontainers:junit-jupiter:1.21.4'
}
```

### 2. Testcontainer Configuration Class

Create a configuration class to set up the PostgreSQL container:

**File: `src/main/java/xyz/tumii/b3agle/config/TestcontainerConfig.java`**

```java
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
```

### 3. Application Properties

Create separate profiles for development and testing:

**File: `src/main/resources/application-dev-postgres.properties`**

```properties
# PostgreSQL Testcontainer configuration for development
spring.datasource.url=${POSTGRES_JDBC_URL:${POSTGRES_DB_URL:jdbc:tc:postgresql:16:///b3agle}}
spring.datasource.username=dev
spring.datasource.password=dev
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.yaml
spring.main.allow-bean-definition-overriding=true
```

**File: `src/main/resources/application-test-postgres.properties`**

```properties
# PostgreSQL Testcontainer configuration for testing
spring.datasource.url=${POSTGRES_JDBC_URL:${POSTGRES_DB_URL:jdbc:tc:postgresql:16:///b3agle_test}}
spring.datasource.username=dev
spring.datasource.password=dev
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.yaml
spring.main.allow-bean-definition-overriding=true
```

### 4. Default Application Properties

Update the default `application.properties`:

```properties
# Default configuration - uses H2 for simplicity
spring.application.name=b3agle

# H2 Database configuration
spring.datasource.url=jdbc:tc:postgresql:16:///demo
spring.datasource.username=demo
spring.datasource.password=demo

# H2 Console configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Liquibase configuration
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.yaml

# JPA configuration
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

## Usage

### Running with PostgreSQL (Development)

```bash
# Make sure Docker is running
docker --version

# Build and run with PostgreSQL profile
./gradlew clean build
SPRING_PROFILES_ACTIVE=dev-postgres ./gradlew bootRun
```

### Running Tests with PostgreSQL

```bash
# Set environment variable and run tests
SPRING_PROFILES_ACTIVE=test-postgres ./gradlew test
```

### Running Specific Tests

```bash
# Set environment variable and run specific test
SPRING_PROFILES_ACTIVE=test-postgres ./gradlew test --tests B3agleApplicationTests
```

## Liquibase Migrations

Liquibase changelog files are located in `src/main/resources/db/changelog/`. The structure includes:

- `db.changelog-master.yaml` - Main changelog file that includes other changelogs
- `changelog-create.yaml` - Example changelog with table creation

### Creating New Migrations

1. Create a new YAML file in `src/main/resources/db/changelog/` following the pattern `changelog-*.yaml`
2. Add an include reference in `db.changelog-master.yaml`:

```yaml
databaseChangeLog:
  - include:
      file: classpath:/db/changelog/changelog-create.yaml
  - include:
      file: classpath:/db/changelog/changelog-new.yaml  # Add your new changelog here
```

3. Define your changes in the new changelog file using Liquibase YAML format.

## Docker Requirements

- **Docker must be installed and running**
- For Mac/Windows: Docker Desktop must be running
- For Linux: docker service should be active
- Testcontainers will automatically pull the PostgreSQL 16 Alpine image on first run

## Container Lifecycle

- **Reuse**: The container is configured with `.withReuse(true)` for faster subsequent startups
- **Startup**: Container starts when the application starts
- **Shutdown**: Container stops when the application stops
- **Data**: Data is ephemeral - it resets when the container restarts

## Persistent Data (Optional)

If you need persistent data across restarts, consider:

### Volume Mounting

```java
postgresContainer = new PostgreSQLContainer<>(
        DockerImageName.parse("postgres:16-alpine"))
        .withDatabaseName("b3agle")
        .withUsername("dev")
        .withPassword("dev")
        .withReuse(true)
        .withFileSystemBind("/path/to/data", "/var/lib/postgresql/data");
```

### Separate PostgreSQL Instance

For development with persistent data, run a standalone PostgreSQL instance:

```bash
# Start PostgreSQL using Docker Compose
docker compose up -d postgres

# Configure application.properties to connect to the external instance
spring.datasource.url=jdbc:postgresql://localhost:5432/b3agle
spring.datasource.username=dev
spring.datasource.password=dev
```

## Testing Strategy

### Unit Tests

- Use `@SpringBootTest` with `test-postgres` profile
- Tests run with a real PostgreSQL database via Testcontainers
- Data is cleaned up after each test

### Integration Tests

- Use `@SpringBootTest` with `test-postgres` profile
- Tests verify end-to-end functionality with real database
- Liquibase migrations are applied automatically

### Testcontainers JUnit Integration

The `junit-jupiter` Testcontainers module provides integration with JUnit 5. The configuration ensures that the container is properly managed during test execution.

## Troubleshooting

### Docker Issues

- **"Cannot connect to Docker daemon"**: Start Docker Desktop or docker service
- **Permission denied**: Add your user to the docker group (Linux)
- **Image pull errors**: Check network connectivity

### Application Issues

- **Connection refused**: Ensure container started successfully
- **Port conflicts**: Check if another PostgreSQL instance is running
- **Timeout errors**: Increase startup time if needed

### Testing Issues

- **Tests failing**: Check that Docker is running
- **Liquibase errors**: Verify changelog files are in the correct location
- **Container not starting**: Check Docker logs for more details

### Common Errors and Solutions

**Error**: `org.testcontainers.containers.PostgreSQLContainer cannot be resolved`

**Solution**: Ensure Testcontainers dependencies are in the correct configuration (implementation, not testImplementation).

**Error**: `liquibase.exception.ChangeLogParseException: Error parsing classpath:/db/changelog/db.changelog-master.yaml`

**Solution**: Verify that the changelog file exists and the include references are correct.

**Error**: `java.sql.SQLException: Connection refused`

**Solution**: Ensure Docker is running and the PostgreSQL container has started successfully.

## Benefits of This Approach

1. **Production-like environment** - Real PostgreSQL instead of H2
2. **Isolated** - Each developer and test has its own DB instance
3. **Consistent** - Same version across all environments
4. **Clean** - No leftover databases or configuration issues
5. **Fast startup** - Container reuse speeds up development

## When to Use This vs H2

- **Use Testcontainer PostgreSQL**: When you need production-like behavior, complex SQL, or specific PostgreSQL features
- **Use H2**: For simple tests, quick prototyping, or when Docker isn't available
- **Use external PostgreSQL**: For persistent development data

## Liquibase Integration

Liquibase works seamlessly with Testcontainers. The migrations are automatically applied when the application starts with the `dev-postgres` or `test-postgres` profiles.

### Creating Migrations

1. Create a new YAML file in `src/main/resources/db/changelog/`
2. Define your changes using Liquibase YAML format
3. Add an include reference in `db.changelog-master.yaml`

### Migration Example

```yaml
databaseChangeLog:
  - changeSet:
      id: 100
      author: developer
      changes:
        - addColumn:
            tableName: person
            columns:
              - column:
                  name: age
                  type: integer
```

## Performance Considerations

- **Container reuse**: Enabled with `.withReuse(true)` for faster subsequent startups
- **Startup time**: First startup may take 5-10 seconds for container pull and initialization
- **Test execution**: Tests run with real PostgreSQL, which may be slower than H2 but more accurate

## CI/CD Integration

This setup is ideal for CI/CD pipelines:

1. Ensure Docker is available in the CI environment
2. Run tests with the `test-postgres` profile
3. Tests will automatically start a PostgreSQL container
4. No external database dependencies needed

## Additional Resources

- [Testcontainers Documentation](https://www.testcontainers.org/)
- [Spring Boot Testcontainers](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-testcontainers)
- [Liquibase Documentation](https://docs.liquibase.com/)
- [PostgreSQL Docker Image](https://hub.docker.com/_/postgres/)
