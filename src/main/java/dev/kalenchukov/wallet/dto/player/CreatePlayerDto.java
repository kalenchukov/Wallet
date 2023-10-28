/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.player;

import lombok.*;

/**
 * Класс добавления игрока.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlayerDto {
	/**
	 * Имя.
	 */
	private String name;

	/**
	 * Пароль.
	 */
	private String password;
}
