/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet.validation.validators;

import dev.kalenchukov.wallet.in.servlet.validation.Validator;

/**
 * Класс проверки корректности идентификатора.
 */
public class IdValidator implements Validator<Long> {
	/**
	 * {@inheritDoc}
	 *
	 * @param value {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean isValid(final Long value) {
		return (value != null && value > 0);
	}
}
