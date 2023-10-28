/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet.validation.validators;

import dev.kalenchukov.wallet.in.servlet.validation.Validator;

import java.math.BigDecimal;

/**
 * Класс проверки корректности суммы.
 */
public class AmountValidator implements Validator<BigDecimal> {
	/**
	 * {@inheritDoc}
	 *
	 * @param value {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean isValid(final BigDecimal value) {
		return (value != null);
	}
}
