/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс токена доступа.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность токена доступа", accessMode = Schema.AccessMode.READ_ONLY)
public class AccessTokenDto {
	/**
	 * Токен доступа.
	 */
	@Schema(description = "Токен доступа")
	private String accessToken;
}
