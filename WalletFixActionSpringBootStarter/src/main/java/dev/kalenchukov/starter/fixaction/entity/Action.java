/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.fixaction.entity;

import dev.kalenchukov.starter.fixaction.types.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс действий.
 */
@Data
@AllArgsConstructor
public class Action {
	/**
	 * Идентификатор.
	 */
	private long actionId;

	/**
	 * Идентификатор игрока.
	 */
	private long playerId;

	/**
	 * Тип.
	 */
	private ActionType actionType;

	/**
	 * Статус.
	 */
	private ActionType.Status actionTypeStatus;
}
