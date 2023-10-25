/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet.validation.validators;

import dev.kalenchukov.wallet.in.servlet.validation.Validator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс проверки методов класса {@link IdValidator}.
 */
public class IdValidatorTest {
	/**
	 * Класс проверки метода {@link IdValidator#isValid(Long)}.
	 */
	@Nested
	public class IsValid {
		/**
		 * Проверка метода {@link IdValidator#isValid(Long)}.
		 */
		@Test
		public void isValid() {
			Long id = 5646L;
			Validator<Long> validator = new IdValidator();

			boolean actual = validator.isValid(id);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link IdValidator#isValid(Long)} с {@code null} в качестве значения.
		 */
		@Test
		public void isValidWithNull() {
			Long id = null;
			Validator<Long> validator = new IdValidator();

			boolean actual = validator.isValid(id);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link IdValidator#isValid(Long)} с отрицательным значением.
		 */
		@Test
		public void isValidWithNegative() {
			Long id = -123L;
			Validator<Long> validator = new IdValidator();

			boolean actual = validator.isValid(id);

			assertThat(actual).isFalse();
		}
	}
}