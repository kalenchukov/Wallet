/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Класс счёта.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность счёта")
public class AccountDto {
	/**
	 * Идентификатор счёта.
	 */
	@Schema(description = "Идентификатор счёта", example = "66")
	private Long accountId;

	/**
	 * Идентификатор игрока.
	 */
	@Schema(description = "Идентификатор игрока", example = "57")
	private Long playerId;

	/**
	 * Сумма.
	 */
	@Schema(description = "Сумма", example = "17.1")
	private BigDecimal amount;
}
