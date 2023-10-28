/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import dev.kalenchukov.wallet.type.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Класс операции.
 */
@Data
@AllArgsConstructor
public class Operation {
	/**
	 * Идентификатор.
	 */
	private long operationId;

	/**
	 * Идентификатор игрока.
	 */
	private long playerId;

	/**
	 * Идентификатор счёта.
	 */
	private long accountId;

	/**
	 * Тип.
	 */
	private OperationType operationType;

	/**
	 * Сумма.
	 */
	private BigDecimal amount;
}
