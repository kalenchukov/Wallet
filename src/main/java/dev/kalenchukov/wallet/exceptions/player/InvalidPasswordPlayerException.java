/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions.player;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * Класс исключения при некорректном пароле игрока.
 */
@Getter
public class InvalidPasswordPlayerException extends PlayerException {
	/**
	 * HTTP-код ответа соответствующий данному исключению.
	 */
	private static final int HTTP_CODE = HttpServletResponse.SC_BAD_REQUEST;

	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Пароль '%1$s' является некорректным.";

	/**
	 * Некорректное значение.
	 */
	private final String invalidValue;

	/**
	 * Конструирует исключение.
	 *
	 * @param invalidValue некорректное значение.
	 */
	public InvalidPasswordPlayerException(final String invalidValue) {
		super(String.format(MESSAGE, invalidValue), HTTP_CODE);
		this.invalidValue = invalidValue;
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
