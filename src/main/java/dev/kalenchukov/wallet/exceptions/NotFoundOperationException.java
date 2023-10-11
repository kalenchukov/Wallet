/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при отсутствии операции.
 */
public class NotFoundOperationException extends Exception {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Операция с идентификатором '%1$s' не найдена.";

	/**
	 * Некорректный идентификатор операции.
	 */
	private final long operationId;

	/**
	 * Конструирует исключение.
	 *
	 * @param operationId некорректный идентификатор операции.
	 */
	public NotFoundOperationException(final long operationId) {
		super(String.format(MESSAGE, operationId));
		this.operationId = operationId;
	}

	/**
	 * Возвращает некорректный идентификатор операции.
	 *
	 * @return некорректный идентификатор операции.
	 */
	public long getOperationId() {
		return this.operationId;
	}
}
