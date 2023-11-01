/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.modules;

import dev.kalenchukov.wallet.properties.Props;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Класс миграции базы данных.
 */
public final class Liquibase {
	/**
	 * Инициализирует миграции.
	 *
	 * @param url      URL подключения к базе данных.
	 * @param username имя пользователя базы данных.
	 * @param password пароль пользователя базы данных.
	 */
	public static void init(final String url, final String username, final String password) {
		Objects.requireNonNull(url);
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			Liquibase.createSchema(connection, Props.get().getLiquibase().getSchema());
			Liquibase.createSchema(connection, Props.get().getLiquibase().getSchemaApp());

			JdbcConnection jdbcConnection = new JdbcConnection(connection);
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
			database.setLiquibaseSchemaName(Props.get().getLiquibase().getSchema());
			database.setDefaultSchemaName(Props.get().getLiquibase().getSchemaApp());

			CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
			updateCommand.addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, database);
			updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, Props.get().getLiquibase().getChangeLogFile());
			updateCommand.addArgumentValue(UpdateCommandStep.CONTEXTS_ARG, Props.get().getLiquibase().getContexts());
			updateCommand.execute();
		} catch (LiquibaseException exception) {
			throw new RuntimeException("Не удалось выполнить миграцию базы данных.");
		} catch (SQLException exception) {
			throw new RuntimeException("Не удалось подключиться к базе данных.");
		}
	}

	/**
	 * Создаёт схему в базе данных.
	 *
	 * @param connection подключение к базе данных.
	 * @param schema     схема.
	 */
	public static void createSchema(final Connection connection, final String schema) {
		Objects.requireNonNull(connection);
		Objects.requireNonNull(schema);

		String query = "CREATE SCHEMA IF NOT EXISTS " + schema;

		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.execute();
		} catch (SQLException exception) {
			throw new RuntimeException("Не удалось создать схему '" + schema + "' в базе данных.");
		}
	}
}
