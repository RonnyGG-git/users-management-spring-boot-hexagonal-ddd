package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import java.util.Locale;

public record PostgreSqlDatabaseConfig(
    String host,
    int port,
    String databaseName,
    String username,
    String password,
    String sslMode) {
  private static final String URL_TEMPLATE = "jdbc:postgresql://%s:%d/%s?sslmode=%s";

  public String buildJdbcUrl() {
    return String.format(URL_TEMPLATE, host, port, databaseName, toPostgreSqlSslMode(sslMode));
  }

  // DB_SSL_MODE usa los valores de MySQL (DISABLED, REQUIRED...); el driver de
  // PostgreSQL espera disable, require, etc. Se aceptan ambas formas.
  private static String toPostgreSqlSslMode(final String sslMode) {
    final String mode = sslMode.trim().toUpperCase(Locale.ROOT);
    return switch (mode) {
      case "DISABLED", "DISABLE" -> "disable";
      case "PREFERRED", "PREFER" -> "prefer";
      case "REQUIRED", "REQUIRE" -> "require";
      case "VERIFY_CA", "VERIFY-CA" -> "verify-ca";
      case "VERIFY_IDENTITY", "VERIFY-FULL" -> "verify-full";
      default -> sslMode.trim().toLowerCase(Locale.ROOT);
    };
  }
}
