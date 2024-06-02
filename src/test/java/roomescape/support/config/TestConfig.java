package roomescape.support.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import roomescape.support.extension.DatabaseCleaner;

@TestConfiguration
public class TestConfig {

    @Bean
    public Clock clock() {
        Instant fixedInstant = Instant.parse("2024-04-08T00:00:00Z");
        ZoneId zone = ZoneOffset.UTC;

        return Clock.fixed(fixedInstant, zone);
    }

    @Bean
    public DatabaseCleaner databaseCleaner() {
        return new DatabaseCleaner();
    }
}
