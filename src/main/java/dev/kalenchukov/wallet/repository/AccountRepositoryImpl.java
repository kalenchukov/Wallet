/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Account;

import java.math.BigDecimal;
import java.util.*;

/**
 * Класс хранилища счетов.
 */
public class AccountRepositoryImpl implements AccountRepository {
	/**
	 * Хранилище счетов.
	 */
	private static Set<Account> DATA = new HashSet<>();

	/**
	 * Конструирует хранилище счетов.
	 */
	public AccountRepositoryImpl() {
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param account {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Account save(final Account account) {
		Objects.requireNonNull(account);
		DATA.add(account);
		return account;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param accountId {@inheritDoc}
	 * @param amount    {@inheritDoc}
	 */
	@Override
	public void updateAmount(final long accountId, BigDecimal amount) {
		Objects.requireNonNull(amount);
		Optional<Account> account = DATA.stream()
				.filter(elm -> Objects.equals(elm.getAccountId(), accountId))
				.findFirst();
		account.ifPresent(value -> value.setAmount(amount));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param accountId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Optional<Account> getById(final long accountId) {
		return DATA.stream()
				.filter(elm -> Objects.equals(elm.getAccountId(), accountId))
				.findFirst();
	}
}
