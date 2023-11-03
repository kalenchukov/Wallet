/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Operation;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для реализации класса хранилища операций.
 */
public interface OperationRepository {
	/**
	 * Сохраняет операцию.
	 *
	 * @param operation операция.
	 * @return операцию.
	 */
	Operation save(Operation operation);

	/**
	 * Возвращает операцию.
	 *
	 * @param playerId    идентификатор игрока.
	 * @param accountId   идентификатор счёта.
	 * @param operationId идентификатор операции.
	 * @return операцию.
	 */
	Optional<Operation> findById(long playerId, long accountId, long operationId);

	/**
	 * Ищет операции.
	 *
	 * @param playerId  идентификатор игрока.
	 * @param accountId идентификатор счёта.
	 * @return найденные операции.
	 */
	List<Operation> find(long playerId, long accountId);
}
