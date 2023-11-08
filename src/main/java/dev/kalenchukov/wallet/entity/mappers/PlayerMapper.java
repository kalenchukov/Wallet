/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity.mappers;

import dev.kalenchukov.wallet.dto.PlayerDto;
import dev.kalenchukov.wallet.entity.Player;
import org.mapstruct.Mapper;

/**
 * Интерфейс для реализации класса преобразования игрока.
 */
@Mapper
public interface PlayerMapper {
	/**
	 * Преобразовывает игрока.
	 *
	 * @param player игрок.
	 * @return игрок для транспортировки.
	 */
	PlayerDto toDto(Player player);
}
