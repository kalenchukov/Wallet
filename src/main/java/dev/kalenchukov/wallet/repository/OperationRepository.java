/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.ApplicationException;

import java.util.Optional;
import java.util.Set;

/**
 * Интерфейс для реализации класса хранилища операций.
 */
public interface OperationRepository {
	/**
	 * Сохраняет операцию.
	 *
	 * @param operation операция.
	 * @return операцию.
	 * @throws ApplicationException если произошла ошибка при работе с приложением.
	 */
	Operation save(Operation operation);

	/**
	 * Возвращает операцию.
	 *
	 * @param operationId идентификатор операции.
	 * @param playerId    идентификатор игрока.
	 * @return операцию.
	 * @throws ApplicationException если произошла ошибка при работе с приложением.
	 */
	Optional<Operation> findById(long operationId, long playerId);

	/**
	 * Ищет операции.
	 *
	 * @param accountId идентификатор счёта.
	 * @param playerId  идентификатор игрока.
	 * @return найденные операции.
	 * @throws ApplicationException если произошла ошибка при работе с приложением.
	 */
	Set<Operation> find(long accountId, long playerId);
}
