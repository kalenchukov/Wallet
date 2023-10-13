/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при пустом имени игрока.
 */
public class EmptyNamePlayerException extends RuntimeException {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Имя не должно быть пустым, а вы указали '%1$s'.";

	/**
	 * Некорректное имя.
	 */
	private final String name;

	/**
	 * Конструирует исключение.
	 *
	 * @param name некорректное имя.
	 */
	public EmptyNamePlayerException(final String name) {
		super(String.format(MESSAGE, name));
		this.name = name;
	}

	/**
	 * Возвращает некорректное имя.
	 *
	 * @return некорректное имя.
	 */
	public String getName() {
		return this.name;
	}
}
