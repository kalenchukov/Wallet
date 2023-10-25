/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.token;

import lombok.*;

/**
 * Класс токена доступа.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccessTokenDto {
	/**
	 * Токен доступа.
	 */
	private String accessToken;
}
