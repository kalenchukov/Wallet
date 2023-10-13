/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link DuplicatePlayerException}.
 */
public class DuplicatePlayerExceptionTest {
	/**
	 * Проверка метода {@link DuplicatePlayerException#getName()}.
	 */
	@Test
	public void getName() {
		String playerName = "Qwe";
		DuplicatePlayerException duplicatePlayerException = new DuplicatePlayerException(playerName);

		String actualPlayerName = duplicatePlayerException.getName();

		assertThat(actualPlayerName).isEqualTo(playerName);

	}
}