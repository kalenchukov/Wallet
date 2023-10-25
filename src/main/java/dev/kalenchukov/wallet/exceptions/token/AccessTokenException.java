/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions.token;

import dev.kalenchukov.wallet.exceptions.ApplicationException;
import lombok.Getter;

/**
 * Класс исключения токена доступа.
 */
@Getter
public class AccessTokenException extends ApplicationException {
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
	public AccessTokenException(final String message, final int httpCode) {
		super(message, httpCode);
		this.message = message;
		this.httpCode = httpCode;
	}
}
