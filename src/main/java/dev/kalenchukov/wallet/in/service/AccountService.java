/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.account.NoAccessAccountException;
import dev.kalenchukov.wallet.exceptions.account.NotFoundAccountException;
import dev.kalenchukov.wallet.exceptions.account.OutOfAmountAccountException;
import dev.kalenchukov.wallet.exceptions.operation.NegativeAmountOperationException;

import java.math.BigDecimal;

/**
 * Интерфейс для реализации класса сервиса счетов.
 */
public interface AccountService {
	/**
	 * Добавляет счёт.
	 *
	 * @param playerId идентификатор игрока.
	 * @return счёт.
	 */
	Account add(long playerId);

	/**
	 * Пополняет счёт.
	 *
	 * @param accountId идентификатор счёта.
	 * @param playerId  идентификатор игрока.
	 * @param amount    сумма.
	 * @return операцию.
	 * @throws NotFoundAccountException         если счёт не найден.
	 * @throws NoAccessAccountException         если счёт принадлежит другому игроку.
	 * @throws NegativeAmountOperationException если сумма меньше нуля.
	 * @throws NoAccessAccountException         если счёт принадлежит другому игроку.
	 */
	Operation credit(long accountId, long playerId, BigDecimal amount)
			throws NotFoundAccountException, NoAccessAccountException, NegativeAmountOperationException;

	/**
	 * Списывает со счёта.
	 *
	 * @param accountId идентификатор счёта.
	 * @param playerId  идентификатор игрока.
	 * @param amount    сумма.
	 * @return операцию.
	 * @throws NotFoundAccountException         если счёт не найден.
	 * @throws NoAccessAccountException         если счёт принадлежит другому игроку.
	 * @throws OutOfAmountAccountException      если для списания недостаточно средств.
	 * @throws NegativeAmountOperationException если сумма меньше нуля.
	 */
	Operation debit(long accountId, long playerId, BigDecimal amount)
			throws NotFoundAccountException, NoAccessAccountException, NegativeAmountOperationException, OutOfAmountAccountException;

	/**
	 * Возвращает счёт.
	 *
	 * @param accountId идентификатор счёта.
	 * @return счёт.
	 * @throws NotFoundAccountException если счёт не найден.
	 */
	Account findById(long accountId)
			throws NotFoundAccountException;
}
