/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NotFoundOperationException;
import dev.kalenchukov.wallet.type.OperationType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Интерфейс для реализации класса сервиса операций.
 */
public interface OperationService {
	/**
	 * Добавляет операцию.
	 *
	 * @param playerId      идентификатор игрока.
	 * @param accountId     идентификатор счёта.
	 * @param operationType тип.
	 * @param amount        сумма.
	 * @return операцию.
	 */
	Operation add(long playerId, long accountId, OperationType operationType, BigDecimal amount);

	/**
	 * Возвращает операцию.
	 *
	 * @param playerId    идентификатор игрока.
	 * @param accountId   идентификатор счёта.
	 * @param operationId идентификатор операции.
	 * @return операцию.
	 * @throws NotFoundOperationException если операция не найдена.
	 */
	Operation findById(long playerId, long accountId, long operationId) throws NotFoundOperationException;

	/**
	 * Ищет операции.
	 *
	 * @param playerId  идентификатор игрока.
	 * @param accountId идентификатор счёта.
	 * @return найденные операции.
	 */
	List<Operation> find(long playerId, long accountId);
}
