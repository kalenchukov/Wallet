/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link NoAccessAccountException}.
 */
public class NoAccessAccountExceptionTest {
	/**
	 * Проверка метода {@link NoAccessAccountException#getAccountId()}.
	 */
	@Test
	public void getAccountId() {
		long accountId = 9L;
		NoAccessAccountException noAccessAccountException = new NoAccessAccountException(accountId);

		long actualAccountId = noAccessAccountException.getAccountId();

		assertThat(actualAccountId).isEqualTo(accountId);
	}
}