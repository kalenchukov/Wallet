/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity.mappers;

import dev.kalenchukov.wallet.dto.account.AccountDto;
import dev.kalenchukov.wallet.entity.Account;
import org.mapstruct.Mapper;

/**
 * Интерфейс для реализации класса преобразования счёта.
 */
@Mapper
public interface AccountMapper {
	/**
	 * Преобразовывает счёт.
	 *
	 * @param account счёт.
	 * @return счёт для транспортировки.
	 */
	AccountDto toDto(Account account);
}
