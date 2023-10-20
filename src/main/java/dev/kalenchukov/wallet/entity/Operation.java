/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import dev.kalenchukov.wallet.type.OperationType;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Класс операции.
 */
public final class Operation {
	/**
	 * Идентификатор.
	 */
	private final long operationId;

	/**
	 * Идентификатор игрока.
	 */
	private final long playerId;

	/**
	 * Идентификатор счёта.
	 */
	private final long accountId;

	/**
	 * Тип.
	 */
	private final OperationType operationType;

	/**
	 * Сумма.
	 */
	private final BigDecimal amount;

	/**
	 * Конструирует операцию.
	 *
	 * @param playerId   	идентификатор игрока.
	 * @param accountId     идентификатор счёта.
	 * @param operationType тип.
	 * @param amount        сумма.
	 */
	public Operation(final long playerId, final long accountId, final OperationType operationType, final BigDecimal amount) {
		this(0L, playerId, accountId, operationType, amount);
	}

	/**
	 * Конструирует операцию.
	 *
	 * @param operationId   идентификатор операции.
	 * @param playerId   	идентификатор игрока.
	 * @param accountId     идентификатор счёта.
	 * @param operationType тип.
	 * @param amount        сумма.
	 */
	public Operation(final long operationId, final long playerId, final long accountId, final OperationType operationType, final BigDecimal amount) {
		Objects.requireNonNull(operationType);
		Objects.requireNonNull(amount);

		this.operationId = operationId;
		this.playerId = playerId;
		this.accountId = accountId;
		this.operationType = operationType;
		this.amount = amount;
	}

	/**
	 * Возвращает идентификатор операции.
	 *
	 * @return идентификатор операции.
	 */
	public long getOperationId() {
		return this.operationId;
	}

	/**
	 * Возвращает идентификатор игрока.
	 *
	 * @return идентификатор игрока.
	 */
	public long getPlayerId() {
		return this.playerId;
	}

	/**
	 * Возвращает идентификатор счета.
	 *
	 * @return идентификатор счета.
	 */
	public long getAccountId() {
		return this.accountId;
	}

	/**
	 * Возвращает тип.
	 *
	 * @return тип.
	 */
	public OperationType getOperationType() {
		return this.operationType;
	}

	/**
	 * Возвращает сумму.
	 *
	 * @return сумму.
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Operation{" +
				"operationId='" + this.operationId + "', " +
				"playerId='" + this.playerId + "', " +
				"accountId='" + this.accountId + "', " +
				"operationType='" + this.operationType + "', " +
				"amount='" + this.amount + "'" +
				'}';
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int result = 0;

		result = 31 * result + Objects.hashCode(this.operationId);

		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param obj {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}

		Operation operation = (Operation) obj;

		if (!Objects.equals(this.operationId, operation.getOperationId())) {
			return false;
		}

		return true;
	}
}
