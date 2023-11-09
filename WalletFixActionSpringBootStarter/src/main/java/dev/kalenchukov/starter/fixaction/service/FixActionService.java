/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.fixaction.service;

import dev.kalenchukov.starter.fixaction.entity.Action;
import dev.kalenchukov.starter.fixaction.types.ActionType;

/**
 * Интерфейс для реализации класса сервиса действий.
 */
public interface FixActionService {
	/**
	 * Добавляет действие.
	 *
	 * @param playerId         идентификатор игрока.
	 * @param actionType       тип.
	 * @param actionTypeStatus статус.
	 * @return действие.
	 */
	Action add(long playerId, ActionType actionType, ActionType.Status actionTypeStatus);
}
