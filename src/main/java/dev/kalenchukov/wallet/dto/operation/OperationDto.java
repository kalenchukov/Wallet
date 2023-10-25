/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.operation;

import dev.kalenchukov.wallet.type.OperationType;
import lombok.*;

import java.math.BigDecimal;

/**
 * Класс операции.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OperationDto {
	/**
	 * Идентификатор.
	 */
	private Long operationId;

	/**
	 * Идентификатор счёта.
	 */
	private Long accountId;

	/**
	 * Тип.
	 */
	private OperationType operationType;

	/**
	 * Сумма.
	 */
	private BigDecimal amount;
}
