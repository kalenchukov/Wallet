/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller.validation.validators;

import dev.kalenchukov.wallet.in.controller.validation.Validator;

/**
 * Класс проверки корректности токена.
 */
public class AccessTokenValidator implements Validator<String> {
	/**
	 * {@inheritDoc}
	 *
	 * @param value {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean isValid(final String value) {
		return (value != null && !value.isEmpty());
	}
}