/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link MissingArgsCommandException}.
 */
public class MissingArgsCommandExceptionTest {
	/**
	 * Проверка метода {@link MissingArgsCommandException#getInputParam()}.
	 */
	@Test
	public void getCountParam() {
		int countParam = 2;
		MissingArgsCommandException missingArgsCommandException = new MissingArgsCommandException(countParam);

		long actualCountParam = missingArgsCommandException.getInputParam();

		assertThat(actualCountParam).isEqualTo(countParam);
	}
}