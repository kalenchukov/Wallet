/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.resources;

import dev.kalenchukov.wallet.in.commands.CommandHandler;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов перечисления {@link CommandType}.
 */
public class CommandTypeTest {
	/**
	 * Проверка метода {@link CommandType#getName()}.
	 */
	@ParameterizedTest
	@EnumSource(CommandType.class)
	public void getName(CommandType commandType) {
		String actual = commandType.getName();

		assertThat(actual).isNotNull();
		assertThat(actual).containsPattern("[a-z]+");
	}

	/**
	 * Проверка метода {@link CommandType#getCommandHandler()}.
	 */
	@ParameterizedTest
	@EnumSource(CommandType.class)
	public void getCommandHandler(CommandType commandType) {
		CommandHandler actual = commandType.getCommandHandler();

		assertThat(actual).isNotNull();
	}

	/**
	 * Класс проверки метода {@link CommandType#findByName(String)}.
	 */
	@Nested
	public class FindByName {
		/**
		 * Проверка метода {@link CommandType#findByName(String)}.
		 */
		@Test
		public void findByName() {
			String name = "actions";
			CommandType expected = CommandType.ACTIONS;

			CommandType actual = CommandType.findByName(name);

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link CommandType#findByName(String)} с {@code null} в качестве имени.
		 */
		@Test
		public void findByNameWithNullName() {
			CommandType actual = CommandType.findByName(null);

			assertThat(actual).isNull();
		}

		/**
		 * Проверка метода {@link CommandType#findByName(String)} с отсутствующем названием команды.
		 */
		@Test
		public void findByNameWithNotFoundName() {
			String name = "qwerty";

			CommandType actual = CommandType.findByName(name);

			assertThat(actual).isNull();
		}
	}
}