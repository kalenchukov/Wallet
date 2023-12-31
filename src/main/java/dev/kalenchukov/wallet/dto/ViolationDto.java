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
 * Класс нарушения.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность нарушения", accessMode = Schema.AccessMode.READ_ONLY)
public class ViolationDto {
	/**
	 * Сообщение.
	 */
	@Schema(description = "Сообщение")
	private String message;
}
