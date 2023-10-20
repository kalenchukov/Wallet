/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при недостаточном количестве аргументов для выполнения команды.
 */
public class MissingArgsCommandException extends RuntimeException {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Для данной команды количество параметров должно быть больше %1$s.";

	/**
	 * Переданное количество параметров.
	 */
	private final int inputParam;

	/**
	 * Конструирует исключение.
	 *
	 * @param inputParam переданное количество параметров.
	 */
	public MissingArgsCommandException(final int inputParam) {
		super(String.format(MESSAGE, inputParam));
		this.inputParam = inputParam;
	}

	/**
	 * Возвращает переданное количество параметров.
	 *
	 * @return переданное количество параметров.
	 */
	public int getInputParam() {
		return this.inputParam;
	}
}
