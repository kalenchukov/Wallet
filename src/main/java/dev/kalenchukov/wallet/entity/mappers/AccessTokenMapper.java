/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity.mappers;

import dev.kalenchukov.wallet.dto.AccessTokenDto;
import dev.kalenchukov.wallet.entity.AccessToken;
import org.mapstruct.Mapper;

/**
 * Интерфейс для реализации класса преобразования токена доступа.
 */
@Mapper
public interface AccessTokenMapper {
	/**
	 * Преобразовывает токен доступа.
	 *
	 * @param accessToken токен доступа.
	 * @return токен доступа для транспортировки.
	 */
	AccessTokenDto toDto(AccessToken accessToken);
}
