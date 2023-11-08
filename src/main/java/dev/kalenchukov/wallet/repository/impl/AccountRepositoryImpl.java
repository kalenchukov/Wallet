/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс хранилища счетов.
 */
@Repository
public class AccountRepositoryImpl implements AccountRepository {
	/**
	 * Источник данных.
	 */
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * Конструирует хранилище счетов.
	 *
	 * @param namedParameterJdbcTemplate источник данных.
	 */
	@Autowired
	public AccountRepositoryImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		Objects.requireNonNull(namedParameterJdbcTemplate);
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

		String query = """
				INSERT INTO accounts (player_id, amount)
				VALUES (:player_id, :amount)
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("player_id", account.getPlayerId());
		mapSqlParameterSource.addValue("amount", account.getAmount());

		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.namedParameterJdbcTemplate.update(query, mapSqlParameterSource, keyHolder);
		long accountId = (long) Objects.requireNonNull(keyHolder.getKeys()).get("account_id");

		return new Account(accountId, account.getPlayerId(), account.getAmount());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId  {@inheritDoc}
	 * @param accountId {@inheritDoc}
	 * @param amount    {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean updateAmount(final long playerId, final long accountId, BigDecimal amount) {
		Objects.requireNonNull(amount);

		String query = """
				UPDATE accounts
				SET amount = :amount
				WHERE player_id = :player_id AND account_id = :account_id
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("player_id", playerId);
		mapSqlParameterSource.addValue("account_id", accountId);
		mapSqlParameterSource.addValue("amount", amount);

		int countUpdate = this.namedParameterJdbcTemplate.update(query, mapSqlParameterSource);

		return (countUpdate > 0);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId  {@inheritDoc}
	 * @param accountId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Optional<Account> findById(final long playerId, final long accountId) {
		String query = """
				SELECT *
				FROM accounts
				WHERE player_id = :player_id AND account_id = :account_id
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("player_id", playerId);
		mapSqlParameterSource.addValue("account_id", accountId);

		try {
			return this.namedParameterJdbcTemplate.queryForObject(query, mapSqlParameterSource,
					(rs, row) -> Optional.of(
							new Account(rs.getLong("account_id"),
									rs.getLong("player_id"),
									rs.getBigDecimal("amount")
							))
			);
		} catch (EmptyResultDataAccessException exception) {
			return Optional.empty();
		}
	}
}
