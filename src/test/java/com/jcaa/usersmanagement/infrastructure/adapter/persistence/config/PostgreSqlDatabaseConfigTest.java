package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PostgreSqlDatabaseConfigTest {

  private static final String HOST = "aws-0-us-east-1.pooler.supabase.com";
  private static final int PORT = 5432;
  private static final String DATABASE = "postgres";
  private static final String USERNAME = "postgres.project";
  private static final String PASSWORD = "secret";

  @ParameterizedTest
  @CsvSource({
    "REQUIRED, require",
    "require, require",
    "DISABLED, disable",
    "PREFERRED, prefer",
    "VERIFY_CA, verify-ca",
    "VERIFY_IDENTITY, verify-full",
    "allow, allow"
  })
  void shouldBuildJdbcUrlTranslatingSslModeToPostgreSqlValues(
      final String configuredSslMode, final String expectedSslMode) {
    // Arrange
    final PostgreSqlDatabaseConfig config =
        new PostgreSqlDatabaseConfig(HOST, PORT, DATABASE, USERNAME, PASSWORD, configuredSslMode);

    // Act
    final String jdbcUrl = config.buildJdbcUrl();

    // Assert
    assertThat(jdbcUrl)
        .isEqualTo(
            "jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres?sslmode="
                + expectedSslMode);
  }
}
