/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link NotFoundPlayerException}.
 */
public class NotFoundPlayerExceptionTest {
	/**
	 * Проверка метода {@link NotFoundPlayerException#getName()}.
	 */
	@Test
	public void getName() {
		String playerName = "Qwe";
		NotFoundPlayerException notFoundPlayerException = new NotFoundPlayerException(playerName);

		String actualPlayerName = notFoundPlayerException.getName();

		assertThat(actualPlayerName).isEqualTo(playerName);
	}
}