/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.operation;

import dev.kalenchukov.wallet.type.OperationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Класс операции.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность операции")
public class OperationDto {
	/**
	 * Идентификатор операции.
	 */
	@Schema(description = "Идентификатор операции", example = "31")
	private Long operationId;

	/**
	 * Идентификатор счёта.
	 */
	@Schema(description = "Идентификатор счёта", example = "19")
	private Long accountId;

	/**
	 * Идентификатор игрока.
	 */
	@Schema(description = "Идентификатор игрока", example = "14")
	private Long playerId;

	/**
	 * Тип.
	 */
	@Schema(description = "Тип операции", example = "CREDIT")
	private OperationType operationType;

	/**
	 * Сумма.
	 */
	@Schema(description = "Сумма операции", example = "6.0")
	private BigDecimal amount;
}
