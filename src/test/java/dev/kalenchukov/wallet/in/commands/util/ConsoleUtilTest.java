/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.util;

import dev.kalenchukov.wallet.entity.Player;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Класс проверки методов класса {@link ConsoleUtil}.
 */
public class ConsoleUtilTest {
	/**
	 * Класс проверки статических методов класса {@link ConsoleUtil}.
	 */
	@Nested
	public class Static {
		/**
		 * Класс проверки метода {@link ConsoleUtil#getDecorator(Player)}.
		 */
		@Nested
		public class GetDecorator {
			/**
			 * Проверка метода {@link ConsoleUtil#getDecorator(Player)}.
			 */
			@Test
			public void getDecorator() {
				String name = "roma";
				Player player = mock(Player.class);
				when(player.getName()).thenReturn(name);

				String actual = ConsoleUtil.getDecorator(player);

				assertThat(actual).isEqualTo("\n" + name + "> ");
			}

			/**
			 * Проверка метода {@link ConsoleUtil#getDecorator(Player)} с {@code null} в качестве игрока.
			 */
			@Test
			public void getDecoratorWithNullPlayer() {
				Player player = null;

				String actual = ConsoleUtil.getDecorator(player);

				assertThat(actual).isEqualTo("\n> ");
			}
		}
	}
}