/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link NotFoundAccountException}.
 */
public class NotFoundAccountExceptionTest {
	/**
	 * Проверка метода {@link NotFoundAccountException#getAccountId()}.
	 */
	@Test
	public void getAccountId() {
		long accountId = 9L;
		NotFoundAccountException notFoundAccountException = new NotFoundAccountException(accountId);

		long actualAccountId = notFoundAccountException.getAccountId();

		assertThat(actualAccountId).isEqualTo(accountId);
	}
}