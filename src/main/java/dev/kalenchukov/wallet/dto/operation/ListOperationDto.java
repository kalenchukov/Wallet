/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.operation;

import lombok.*;

/**
 * Класс списка операций.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ListOperationDto {
	/**
	 * Идентификатор счёта.
	 */
	private Long accountId;

	/**
	 * Токен доступа.
	 */
	private String accessToken;
}
