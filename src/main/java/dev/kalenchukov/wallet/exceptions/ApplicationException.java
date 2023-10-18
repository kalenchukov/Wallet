/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при ошибке приложения.
 */
public class ApplicationException extends RuntimeException {
	/**
	 * Конструирует исключение.
	 *
	 * @param cause родительское исключение.
	 */
	public ApplicationException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Конструирует исключение.
	 *
	 * @param message сообщение.
	 */
	public ApplicationException(final String message) {
		super(message);
	}
}
