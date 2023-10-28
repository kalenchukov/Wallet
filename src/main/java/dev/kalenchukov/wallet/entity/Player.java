/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс игрока.
 */
@Data
@AllArgsConstructor
public class Player {
	/**
	 * Идентификатор.
	 */
	private long playerId;

	/**
	 * Имя.
	 */
	private String name;

	/**
	 * Пароль.
	 */
	private String password;
}
