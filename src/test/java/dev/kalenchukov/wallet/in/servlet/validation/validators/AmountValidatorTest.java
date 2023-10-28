/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet.validation.validators;

import dev.kalenchukov.wallet.in.servlet.validation.Validator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link AmountValidator}.
 */
public class AmountValidatorTest {
	/**
	 * Класс проверки метода {@link AmountValidator#isValid(BigDecimal)}.
	 */
	@Nested
	public class IsValid {
		/**
		 * Проверка метода {@link AmountValidator#isValid(BigDecimal)}.
		 */
		@Test
		public void isValid() {
			BigDecimal amount = BigDecimal.TEN;
			Validator<BigDecimal> validator = new AmountValidator();

			boolean actual = validator.isValid(amount);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link AmountValidator#isValid(BigDecimal)} с {@code null} в качестве значения.
		 */
		@Test
		public void isValidWithNull() {
			BigDecimal amount = null;
			Validator<BigDecimal> validator = new AmountValidator();

			boolean actual = validator.isValid(amount);

			assertThat(actual).isFalse();
		}
	}
}