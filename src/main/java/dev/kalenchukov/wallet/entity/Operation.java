/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import dev.kalenchukov.wallet.exceptions.NegativeAmountOperationException;
import dev.kalenchukov.wallet.resources.OperationType;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Класс операции.
 */
public final class Operation {
	/**
	 * Счётчик идентификаторов.
	 */
	private static long SCORE_ID = 0;

	/**
	 * Идентификатор.
	 */
	private final long operationId;

	/**
	 * Игрок.
	 */
	private final Account account;

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
	 * @param account       счёт.
	 * @param operationType тип.
	 * @param amount        сумма.
	 * @throws NegativeAmountOperationException если сумма операции меньше нуля.
	 */
	public Operation(final Account account, final OperationType operationType, final BigDecimal amount) {
		Objects.requireNonNull(account);
		Objects.requireNonNull(operationType);
		Objects.requireNonNull(amount);

		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new NegativeAmountOperationException(amount);
		}

		this.operationId = ++SCORE_ID;
		this.account = account;
		this.operationType = operationType;
		this.amount = amount;
	}

	/**
	 * Возвращает идентификатор.
	 *
	 * @return идентификатор.
	 */
	public long getOperationId() {
		return this.operationId;
	}

	/**
	 * Возвращает счет.
	 *
	 * @return счет.
	 */
	public Account getAccount() {
		return this.account;
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
				"account='" + this.account + "', " +
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
