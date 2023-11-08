/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
	@NotNull(message = "Сумма не должна быть null.")
	@DecimalMin(value = "0.0", message = "Сумма не должна быть меньше нуля.")
	private BigDecimal amount;
}
