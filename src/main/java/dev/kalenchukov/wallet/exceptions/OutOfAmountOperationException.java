/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import java.math.BigDecimal;

/**
 * Класс исключения при превышении суммы операции.
 */
public class OutOfAmountOperationException extends RuntimeException {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Недостаточно средств на счёте. У вас сейчас %1$s.";

	/**
	 * Некорректная сумма.
	 */
	private final BigDecimal amount;

	/**
	 * Конструирует исключение.
	 *
	 * @param amount некорректная сумма.
	 */
	public OutOfAmountOperationException(final BigDecimal amount) {
		super(String.format(MESSAGE, amount));

		this.amount = amount;
	}

	/**
	 * Возвращает некорректную сумму.
	 *
	 * @return некорректную сумму.
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}
}
