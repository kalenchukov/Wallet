/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при отсутствии игрока.
 */
public class NotFoundPlayerException extends Exception {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Игрок с именем '%1$s', не найден.";

	/**
	 * Ненайденное имя.
	 */
	private final String name;

	/**
	 * Конструирует исключение.
	 *
	 * @param name ненайденное имя.
	 */
	public NotFoundPlayerException(final String name) {
		super(String.format(MESSAGE, name));
		this.name = name;
	}

	/**
	 * Возвращает ненайденное имя.
	 *
	 * @return ненайденное имя.
	 */
	public String getName() {
		return this.name;
	}
}
