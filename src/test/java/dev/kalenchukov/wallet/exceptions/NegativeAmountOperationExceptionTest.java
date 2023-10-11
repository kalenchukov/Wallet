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
 * Класс проверки методов класса {@link NegativeAmountOperationException}.
 */
public class NegativeAmountOperationExceptionTest {
	/**
	 * Проверка метода {@link NegativeAmountOperationException#getAmount()}.
	 */
	@Test
	public void getAmount() {
		BigDecimal amount = BigDecimal.valueOf(-10.90);
		NegativeAmountOperationException negativeAmountOperationException = new NegativeAmountOperationException(amount);

		BigDecimal actualAmount = negativeAmountOperationException.getAmount();

		assertThat(actualAmount).isEqualTo(amount);
	}
}