/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при дубликате игрока.
 */
public class DuplicatePlayerException extends Exception {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Игрок с именем '%1$s' уже существует.";

	/**
	 * Дублирующее имя.
	 */
	private final String name;

	/**
	 * Конструирует исключение.
	 *
	 * @param name дублирующее имя.
	 */
	public DuplicatePlayerException(final String name) {
		super(String.format(MESSAGE, name));

		this.name = name;
	}

	/**
	 * Возвращает дублирующее имя.
	 *
	 * @return дублирующее имя.
	 */
	public String getName() {
		return this.name;
	}
}
