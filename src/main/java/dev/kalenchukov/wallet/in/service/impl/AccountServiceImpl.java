/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NegativeAmountOperationException;
import dev.kalenchukov.wallet.exceptions.NoAccessAccountException;
import dev.kalenchukov.wallet.exceptions.NotFoundAccountException;
import dev.kalenchukov.wallet.exceptions.OutOfAmountAccountException;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.repository.AccountRepository;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.type.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс сервиса счетов.
 */
@Service
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
	@Autowired
	public AccountServiceImpl(final AccountRepository accountRepository,
							  final OperationRepository operationRepository) {
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
		return this.accountRepository.save(new Account(0L, playerId, BigDecimal.ZERO));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId  {@inheritDoc}
	 * @param accountId {@inheritDoc}
	 * @param amount    {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundAccountException         {@inheritDoc}
	 * @throws NegativeAmountOperationException {@inheritDoc}
	 * @throws NoAccessAccountException         {@inheritDoc}
	 */
	@Override
	public Operation credit(final long playerId, final long accountId, final BigDecimal amount)
			throws NotFoundAccountException, NoAccessAccountException, NegativeAmountOperationException {
		Objects.requireNonNull(amount);

		Optional<Account> account = this.accountRepository.findById(playerId, accountId);

		if (account.isEmpty()) {
			throw new NotFoundAccountException(accountId);
		}

		if (!Objects.equals(account.get().getPlayerId(), playerId)) {
			throw new NoAccessAccountException(accountId);
		}

		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new NegativeAmountOperationException(amount);
		}

		BigDecimal resultAmount = account.get().getAmount().add(amount);

		if (!this.accountRepository.updateAmount(playerId, accountId, resultAmount)) {
			throw new RuntimeException("Не удалось выполнить пополнение счёта.");
		}

		return this.operationRepository.save(
				new Operation(0L, playerId, accountId, OperationType.CREDIT, amount)
		);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId  {@inheritDoc}
	 * @param accountId {@inheritDoc}
	 * @param amount    {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundAccountException         {@inheritDoc}
	 * @throws OutOfAmountAccountException      {@inheritDoc}
	 * @throws NegativeAmountOperationException {@inheritDoc}
	 * @throws NoAccessAccountException         {@inheritDoc}
	 */
	@Override
	public Operation debit(final long playerId, final long accountId, final BigDecimal amount)
			throws NotFoundAccountException, NoAccessAccountException, NegativeAmountOperationException,
			OutOfAmountAccountException {
		Objects.requireNonNull(amount);

		Optional<Account> account = this.accountRepository.findById(playerId, accountId);

		if (account.isEmpty()) {
			throw new NotFoundAccountException(accountId);
		}

		if (!Objects.equals(account.get().getPlayerId(), playerId)) {
			throw new NoAccessAccountException(accountId);
		}

		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new NegativeAmountOperationException(amount);
		}

		BigDecimal resultAmount = account.get().getAmount().subtract(amount);

		if (resultAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new OutOfAmountAccountException(account.get().getAmount());
		}

		if (!this.accountRepository.updateAmount(playerId, accountId, resultAmount)) {
			throw new RuntimeException("Не удалось выполнить списание со счёта.");
		}

		return this.operationRepository.save(
				new Operation(0L, playerId, accountId, OperationType.DEBIT, amount)
		);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId  {@inheritDoc}
	 * @param accountId {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundAccountException {@inheritDoc}
	 */
	@Override
	public Account findById(final long playerId, final long accountId) throws NotFoundAccountException {
		Optional<Account> account = this.accountRepository.findById(playerId, accountId);
		return account.orElseThrow(() -> new NotFoundAccountException(accountId));
	}
}
