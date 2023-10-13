/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.entity.Player;

import java.util.Set;

/**
 * Интерфейс для реализации класса хранилища действий.
 */
public interface ActionRepository {
	/**
	 * Сохраняет действие.
	 *
	 * @param action действие.
	 * @return действие.
	 */
	Action save(Action action);

	/**
	 * Ищет действия.
	 *
	 * @param playerId идентификатор игрока.
	 * @return найденные действия.
	 */
	Set<Action> find(long playerId);
}
