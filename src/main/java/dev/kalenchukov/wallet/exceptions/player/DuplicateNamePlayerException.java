/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions.player;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * Класс исключения при дубликате игрока.
 */
@Getter
public class DuplicateNamePlayerException extends PlayerException {
	/**
	 * HTTP-код ответа соответствующий данному исключению.
	 */
	private static final int HTTP_CODE = HttpServletResponse.SC_CONFLICT;

	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Игрок с именем '%1$s' уже существует.";

	/**
	 * Некорректное значение.
	 */
	private final String invalidValue;

	/**
	 * Конструирует исключение.
	 *
	 * @param invalidValue некорректное значение.
	 */
	public DuplicateNamePlayerException(final String invalidValue) {
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
