/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity.mappers;

import dev.kalenchukov.wallet.dto.operation.OperationDto;
import dev.kalenchukov.wallet.entity.Operation;
import org.mapstruct.Mapper;

/**
 * Интерфейс для реализации класса преобразования операции.
 */
@Mapper
public interface OperationMapper {
	/**
	 * Преобразовывает операцию.
	 *
	 * @param operation операция.
	 * @return операция для транспортировки.
	 */
	OperationDto toDto(Operation operation);
}
