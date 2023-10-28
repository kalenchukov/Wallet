/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс токена доступа игрока.
 */
@Data
@AllArgsConstructor
public class AccessToken {
	/**
	 * Токен доступа.
	 */
	private String accessToken;
}
