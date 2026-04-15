package xyz.tumii.b3agle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test-postgres")
class PostgreSQLTest {

    @Test
    void contextLoadsWithPostgreSQL() {
        // This test verifies that the application context loads with PostgreSQL
    }
}