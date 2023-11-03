/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.properties.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс основных параметров конфигурации.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MasterProperty {
	/**
	 * Параметр контекста конфигурации.
	 */
	private String context;

	/**
	 * Параметры конфигурации токенов доступа.
	 */
	private AccessTokenProperty accessToken;

	/**
	 * Параметры конфигурации базы данных.
	 */
	private DatabaseProperty database;

	/**
	 * Параметры конфигурации миграции.
	 */
	private LiquibaseProperty liquibase;

	/**
	 * Параметры конфигурации тестирования.
	 */
	private TestProperty test;
}
