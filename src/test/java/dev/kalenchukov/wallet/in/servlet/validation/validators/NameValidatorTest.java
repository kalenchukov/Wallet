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
 * Класс проверки методов класса {@link NameValidator}.
 */
public class NameValidatorTest {
	/**
	 * Класс проверки метода {@link NameValidator#isValid(String)}.
	 */
	@Nested
	public class IsValid {
		/**
		 * Проверка метода {@link NameValidator#isValid(String)}.
		 */
		@Test
		public void isValid() {
			String name = "ascrsd";
			Validator<String> validator = new NameValidator();

			boolean actual = validator.isValid(name);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link NameValidator#isValid(String)} с пустым значением.
		 */
		@Test
		public void isValidWithEmpty() {
			String name = "";
			Validator<String> validator = new NameValidator();

			boolean actual = validator.isValid(name);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link NameValidator#isValid(String)} с цифрами в значении.
		 */
		@Test
		public void isValidWithNumber() {
			String name = "7sdr7f";
			Validator<String> validator = new NameValidator();

			boolean actual = validator.isValid(name);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link NameValidator#isValid(String)} с превышением символов в значении.
		 */
		@Test
		public void isValidWithMaxLength() {
			String name = "dsferuteruotuerotgueyrnieywifeoutpe" +
					"roporgvpojcifmDReinfgdunsnfucewifyner8" +
					"gotrimhoptrhpbtrhbtrophgoemf";
			Validator<String> validator = new NameValidator();

			boolean actual = validator.isValid(name);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link NameValidator#isValid(String)} с невидимыми символами в качестве значения.
		 */
		@Test
		public void isValidWithWhiteSpace() {
			String name = "\n";
			Validator<String> validator = new NameValidator();

			boolean actual = validator.isValid(name);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link NameValidator#isValid(String)} с {@code null} в качестве значения.
		 */
		@Test
		public void isValidWithNull() {
			String name = null;
			Validator<String> validator = new NameValidator();

			boolean actual = validator.isValid(name);

			assertThat(actual).isFalse();
		}
	}
}