/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.exceptions.ApplicationException;

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
	 * @throws ApplicationException если произошла ошибка при работе с приложением.
	 */
	Account save(Account account);

	/**
	 * Обновляет сумму счёта.
	 *
	 * @param accountId идентификатор счёта.
	 * @param amount    сумма.
	 * @return {@code true} если обновление выполнено, иначе {@code false}.
	 * @throws ApplicationException если произошла ошибка при работе с приложением.
	 */
	boolean updateAmount(long accountId, BigDecimal amount);

	/**
	 * Возвращает счёт.
	 *
	 * @param accountId идентификатор счёта.
	 * @return счёт.
	 * @throws ApplicationException если произошла ошибка при работе с приложением.
	 */
	Optional<Account> findById(long accountId);
}
