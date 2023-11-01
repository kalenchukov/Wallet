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
 * Класс параметров конфигурации миграции.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LiquibaseProperty {
	/**
	 * Путь к файлам миграции.
	 */
	private String changeLogFile;

	/**
	 * Схема для служебных таблиц.
	 */
	private String schema;

	/**
	 * Схема для таблиц приложения.
	 */
	private String schemaApp;

	/**
	 * Необходимые миграции.
	 */
	private String contexts;
}
