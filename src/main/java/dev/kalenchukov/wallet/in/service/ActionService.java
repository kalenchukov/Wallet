/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.resources.ActionType;

import java.util.Set;

/**
 * Интерфейс для реализации класса сервиса действий.
 */
public interface ActionService {
	/**
	 * Добавляет действие.
	 *
	 * @param playerId         идентификатор игрока.
	 * @param actionType       тип.
	 * @param actionTypeStatus статус.
	 * @return действие.
	 */
	Action add(long playerId, ActionType actionType, ActionType.Status actionTypeStatus);

	/**
	 * Ищет действия.
	 *
	 * @param playerId идентификатор игрока.
	 * @return найденные действия.
	 */
	Set<Action> find(long playerId);
}
