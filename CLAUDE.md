# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot application using Gradle as the build system. The project is configured with:

- **Java 25** (JDK 25 required)
- **Spring Boot 4.0.5** with Liquibase for database migrations
- **H2 Database** with PostgreSQL compatibility for testing
- **Lombok** for reducing boilerplate code

## Project Structure

```
src/
├── main/java/xyz/tumii/b3agle/  # Main application code
│   └── B3agleApplication.java    # Spring Boot application entry point
├── main/resources/
│   ├── application.properties    # Application configuration
│   └── db/changelog/             # Liquibase migration scripts
└── test/java/xyz/tumii/b3agle/  # Unit and integration tests
    └── B3agleApplicationTests.java
```

## Common Development Commands

### Build and Run

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Build and create an OCI image
./gradlew bootBuildImage
```

### Testing

```bash
# Run all tests (H2)
./gradlew test

# Run a specific test class (H2)
./gradlew test --tests B3agleApplicationTests

# Run tests with detailed output
./gradlew test --info

# Run all tests with PostgreSQL (set environment variable)
SPRING_PROFILES_ACTIVE=test-postgres ./gradlew test

# Run a specific test class with PostgreSQL (set environment variable)
SPRING_PROFILES_ACTIVE=test-postgres ./gradlew test --tests B3agleApplicationTests
```

### Database Migrations

Liquibase migrations are stored in `src/main/resources/db/changelog/`. New migrations should follow the standard Liquibase XML format.

### Development Workflow

1. Make changes to Java code in `src/main/java`
2. Update/add Liquibase migrations if database schema changes
3. Run tests to verify changes
4. Build and run locally to test functionality

## Important Configuration

### Application Properties

`src/main/resources/application.properties`:
- Database connection settings
- H2 console enabled for debugging
- PostgreSQL compatibility for testcontainers

### Database Migrations

Liquibase migrations are stored in `src/main/resources/db/changelog/`. The changelog structure includes:

- `db.changelog-master.yaml` - Main changelog file that includes other changelogs
- `changelog-create.yaml` - Example changelog with table creation

New migrations should follow the standard Liquibase YAML format.

### Database

The application uses H2 database in memory with PostgreSQL compatibility. For testing, Testcontainers with PostgreSQL is used.

**Testcontainers for Runtime Development**: You can also configure the application to use a real PostgreSQL database via Testcontainers during development. This provides a production-like environment without needing a separate PostgreSQL installation. See CONFIG_TESTCONTAINERS.md for detailed instructions.

## Testing Strategy

- **Unit tests**: Use JUnit 5 with Spring Boot test support
- **Integration tests**: Use `@SpringBootTest` annotation
- **Liquibase tests**: Use `spring-boot-starter-liquibase-test`
- **Web MVC tests**: Use `spring-boot-starter-webmvc-test`

## Build Tool Configuration

`build.gradle`:
- Java toolchain configured for Java 25
- Dependencies managed via Spring Boot BOM
- Lombok annotation processing enabled
- Testcontainers for integration testing

## Tips for Development

1. **Lombok**: Ensure your IDE has Lombok plugin installed to avoid compilation errors in IDE
2. **H2 Console**: Access H2 console at `/h2-console` when application is running (check application.properties for credentials)
3. **Gradle Wrapper**: Use `./gradlew` instead of system Gradle to ensure consistent builds
4. **Testcontainers**: When running integration tests or development with PostgreSQL, ensure Docker is running
5. **Testcontainers for Runtime**: For production-like development, configure PostgreSQL via Testcontainers (see CONFIG_TESTCONTAINERS.md)

## Git Workflow

- Main branch: `main`
- Current branch: `tumit/liquibase`
- Use feature branches for new work
- Run tests before committing

## Troubleshooting

- **Build failures**: Check Java version (must be 25)
- **Test failures**: Ensure Docker is running for Testcontainers tests
- **Lombok issues**: Install Lombok plugin in your IDE and enable annotation processing

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/4.0.5/reference/html/)
- [Liquibase Documentation](https://docs.liquibase.com/)
- [Gradle Documentation](https://docs.gradle.org/current/userguide/userguide.html)
