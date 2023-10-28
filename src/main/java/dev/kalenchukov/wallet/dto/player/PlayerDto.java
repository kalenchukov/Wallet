/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.player;

import lombok.*;

/**
 * Класс игрока.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
	/**
	 * Идентификатор.
	 */
	private Long playerId;

	/**
	 * Имя.
	 */
	private String name;
}
