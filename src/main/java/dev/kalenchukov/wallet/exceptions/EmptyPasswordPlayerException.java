/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при пустом пароле игрока.
 */
public class EmptyPasswordPlayerException extends RuntimeException {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Пароль не должен быть пустым.";

	/**
	 * Конструирует исключение.
	 */
	public EmptyPasswordPlayerException() {
		super(MESSAGE);
	}
}
