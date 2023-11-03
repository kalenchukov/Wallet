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
 * Класс добавления игрока.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность создания игрока")
public class CreatePlayerDto {
	/**
	 * Имя.
	 */
	@Schema(description = "Имя игрока", example = "Petya")
	private String name;

	/**
	 * Пароль.
	 */
	@Schema(description = "Пароль игрока", example = "^%Vr65V^678b8fo")
	private String password;
}
