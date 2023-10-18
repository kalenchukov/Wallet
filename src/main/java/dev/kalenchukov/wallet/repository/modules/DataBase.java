/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.modules;

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
	private static final DataSource DATA_SOURCE = DataBase.init();

	static {
		Liquibase.init(
				Config.get().getProperty("url"),
				Config.get().getProperty("username"),
				Config.get().getProperty("password")
		);
	}

	/**
	 * Возвращает источник данных.
	 *
	 * @return источник данных.
	 */
	public static DataSource getDataSource() {
		return DataBase.DATA_SOURCE;
	}

	/**
	 * Инициализирует источник данных.
	 *
	 * @return источник данных.
	 */
	private static DataSource init() {
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setUrl(Config.get().getProperty("url"));
		dataSource.setUser(Config.get().getProperty("username"));
		dataSource.setPassword(Config.get().getProperty("password"));
		dataSource.setCurrentSchema(Config.get().getProperty("application.schema"));
		return dataSource;
	}
}
