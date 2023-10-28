/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet.validation.validators;

import dev.kalenchukov.wallet.in.servlet.validation.Validator;

/**
 * Класс проверки корректности имени.
 */
public class NameValidator implements Validator<String> {
	/**
	 * {@inheritDoc}
	 *
	 * @param value {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean isValid(final String value) {
		return (value != null && !value.isEmpty() && value.matches("[a-zA-Z]{1,100}"));
	}
}
