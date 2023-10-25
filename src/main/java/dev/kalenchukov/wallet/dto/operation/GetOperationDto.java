/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.operation;

import lombok.*;

/**
 * Класс операции.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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
