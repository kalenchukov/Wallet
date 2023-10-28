/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions.account;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * Класс исключения при отсутствии доступа к счёту.
 */
@Getter
public class NoAccessAccountException extends AccountException {
	/**
	 * HTTP-код ответа соответствующий данному исключению.
	 */
	private static final int HTTP_CODE = HttpServletResponse.SC_FORBIDDEN;

	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Нет доступа к счёту с идентификатором '%1$s'.";

	/**
	 * Некорректное значение.
	 */
	private final String invalidValue;

	/**
	 * Конструирует исключение.
	 *
	 * @param invalidValue некорректное значение.
	 */
	public NoAccessAccountException(final Long invalidValue) {
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
