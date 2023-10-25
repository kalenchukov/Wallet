/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.account;

import lombok.*;

import java.math.BigDecimal;

/**
 * Класс снятия со счёта.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DebitAccountDto {
	/**
	 * Идентификатор счёта.
	 */
	private Long accountId;

	/**
	 * Сумма.
	 */
	private BigDecimal amount;

	/**
	 * Токен доступа.
	 */
	private String accessToken;
}
