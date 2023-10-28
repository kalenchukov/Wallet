/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions.player;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * Класс исключения при необходимости авторизации.
 */
@Getter
public class NeedAuthPlayerException extends PlayerException {
	/**
	 * HTTP-код ответа соответствующий данному исключению.
	 */
	private static final int HTTP_CODE = HttpServletResponse.SC_UNAUTHORIZED;

	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Необходимо пройти авторизацию.";

	/**
	 * Некорректное значение.
	 */
	private final String invalidValue;

	/**
	 * Конструирует исключение.
	 */
	public NeedAuthPlayerException() {
		super(MESSAGE, HTTP_CODE);
		this.invalidValue = null;
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
