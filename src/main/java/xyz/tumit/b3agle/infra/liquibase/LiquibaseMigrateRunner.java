package xyz.tumit.b3agle.infra.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Component
@Profile("migrate")
@RequiredArgsConstructor
public class LiquibaseMigrateRunner implements CommandLineRunner {

    private final DataSource liquibaseDataSource;

    private static final String CHANGELOG_FILE = "db/changelog/db.changelog-master.yaml";

    @Override
    public void run(String... args) throws Exception {

        log.info("Starting Liquibase migration...");
        try (Connection connection = liquibaseDataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(CHANGELOG_FILE, new ClassLoaderResourceAccessor(), database);
            liquibase.update((String) null);
            log.info("Liquibase migration completed successfully.");
        } catch (Exception ex) {
            log.error("Liquibase migration failed.", ex);
            throw ex;
        }
        System.exit(0);
    }
}
