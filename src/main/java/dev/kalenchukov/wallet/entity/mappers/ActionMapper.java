/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity.mappers;

import dev.kalenchukov.starter.fixaction.entity.Action;
import dev.kalenchukov.wallet.dto.ActionDto;
import org.mapstruct.Mapper;

/**
 * Интерфейс для реализации класса преобразования действия.
 */
@Mapper
public interface ActionMapper {
	/**
	 * Преобразовывает действие.
	 *
	 * @param action действие.
	 * @return действие для транспортировки.
	 */
	ActionDto toDto(Action action);
}
