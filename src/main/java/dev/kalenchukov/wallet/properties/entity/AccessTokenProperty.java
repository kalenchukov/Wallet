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
 * Класс параметров конфигурации токенов доступа.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccessTokenProperty {
	/**
	 * Сервер авторизации выдавший токен доступа.
	 */
	private String server;

	/**
	 * Время жизни токена доступа в секундах.
	 */
	private Integer ttl;

	/**
	 * Строка для формирования токенов доступа.
	 */
	private String secret;
}
