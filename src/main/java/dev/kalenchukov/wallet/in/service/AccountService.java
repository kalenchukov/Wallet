/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NegativeAmountOperationException;
import dev.kalenchukov.wallet.exceptions.NoAccessAccountException;
import dev.kalenchukov.wallet.exceptions.NotFoundAccountException;
import dev.kalenchukov.wallet.exceptions.OutOfAmountAccountException;

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
	 * @param playerId  идентификатор игрока.
	 * @param accountId идентификатор счёта.
	 * @param amount    сумма.
	 * @return операцию.
	 * @throws NotFoundAccountException         если счёт не найден.
	 * @throws NoAccessAccountException         если счёт принадлежит другому игроку.
	 * @throws NegativeAmountOperationException если сумма меньше нуля.
	 * @throws NoAccessAccountException         если счёт принадлежит другому игроку.
	 */
	Operation credit(long playerId, long accountId, BigDecimal amount)
			throws NotFoundAccountException, NoAccessAccountException, NegativeAmountOperationException;

	/**
	 * Списывает со счёта.
	 *
	 * @param playerId  идентификатор игрока.
	 * @param accountId идентификатор счёта.
	 * @param amount    сумма.
	 * @return операцию.
	 * @throws NotFoundAccountException         если счёт не найден.
	 * @throws NoAccessAccountException         если счёт принадлежит другому игроку.
	 * @throws OutOfAmountAccountException      если для списания недостаточно средств.
	 * @throws NegativeAmountOperationException если сумма меньше нуля.
	 */
	Operation debit(long playerId, long accountId, BigDecimal amount)
			throws NotFoundAccountException, NoAccessAccountException, NegativeAmountOperationException,
			OutOfAmountAccountException;

	/**
	 * Возвращает счёт.
	 *
	 * @param playerId  идентификатор игрока.
	 * @param accountId идентификатор счёта.
	 * @return счёт.
	 * @throws NotFoundAccountException если счёт не найден.
	 */
	Account findById(long playerId, long accountId) throws NotFoundAccountException;
}
