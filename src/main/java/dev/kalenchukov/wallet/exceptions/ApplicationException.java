/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import lombok.Getter;

/**
 * Класс исключения при ошибке приложения.
 */
@Getter
public class ApplicationException extends Exception {
	/**
	 * HTTP-код ответа соответствующий данному исключению.
	 */
	private final int httpCode;

	/**
	 * Текст сообщения.
	 */
	private final String message;

	/**
	 * Конструирует исключение.
	 *
	 * @param message  сообщение.
	 * @param httpCode HTTP-код.
	 */
	public ApplicationException(final String message, final int httpCode) {
		super(message);
		this.message = message;
		this.httpCode = httpCode;
	}
}
