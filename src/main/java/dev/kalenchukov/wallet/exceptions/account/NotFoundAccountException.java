/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions.account;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * Класс исключения при отсутствии счёта.
 */
@Getter
public class NotFoundAccountException extends AccountException {
	/**
	 * HTTP-код ответа соответствующий данному исключению.
	 */
	private static final int HTTP_CODE = HttpServletResponse.SC_NOT_FOUND;

	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Счёт с идентификатором '%1$s' не найден.";

	/**
	 * Некорректное значение.
	 */
	private final String invalidValue;

	/**
	 * Конструирует исключение.
	 *
	 * @param invalidValue некорректное значение.
	 */
	public NotFoundAccountException(final Long invalidValue) {
		super(String.format(MESSAGE, invalidValue), HTTP_CODE);
		this.invalidValue = String.valueOf(invalidValue);
	}

	/**
	 * Возвращает HTTP-код.
	 *
	 * @return HTTP-код.
	 */
	@Override
	public int getHttpCode() {
		return HTTP_CODE;
	}

	/**
	 * Возвращает сообщение.
	 *
	 * @return сообщение.
	 */
	@Override
	public String getMessage() {
		return String.format(MESSAGE, invalidValue);
	}
}