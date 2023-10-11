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
import dev.kalenchukov.wallet.exceptions.NegativeAmountOperationException;
import dev.kalenchukov.wallet.exceptions.OutOfAmountOperationException;
import dev.kalenchukov.wallet.repository.AccountRepository;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.resources.OperationType;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс сервиса счетов.
 */
public class AccountServiceImpl implements AccountService {
	/**
	 * Хранилище счетов.
	 */
	private final AccountRepository accountRepository;

	/**
	 * Хранилище операций.
	 */
	private final OperationRepository operationRepository;

	/**
	 * Конструирует сервис счетов.
	 *
	 * @param accountRepository   хранилище счетов.
	 * @param operationRepository хранилище операций.
	 */
	public AccountServiceImpl(final AccountRepository accountRepository, final OperationRepository operationRepository) {
		Objects.requireNonNull(accountRepository);
		Objects.requireNonNull(operationRepository);

		this.accountRepository = accountRepository;
		this.operationRepository = operationRepository;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Account add(final long playerId) {
		Account account = new Account(playerId);

		return this.accountRepository.save(account);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param accountId {@inheritDoc}
	 * @param playerId  {@inheritDoc}
	 * @param amount    {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundAccountException         {@inheritDoc}
	 * @throws NegativeAmountOperationException если сумма меньше нуля.
	 * @throws NoAccessAccountException         если счёт принадлежит другому игроку.
	 */
	@Override
	public Operation credit(final long accountId, final long playerId, final BigDecimal amount)
			throws NotFoundAccountException, NoAccessAccountException {
		Objects.requireNonNull(amount);

		Optional<Account> account = this.accountRepository.getById(accountId);

		if (account.isEmpty()) {
			throw new NotFoundAccountException(accountId);
		}

		if (!Objects.equals(account.get().getPlayerId(), playerId)) {
			throw new NoAccessAccountException(accountId);
		}

		account.get().credit(amount);
		Operation operation = new Operation(account.get(), OperationType.CREDIT, amount);

		this.accountRepository.updateAmount(account.get().getAccountId(), account.get().getAmount());
		this.operationRepository.save(operation);

		return operation;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param accountId {@inheritDoc}
	 * @param playerId  {@inheritDoc}
	 * @param amount    {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundAccountException         {@inheritDoc}
	 * @throws OutOfAmountOperationException    если для списания недостаточно средств.
	 * @throws NegativeAmountOperationException если сумма меньше нуля.
	 * @throws NoAccessAccountException         если счёт принадлежит другому игроку.
	 */
	@Override
	public Operation debit(final long accountId, final long playerId, final BigDecimal amount)
			throws NotFoundAccountException, NoAccessAccountException {
		Objects.requireNonNull(amount);

		Optional<Account> account = this.accountRepository.getById(accountId);

		if (account.isEmpty()) {
			throw new NotFoundAccountException(accountId);
		}

		if (!Objects.equals(account.get().getPlayerId(), playerId)) {
			throw new NoAccessAccountException(accountId);
		}

		account.get().debit(amount);
		Operation operation = new Operation(account.get(), OperationType.DEBIT, amount);

		this.accountRepository.updateAmount(account.get().getAccountId(), account.get().getAmount());
		this.operationRepository.save(operation);

		return operation;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param accountId {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundAccountException {@inheritDoc}
	 */
	@Override
	public Account getById(final long accountId) throws NotFoundAccountException {
		Optional<Account> account = this.accountRepository.getById(accountId);

		return account.orElseThrow(() -> new NotFoundAccountException(accountId));
	}
}
