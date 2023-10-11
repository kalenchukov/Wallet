/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

/**
 * Класс исключения при недостаточном количестве параметров для выполнения команды.
 */
public class MissingParametersCommandException extends RuntimeException {
	/**
	 * Текст сообщения.
	 */
	private static final String MESSAGE =
			"Для данной команды количество параметров должно быть больше %1$s.";

	/**
	 * Некорректное количество параметров.
	 */
	private final int countParam;

	/**
	 * Конструирует исключение.
	 *
	 * @param countParam некорректное количество параметров.
	 */
	public MissingParametersCommandException(final int countParam) {
		super(String.format(MESSAGE, countParam));
		this.countParam = countParam;
	}

	/**
	 * Возвращает некорректное количество параметров.
	 *
	 * @return некорректное количество параметров.
	 */
	public int getCountParam() {
		return this.countParam;
	}
}
