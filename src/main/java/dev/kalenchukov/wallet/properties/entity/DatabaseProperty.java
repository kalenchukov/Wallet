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
 * Класс параметров конфигурации базы данных.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DatabaseProperty {
	/**
	 * URL базы данных.
	 */
	private String url;

	/**
	 * Название базы данных.
	 */
	private String name;

	/**
	 * Пользователь.
	 */
	private String username;

	/**
	 * Пароль.
	 */
	private String password;
}
