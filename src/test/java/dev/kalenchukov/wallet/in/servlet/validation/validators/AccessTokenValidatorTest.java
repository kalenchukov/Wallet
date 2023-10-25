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
 * Класс проверки методов класса {@link AccessTokenValidator}.
 */
public class AccessTokenValidatorTest {
	/**
	 * Класс проверки метода {@link AccessTokenValidator#isValid(String)}.
	 */
	@Nested
	public class IsValid {
		/**
		 * Проверка метода {@link AccessTokenValidator#isValid(String)}.
		 */
		@Test
		public void isValid() {
			String token = "ascrsd";
			Validator<String> validator = new AccessTokenValidator();

			boolean actual = validator.isValid(token);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link AccessTokenValidator#isValid(String)} с пустым значением.
		 */
		@Test
		public void isValidWithEmpty() {
			String token = "";
			Validator<String> validator = new AccessTokenValidator();

			boolean actual = validator.isValid(token);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link AccessTokenValidator#isValid(String)} с {@code null} в качестве значения.
		 */
		@Test
		public void isValidWithNull() {
			String token = null;
			Validator<String> validator = new AccessTokenValidator();

			boolean actual = validator.isValid(token);

			assertThat(actual).isFalse();
		}
	}
}