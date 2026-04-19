package xyz.tumit.b3agle.infra.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@Component
@Profile("rollback-datetime")
@RequiredArgsConstructor
public class LiquibaseDateTimeRollbackRunner implements CommandLineRunner {

    private final DataSource liquibaseDataSource;

    @Value("${db.rollback.datetime}")
    private String rollbackDatetime;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    @Override
    public void run(String... args) throws Exception {

        if (rollbackDatetime == null || rollbackDatetime.isBlank()) {
            throw new IllegalArgumentException("Missing liquibase.rollback.datetime");
        }

        LocalDateTime rollbackTime = LocalDateTime.parse(rollbackDatetime, FORMATTER);
        Date rollbackDate = Date.from(rollbackTime.atZone(ZoneId.systemDefault()).toInstant());

        try (Connection connection = liquibaseDataSource.getConnection()) {

            validateRollbackTarget(connection);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(), database);
            log.info("Starting rollback to {}", rollbackDatetime);

            liquibase.rollback(rollbackDate, null);
            log.info("Rollback completed successfully");

        }

        System.exit(0);
    }

    private void validateRollbackTarget(Connection connection) throws SQLException {

        String sql = """
                select id, author, filename, dateexecuted \
                from databasechangelog \
                where dateexecuted <= ? \
                order by dateexecuted \
                desc limit 5
                """;
        log.info("sql {}", sql);
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.parse(rollbackDatetime, FORMATTER)));
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("No matching changeset found before datetime: " + rollbackDatetime);
                }
                log.info("Latest matching changesets before rollback target:");
                do {
                    log.info("id={}, author={}, file={}, executed={}", rs.getString("id"), rs.getString("author"), rs.getString("filename"), rs.getTimestamp("dateexecuted"));
                } while (rs.next());
            }
        }
    }
}
