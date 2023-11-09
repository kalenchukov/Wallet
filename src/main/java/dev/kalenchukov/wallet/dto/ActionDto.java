/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto;

import dev.kalenchukov.starter.fixaction.types.ActionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс действия.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность действия", accessMode = Schema.AccessMode.READ_ONLY)
public class ActionDto {
	/**
	 * Идентификатор.
	 */
	@Schema(description = "Идентификатор действия", example = "87")
	private Long actionId;

	/**
	 * Идентификатор игрока.
	 */
	@Schema(description = "Идентификатор игрока", example = "4")
	private Long playerId;

	/**
	 * Тип.
	 */
	@Schema(description = "Тип действия", example = "CREATE_ACCOUNT")
	private ActionType actionType;

	/**
	 * Статус.
	 */
	@Schema(description = "Статус действия", example = "SUCCESS")
	private ActionType.Status actionTypeStatus;
}
