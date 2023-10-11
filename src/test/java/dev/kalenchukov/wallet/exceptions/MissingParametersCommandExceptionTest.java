/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link MissingParametersCommandException}.
 */
public class MissingParametersCommandExceptionTest {
	/**
	 * Проверка метода {@link MissingParametersCommandException#getCountParam()}.
	 */
	@Test
	public void getCountParam() {
		int countParam = 2;
		MissingParametersCommandException missingParametersCommandException = new MissingParametersCommandException(countParam);

		long actualCountParam = missingParametersCommandException.getCountParam();

		assertThat(actualCountParam).isEqualTo(countParam);
	}
}