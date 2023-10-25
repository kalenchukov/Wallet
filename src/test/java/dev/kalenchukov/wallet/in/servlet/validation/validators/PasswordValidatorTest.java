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
 * Класс проверки методов класса {@link PasswordValidator}.
 */
public class PasswordValidatorTest {
	/**
	 * Класс проверки метода {@link PasswordValidator#isValid(String)}.
	 */
	@Nested
	public class IsValid {
		/**
		 * Проверка метода {@link PasswordValidator#isValid(String)}.
		 */
		@Test
		public void isValid() {
			String password = "ascrsd";
			Validator<String> validator = new PasswordValidator();

			boolean actual = validator.isValid(password);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link PasswordValidator#isValid(String)} с пустым значением.
		 */
		@Test
		public void isValidWithEmpty() {
			String password = "";
			Validator<String> validator = new PasswordValidator();

			boolean actual = validator.isValid(password);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link PasswordValidator#isValid(String)} с превышением символов в значении.
		 */
		@Test
		public void isValidWithMaxLength() {
			String password = "dsferuteruotuerotgueyrnieywifeoutpe" +
					"roporgvpojcifmDReinfgdunsnfucewifyner8" +
					"gotrimhoptrhpbtrhbtrophgoemf";
			Validator<String> validator = new PasswordValidator();

			boolean actual = validator.isValid(password);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link PasswordValidator#isValid(String)} с {@code null} в качестве значения.
		 */
		@Test
		public void isValidWithNull() {
			String password = null;
			Validator<String> validator = new PasswordValidator();

			boolean actual = validator.isValid(password);

			assertThat(actual).isFalse();
		}
	}
}