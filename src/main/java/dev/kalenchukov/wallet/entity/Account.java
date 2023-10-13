/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import dev.kalenchukov.wallet.exceptions.NegativeAmountOperationException;
import dev.kalenchukov.wallet.exceptions.OutOfAmountOperationException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Класс счёта.
 */
public final class Account {
	/**
	 * Счётчик идентификаторов.
	 */
	private static long SCORE_ID = 0;

	/**
	 * Идентификатор.
	 */
	private final long accountId;
	/**
	 * Идентификатор игрока.
	 */
	private final long playerId;
	/**
	 * Сумма.
	 */
	private BigDecimal amount;

	/**
	 * Конструирует счёт.
	 *
	 * @param playerId идентификатор игрока.
	 */
	public Account(final long playerId) {
		this.accountId = ++SCORE_ID;
		this.amount = BigDecimal.ZERO;
		this.playerId = playerId;
	}

	/**
	 * Возвращает сумму.
	 *
	 * @return сумма.
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * Устанавливает сумму.
	 *
	 * @param amount сумма.
	 */
	public void setAmount(final BigDecimal amount) {
		Objects.requireNonNull(amount);

		this.amount = amount;
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
	 * Возвращает идентификатор.
	 *
	 * @return идентификатор.
	 */
	public long getAccountId() {
		return this.accountId;
	}

	/**
	 * Пополняет счёт.
	 *
	 * @param amount сумма.
	 * @throws NegativeAmountOperationException если сумма меньше нуля.
	 */
	public void credit(final BigDecimal amount) {
		Objects.requireNonNull(amount);

		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new NegativeAmountOperationException(amount);
		}

		this.amount = amount.add(this.amount);
	}

	/**
	 * Списывает со счёта.
	 *
	 * @param amount сумма.
	 * @throws NegativeAmountOperationException если сумма меньше нуля.
	 * @throws OutOfAmountOperationException    если для списания недостаточно средств.
	 */
	public void debit(final BigDecimal amount) {
		Objects.requireNonNull(amount);

		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new NegativeAmountOperationException(amount);
		}

		if (this.amount.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
			throw new OutOfAmountOperationException(this.amount);
		}

		this.amount = amount.subtract(this.amount).abs();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Account{" +
				"accountId='" + this.accountId + "', " +
				"playerId='" + this.playerId + "', " +
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

		result = 31 * result + Objects.hashCode(this.accountId);

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

		Account account = (Account) obj;

		if (!Objects.equals(this.accountId, account.getAccountId())) {
			return false;
		}

		return true;
	}
}
