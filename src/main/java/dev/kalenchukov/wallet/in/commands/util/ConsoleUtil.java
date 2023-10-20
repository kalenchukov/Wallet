/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.util;

import dev.kalenchukov.wallet.entity.Player;

import java.util.Objects;

/**
 * Класс утилит консоли.
 */
public final class ConsoleUtil {
	/**
	 * Возвращает декоратор командной строки.
	 *
	 * @param player игрок.
	 * @return декоратор командной строки.
	 */
	public static String getDecorator(final Player player) {
		StringBuilder decorator = new StringBuilder();
		decorator.append("\n");
		if (Objects.nonNull(player)) {
			decorator.append(player.getName());
		}
		decorator.append("> ");
		return decorator.toString();
	}
}
