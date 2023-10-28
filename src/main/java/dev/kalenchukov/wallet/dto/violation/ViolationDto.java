/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.violation;

import lombok.*;

/**
 * Класс нарушения.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViolationDto {
	/**
	 * Сообщение.
	 */
	private String message;
}
