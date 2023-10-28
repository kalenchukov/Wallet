/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Класс счёта.
 */
@Data
@AllArgsConstructor
public class Account {
	/**
	 * Идентификатор.
	 */
	private long accountId;
	/**
	 * Идентификатор игрока.
	 */
	private long playerId;
	/**
	 * Сумма.
	 */
	private BigDecimal amount;
}
