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
 * Класс игрока.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность игрока")
public class PlayerDto {
	/**
	 * Идентификатор.
	 */
	@Schema(description = "Идентификатор игрока", example = "78")
	private Long playerId;

	/**
	 * Имя.
	 */
	@Schema(description = "Имя игрока", example = "Fedya")
	private String name;
}
