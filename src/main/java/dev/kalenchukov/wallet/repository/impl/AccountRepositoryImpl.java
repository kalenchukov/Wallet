/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
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
	private final DataSource dataSource;

	/**
	 * Конструирует хранилище счетов.
	 *
	 * @param dataSource источник данных.
	 */
	@Autowired
	public AccountRepositoryImpl(final DataSource dataSource) {
		Objects.requireNonNull(dataSource);
		this.dataSource = dataSource;
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

		String query = "INSERT INTO accounts (player_id, amount) VALUES (?, ?)";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setLong(1, account.getPlayerId());
			preparedStatement.setBigDecimal(2, account.getAmount());
			preparedStatement.executeUpdate();

			try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
				resultSet.next();
				long accountId = resultSet.getLong(1);

				return new Account(
						accountId,
						account.getPlayerId(),
						account.getAmount()
				);
			}
		} catch (SQLException exception) {
			throw new RuntimeException("Возникла ошибка при работе с базой данных");
		}
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

		String query = "UPDATE accounts SET amount = ? WHERE player_id = ? AND account_id = ?";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setBigDecimal(1, amount);
			preparedStatement.setLong(2, playerId);
			preparedStatement.setLong(3, accountId);
			preparedStatement.executeUpdate();

			try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
				return resultSet.next();
			}
		} catch (SQLException exception) {
			throw new RuntimeException("Возникла ошибка при работе с базой данных");
		}
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
		Optional<Account> account = Optional.empty();
		String query = "SELECT * FROM accounts WHERE player_id = ? AND account_id = ?";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setLong(1, playerId);
			preparedStatement.setLong(2, accountId);
			preparedStatement.execute();

			try (ResultSet resultSet = preparedStatement.getResultSet()) {
				if (resultSet.next()) {
					Account accountEntity = new Account(
							resultSet.getLong("account_id"),
							resultSet.getLong("player_id"),
							resultSet.getBigDecimal("amount")
					);

					account = Optional.of(accountEntity);
				}
			}
		} catch (SQLException exception) {
			throw new RuntimeException("Возникла ошибка при работе с базой данных");
		}

		return account;
	}
}
