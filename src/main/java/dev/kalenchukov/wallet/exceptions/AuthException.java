/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения авторизации.
 */
public class AuthException extends RuntimeException {
	/**
	 * Конструирует исключение.
	 *
	 * @param message сообщение.
	 */
	public AuthException(String message) {
		super(message);
	}
}
