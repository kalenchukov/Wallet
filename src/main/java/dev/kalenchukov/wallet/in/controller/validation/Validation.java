/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller.validation;

import java.util.Objects;

/**
 * Класс проверки значений с помощью проверяющих.
 */
public class Validation {
	/**
	 * Проверяет значение на соответствие требованиям проверяющего.
	 *
	 * @param value     проверяемое значение.
	 * @param validator проверяющий.
	 * @param <T>       тип проверяемого значения.
	 * @return {@code true} если значение удовлетворяет требованиям проверяющего, иначе {@code false}.
	 */
	public static <T> boolean isValid(final T value, final Validator<T> validator) {
		Objects.requireNonNull(validator);
		return validator.isValid(value);
	}
}
