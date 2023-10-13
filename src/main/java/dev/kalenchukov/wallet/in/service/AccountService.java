/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NoAccessAccountException;
import dev.kalenchukov.wallet.exceptions.NotFoundAccountException;

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
	 * @throws NotFoundAccountException если счёт не найден.
	 * @throws NoAccessAccountException если счёт принадлежит другому игроку.
	 */
	Operation credit(long accountId, long playerId, BigDecimal amount)
			throws NotFoundAccountException, NoAccessAccountException;

	/**
	 * Списывает со счёта.
	 *
	 * @param accountId идентификатор счёта.
	 * @param playerId  идентификатор игрока.
	 * @param amount    сумма.
	 * @return операцию.
	 * @throws NotFoundAccountException если счёт не найден.
	 * @throws NoAccessAccountException если счёт принадлежит другому игроку.
	 */
	Operation debit(long accountId, long playerId, BigDecimal amount)
			throws NotFoundAccountException, NoAccessAccountException;

	/**
	 * Возвращает счёт.
	 *
	 * @param accountId идентификатор счёта.
	 * @return счёт.
	 * @throws NotFoundAccountException если счёт не найден.
	 */
	Account getById(long accountId)
			throws NotFoundAccountException;
}
