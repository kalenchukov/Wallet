/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands;

import java.io.PrintStream;

/**
 * Интерфейс для реализации обработчика команды.
 */
public interface CommandHandler {
	/**
	 * Выполняет команду.
	 *
	 * @param data   входящие данные.
	 * @param output исходящий поток данных.
	 */
	void execute(String[] data, PrintStream output);
}
