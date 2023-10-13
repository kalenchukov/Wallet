/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link NotFoundOperationException}.
 */
public class NotFoundOperationExceptionTest {
	/**
	 * Проверка метода {@link NotFoundOperationException#getOperationId()}.
	 */
	@Test
	public void getOperationId() {
		long operationId = 9L;
		NotFoundOperationException notFoundOperationException = new NotFoundOperationException(operationId);

		long actualOperationId = notFoundOperationException.getOperationId();

		assertThat(actualOperationId).isEqualTo(operationId);
	}
}