# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

B3agle is a Spring Boot 4.0.5 application using Java 25, Gradle, Spring Data JPA, and PostgreSQL. It provides REST APIs for stock management.

## Build Commands

```bash
./gradlew build          # Build the project
./gradlew test           # Run all tests
./gradlew bootRun        # Run the application
./gradlew bootTestRun    # Run with test configuration

# Running a single test
./gradlew test --tests "xyz.tumit.b3agle.B3agleApplicationTests"
./gradlew test --tests "xyz.tumit.b3agle.*"  # Run tests matching pattern
```

## Development Setup

1. Start PostgreSQL:
   ```bash
   docker-compose up -d
   ```

2. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The application connects to PostgreSQL at `localhost:5432/b3agledb` with user `b3agleuser`.

## Architecture

The codebase follows a **layered package structure by domain**:

```
xyz.tumit.b3agle/
├── stock/
│   ├── controller/          # REST API endpoints
│   ├── service/             # Business logic
│   └── infra/persistence/   # Data access layer
│       ├── entity/          # JPA entities
│       └── repository/      # Spring Data repositories
```

Key patterns:
- Domain-driven packaging: all stock-related code lives under `stock/` subpackage
- Infrastructure layer uses `infra/` subpackage for persistence concerns
- Lombok is used for boilerplate reduction (`@Data`, `@RequiredArgsConstructor`)
- Constructor injection is preferred via `@RequiredArgsConstructor`

## Technology Stack

- Spring Boot 4.0.5 with Java 25
- Spring Web MVC for REST APIs
- Spring Data JPA with PostgreSQL
- Spring Boot Actuator for health endpoints
- Lombok for code generation
- Gradle with Kotlin DSL
