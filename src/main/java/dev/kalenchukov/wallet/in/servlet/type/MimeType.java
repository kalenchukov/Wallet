/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet.type;

import lombok.Getter;

/**
 * Перечисление MIME-типов.
 */
@Getter
public enum MimeType {
	/**
	 * Тип JSON.
	 */
	JSON("application/json");

	/**
	 * Значение.
	 */
	private final String value;

	/**
	 * Конструирует перечисление MIME-типов.
	 *
	 * @param value значение.
	 */
	MimeType(final String value) {
		this.value = value;
	}
}
