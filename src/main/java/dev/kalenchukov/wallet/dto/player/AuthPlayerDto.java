/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.player;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс авторизации игрока.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность авторизации игрока")
public class AuthPlayerDto {
	/**
	 * Имя.
	 */
	@Schema(description = "Имя игрока", example = "Vasya")
	private String name;

	/**
	 * Пароль.
	 */
	@Schema(description = "Пароль игрока", example = "R65^%r65r65")
	private String password;
}
