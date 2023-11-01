/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.modules;

import dev.kalenchukov.wallet.properties.Props;
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
					Props.get().getDatabase().getUrl(),
					Props.get().getDatabase().getUsername(),
					Props.get().getDatabase().getPassword()
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
		dataSource.setUrl(Props.get().getDatabase().getUrl());
		dataSource.setUser(Props.get().getDatabase().getUsername());
		dataSource.setPassword(Props.get().getDatabase().getPassword());
		dataSource.setCurrentSchema(Props.get().getLiquibase().getSchemaApp());
		return dataSource;
	}
}
