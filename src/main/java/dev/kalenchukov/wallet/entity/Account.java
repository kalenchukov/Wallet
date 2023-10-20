/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Класс счёта.
 */
public final class Account {
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
	private final BigDecimal amount;

	/**
	 * Конструирует счёт.
	 *
	 * @param playerId  идентификатор игрока.
	 * @param amount    сумма.
	 */
	public Account(final long playerId, final BigDecimal amount) {
		this(0L, playerId, amount);
	}

	/**
	 * Конструирует счёт.
	 *
	 * @param accountId идентификатор счёта.
	 * @param playerId  идентификатор игрока.
	 * @param amount    сумма.
	 */
	public Account(final long accountId, final long playerId, final BigDecimal amount) {
		this.accountId = accountId;
		this.playerId = playerId;
		this.amount = amount;
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
