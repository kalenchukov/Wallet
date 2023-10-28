/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.account;

import lombok.*;

/**
 * Класс добавления счёта.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {
	/**
	 * Токен доступа.
	 */
	private String accessToken;
}
