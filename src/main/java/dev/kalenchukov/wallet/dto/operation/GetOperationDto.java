/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.operation;

import lombok.*;

/**
 * Класс получения информации об операции.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOperationDto {
	/**
	 * Идентификатор.
	 */
	private Long operationId;

	/**
	 * Токен доступа.
	 */
	private String accessToken;
}