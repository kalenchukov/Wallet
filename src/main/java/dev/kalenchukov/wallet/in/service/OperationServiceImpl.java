/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.NotFoundAccountException;
import dev.kalenchukov.wallet.exceptions.NotFoundOperationException;
import dev.kalenchukov.wallet.repository.AccountRepository;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.resources.OperationType;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Класс сервиса операций.
 */
public class OperationServiceImpl implements OperationService {
	/**
	 * Хранилище операций.
	 */
	private final OperationRepository operationRepository;

	/**
	 * Хранилище счетов.
	 */
	private final AccountRepository accountRepository;

	/**
	 * Конструирует сервис операций.
	 *
	 * @param operationRepository хранилище операций.
	 * @param accountRepository   хранилище счетов.
	 */
	public OperationServiceImpl(final OperationRepository operationRepository,
								final AccountRepository accountRepository) {
		Objects.requireNonNull(operationRepository);
		Objects.requireNonNull(accountRepository);

		this.operationRepository = operationRepository;
		this.accountRepository = accountRepository;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param accountId     {@inheritDoc}
	 * @param operationType {@inheritDoc}
	 * @param amount        {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundAccountException если счёта не существует.
	 */
	@Override
	public Operation add(long accountId, OperationType operationType, BigDecimal amount)
			throws NotFoundAccountException {
		Objects.requireNonNull(operationType);
		Objects.requireNonNull(amount);

		Optional<Account> account = this.accountRepository.getById(accountId);

		if (account.isEmpty()) {
			throw new NotFoundAccountException(accountId);
		}

		Operation operation = new Operation(account.get(), operationType, amount);
		return this.operationRepository.save(operation);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param operationId {@inheritDoc}
	 * @param playerId    {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundOperationException {@inheritDoc}
	 */
	public Operation getById(final long operationId, final long playerId)
			throws NotFoundOperationException {
		Optional<Operation> operation = this.operationRepository.getById(operationId, playerId);

		return operation.orElseThrow(() -> new NotFoundOperationException(operationId));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param accountId {@inheritDoc}
	 * @param playerId  {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Set<Operation> find(final long accountId, final long playerId) {
		return this.operationRepository.find(accountId, playerId);
	}
}
