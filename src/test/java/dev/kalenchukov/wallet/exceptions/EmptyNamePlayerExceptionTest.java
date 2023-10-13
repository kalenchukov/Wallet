/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link EmptyNamePlayerException}.
 */
public class EmptyNamePlayerExceptionTest {
	/**
	 * Проверка метода {@link EmptyNamePlayerException#getName()}.
	 */
	@Test
	public void getName() {
		String playerName = "";
		EmptyNamePlayerException emptyNamePlayerException = new EmptyNamePlayerException(playerName);

		String actualPlayerName = emptyNamePlayerException.getName();

		assertThat(actualPlayerName).isEqualTo(playerName);
	}
}