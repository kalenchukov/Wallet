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
 * Класс снятия со счёта.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность списания со счёта")
public class DebitAccountDto {
	/**
	 * Сумма.
	 */
	@Schema(description = "Сумма", example = "78.9")
	private BigDecimal amount;
}
