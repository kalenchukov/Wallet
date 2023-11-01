/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.type.OperationType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Класс хранилища операций.
 */
public class OperationRepositoryImpl implements OperationRepository {
	/**
	 * Источник данных.
	 */
	private final DataSource dataSource;

	/**
	 * Конструирует хранилище операций.
	 *
	 * @param dataSource источник данных.
	 */
//	@Autowired
	public OperationRepositoryImpl(final DataSource dataSource) {
		Objects.requireNonNull(dataSource);
		this.dataSource = dataSource;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param operation {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Operation save(final Operation operation) {
		Objects.requireNonNull(operation);

		String query = "INSERT INTO operations (player_id, account_id, type, amount) VALUES (?, ?, ?, ?)";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setLong(1, operation.getPlayerId());
			preparedStatement.setLong(2, operation.getAccountId());
			preparedStatement.setString(3, operation.getOperationType().name());
			preparedStatement.setBigDecimal(4, operation.getAmount());
			preparedStatement.executeUpdate();

			try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
				resultSet.next();
				long operationId = resultSet.getLong(1);

				return new Operation(
						operationId,
						operation.getPlayerId(),
						operation.getAccountId(),
						operation.getOperationType(),
						operation.getAmount()
				);
			}
		} catch (SQLException exception) {
			throw new RuntimeException("Возникла ошибка при работе с базой данных");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param operationId {@inheritDoc}
	 * @param accountId   {@inheritDoc}
	 * @param playerId    {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	public Optional<Operation> findById(final long playerId, final long accountId, final long operationId) {
		Optional<Operation> operation = Optional.empty();
		String query = "SELECT * FROM operations WHERE player_id = ? AND account_id = ? AND operation_id = ?";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setLong(1, playerId);
			preparedStatement.setLong(2, accountId);
			preparedStatement.setLong(3, operationId);
			preparedStatement.execute();

			try (ResultSet resultSet = preparedStatement.getResultSet()) {
				if (resultSet.next()) {
					Operation operationEntity = new Operation(
							resultSet.getLong("operation_id"),
							resultSet.getLong("player_id"),
							resultSet.getLong("account_id"),
							OperationType.valueOf(resultSet.getString("type")),
							resultSet.getBigDecimal("amount")
					);

					operation = Optional.of(operationEntity);
				}
			}
		} catch (SQLException exception) {
			throw new RuntimeException("Возникла ошибка при работе с базой данных");
		}

		return operation;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId  {@inheritDoc}
	 * @param accountId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public List<Operation> find(final long playerId, final long accountId) {
		List<Operation> operations = new ArrayList<>();
		String query = "SELECT * FROM operations WHERE player_id = ? AND account_id = ? ORDER BY operation_id DESC";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setLong(1, playerId);
			preparedStatement.setLong(2, accountId);
			preparedStatement.execute();

			try (ResultSet resultSet = preparedStatement.getResultSet()) {
				while (resultSet.next()) {
					Operation operationEntity = new Operation(
							resultSet.getLong("operation_id"),
							resultSet.getLong("player_id"),
							resultSet.getLong("account_id"),
							OperationType.valueOf(resultSet.getString("type")),
							resultSet.getBigDecimal("amount")
					);

					operations.add(operationEntity);
				}
			}
		} catch (SQLException exception) {
			throw new RuntimeException("Возникла ошибка при работе с базой данных");
		}

		return Collections.unmodifiableList(operations);
	}
}
