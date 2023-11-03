/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller.validation;

/**
 * Интерфейс для реализации класса проверки корректности значения.
 *
 * @param <T> тип проверяемого значения.
 */
public interface Validator<T> {
	/**
	 * Проверяет значение на соответствие требованиям.
	 *
	 * @param value значение.
	 * @return {@code true} если значение удовлетворяет требованиям, иначе {@code false}.
	 */
	boolean isValid(T value);
}
