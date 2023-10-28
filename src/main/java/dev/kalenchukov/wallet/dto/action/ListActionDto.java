/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.action;

import lombok.*;

/**
 * Класс списка действий.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListActionDto {
	/**
	 * Токен доступа.
	 */
	private String accessToken;
}
