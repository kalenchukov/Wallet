/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при необходимости авторизации.
 */
public class NeedAuthException extends AuthException {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Для выполнения данной команды необходимо пройти авторизацию.";

	/**
	 * Конструирует исключение.
	 */
	public NeedAuthException() {
		super(MESSAGE);
	}
}
