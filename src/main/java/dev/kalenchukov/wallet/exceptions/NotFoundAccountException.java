/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при отсутствии счёта.
 */
public class NotFoundAccountException extends Exception {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Счёт с идентификатором '%1$s' не найден.";

	/**
	 * Некорректный идентификатор счёта.
	 */
	private final long accountId;

	/**
	 * Конструирует исключение.
	 *
	 * @param accountId некорректный идентификатор счёта.
	 */
	public NotFoundAccountException(final long accountId) {
		super(String.format(MESSAGE, accountId));
		this.accountId = accountId;
	}

	/**
	 * Возвращает некорректный идентификатор счёта.
	 *
	 * @return некорректный идентификатор счёта.
	 */
	public long getAccountId() {
		return this.accountId;
	}
}
