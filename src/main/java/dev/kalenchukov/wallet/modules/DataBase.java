/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.modules;

import dev.kalenchukov.wallet.Config;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

/**
 * Класс источника данных.
 */
public final class DataBase {
	/**
	 * Источник данных.
	 */
	private static DataSource DATA_SOURCE;

	/**
	 * Возвращает источник данных.
	 *
	 * @return источник данных.
	 */
	public static DataSource getDataSource() {
		if (DATA_SOURCE == null) {
			DataBase.DATA_SOURCE = DataBase.init();

			Liquibase.init(
					Config.get().getProperty("database.url"),
					Config.get().getProperty("database.username"),
					Config.get().getProperty("database.password")
			);
		}

		return DataBase.DATA_SOURCE;
	}

	/**
	 * Инициализирует источник данных.
	 *
	 * @return источник данных.
	 */
	private static DataSource init() {
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setUrl(Config.get().getProperty("database.url"));
		dataSource.setUser(Config.get().getProperty("database.username"));
		dataSource.setPassword(Config.get().getProperty("database.password"));
		dataSource.setCurrentSchema(Config.get().getProperty("liquibase.schema.app"));
		return dataSource;
	}
}
