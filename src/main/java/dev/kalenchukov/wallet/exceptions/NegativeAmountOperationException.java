/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

/**
 * Класс исключения при отрицательной сумме операции.
 */
@Getter
public class NegativeAmountOperationException extends ApplicationException {
	/**
	 * HTTP-код ответа соответствующий данному исключению.
	 */
	private static final HttpStatus HTTP_CODE = HttpStatus.BAD_REQUEST;

	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Сумма должна быть больше или равна 0 а не '%1$s'.";

	/**
	 * Некорректное значение.
	 */
	private final String invalidValue;

	/**
	 * Конструирует исключение.
	 *
	 * @param invalidValue некорректное значение.
	 */
	public NegativeAmountOperationException(final BigDecimal invalidValue) {
		super(String.format(MESSAGE, invalidValue), HTTP_CODE);
		this.invalidValue = String.valueOf(invalidValue);
	}

	/**
	 * Возвращает HTTP-код.
	 *
	 * @return HTTP-код.
	 */
	@Override
	public HttpStatus getHttpCode() {
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
