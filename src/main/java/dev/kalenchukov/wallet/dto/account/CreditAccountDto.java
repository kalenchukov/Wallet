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
 * Класс пополнения счёта.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность пополнения счёта")
public class CreditAccountDto {
	/**
	 * Сумма.
	 */
	@Schema(description = "Сумма", example = "10.7")
	private BigDecimal amount;
}
