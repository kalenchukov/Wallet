/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto.action;

import dev.kalenchukov.wallet.type.ActionType;
import lombok.*;

/**
 * Класс действий.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ActionDto {
	/**
	 * Идентификатор.
	 */
	private Long actionId;

	/**
	 * Тип.
	 */
	private ActionType actionType;

	/**
	 * Статус.
	 */
	private ActionType.Status actionTypeStatus;
}
