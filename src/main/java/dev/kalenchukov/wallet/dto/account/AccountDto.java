/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.account;

import lombok.*;

import java.math.BigDecimal;

/**
 * Класс счёта.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
	/**
	 * Идентификатор счёта.
	 */
	private Long accountId;

	/**
	 * Идентификатор игрока.
	 */
	private Long playerId;

	/**
	 * Сумма.
	 */
	private BigDecimal amount;
}
