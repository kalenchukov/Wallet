/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.modules;

import dev.kalenchukov.wallet.Config;
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
			Liquibase.createSchema(connection, Config.get().getProperty("liquibase.schema"));
			Liquibase.createSchema(connection, Config.get().getProperty("application.schema"));

			JdbcConnection jdbcConnection = new JdbcConnection(connection);
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
			database.setLiquibaseSchemaName(Config.get().getProperty("liquibase.schema"));
			database.setDefaultSchemaName(Config.get().getProperty("application.schema"));

			CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
			updateCommand.addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, database);
			updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, Config.get().getProperty("change.log.file"));
			updateCommand.addArgumentValue(UpdateCommandStep.CONTEXTS_ARG, Config.get().getProperty("contexts"));
			updateCommand.execute();
		} catch (LiquibaseException exception) {
			System.out.println("Не удалось выполнить миграцию базы данных.");
		} catch (SQLException exception) {
			System.out.println("Не удалось подключиться к базе данных.");
		}
	}

	/**
	 * Создаёт схему в базе данных.
	 *
	 * @param connection подключение к базе данных.
	 * @param schema     схемы.
	 */
	public static void createSchema(final Connection connection, final String schema) {
		Objects.requireNonNull(connection);
		Objects.requireNonNull(schema);

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"CREATE SCHEMA IF NOT EXISTS " + schema)) {
			preparedStatement.execute();
		} catch (SQLException exception) {
			System.out.println("Не удалось создать схему '" + schema + "' в базе данных.");
		}
	}
}
