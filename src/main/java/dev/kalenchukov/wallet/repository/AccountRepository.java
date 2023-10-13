/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Account;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Интерфейс для реализации класса хранилища счетов.
 */
public interface AccountRepository {
	/**
	 * Сохраняет счёт.
	 *
	 * @param account счёт.
	 * @return счёт.
	 */
	Account save(Account account);

	/**
	 * Обновляет сумму счёта.
	 *
	 * @param accountId идентификатор счёта.
	 * @param amount    сумма.
	 */
	void updateAmount(long accountId, BigDecimal amount);

	/**
	 * Возвращает счёт.
	 *
	 * @param accountId идентификатор счёта.
	 * @return счёт.
	 */
	Optional<Account> getById(long accountId);
}
