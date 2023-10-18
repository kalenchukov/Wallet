/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при отсутствии необходимости авторизации.
 */
public class NotNeedAuthException extends AuthException {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Нет необходимости проходить авторизацию.";

	/**
	 * Конструирует исключение.
	 */
	public NotNeedAuthException() {
		super(MESSAGE);
	}
}
