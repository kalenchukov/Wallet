/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.exceptions;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link OutOfAmountOperationException}.
 */
public class OutOfAmountOperationExceptionTest {
	/**
	 * Проверка метода {@link OutOfAmountOperationException#getAmount()}.
	 */
	@Test
	public void getAmount() {
		BigDecimal amount = BigDecimal.TEN;
		OutOfAmountOperationException outOfAmountOperationException = new OutOfAmountOperationException(amount);

		BigDecimal actualAmount = outOfAmountOperationException.getAmount();

		assertThat(actualAmount).isEqualTo(amount);
	}
}