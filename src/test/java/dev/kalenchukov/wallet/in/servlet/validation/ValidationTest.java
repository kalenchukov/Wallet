/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet.validation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link Validation}.
 */
public class ValidationTest {
	/**
	 * Класс проверки статических методов класса {@link Validation}.
	 */
	@Nested
	public class Static {
		/**
		 * Проверка метода {@link Validation#isValid(Object, Validator)}.
		 */
		@Test
		public void isValid() {
			String value = "значение";
			Validator<String> validator = mock(Validator.class);
			when(validator.isValid(anyString())).thenReturn(true);

			boolean actual = Validation.isValid(value, validator);

			assertThat(actual).isTrue();
		}
	}
}