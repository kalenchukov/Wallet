/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.starter.fixaction.entity.Action;

import java.util.List;

/**
 * Интерфейс для реализации класса хранилища действий.
 */
public interface ActionRepository {
	/**
	 * Ищет действия.
	 *
	 * @param playerId идентификатор игрока.
	 * @return найденные действия.
	 */
	List<Action> find(long playerId);
}
