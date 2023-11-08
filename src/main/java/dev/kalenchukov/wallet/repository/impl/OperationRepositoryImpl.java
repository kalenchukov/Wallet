/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.type.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс хранилища операций.
 */
@Repository
public class OperationRepositoryImpl implements OperationRepository {
	/**
	 * Источник данных.
	 */
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * Конструирует хранилище операций.
	 *
	 * @param namedParameterJdbcTemplate источник данных.
	 */
	@Autowired
	public OperationRepositoryImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		Objects.requireNonNull(namedParameterJdbcTemplate);
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

		String query = """
				INSERT INTO operations (player_id, account_id, type, amount)
				VALUES (:player_id, :account_id, :type, :amount)
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("player_id", operation.getPlayerId());
		mapSqlParameterSource.addValue("account_id", operation.getAccountId());
		mapSqlParameterSource.addValue("type", operation.getOperationType().name());
		mapSqlParameterSource.addValue("amount", operation.getAmount());

		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.namedParameterJdbcTemplate.update(query, mapSqlParameterSource, keyHolder);
		long operationId = (long) Objects.requireNonNull(keyHolder.getKeys()).get("operation_id");

		return new Operation(operationId, operation.getPlayerId(), operation.getAccountId(),
				operation.getOperationType(), operation.getAmount()
		);
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
		String query = """
				SELECT *
				FROM operations
				WHERE player_id = :player_id AND account_id = :account_id AND operation_id = :operation_id
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("player_id", playerId);
		mapSqlParameterSource.addValue("account_id", accountId);
		mapSqlParameterSource.addValue("operation_id", operationId);

		try {
			return this.namedParameterJdbcTemplate.queryForObject(query, mapSqlParameterSource,
					(rs, row) -> Optional.of(
							new Operation(rs.getLong("operation_id"),
									rs.getLong("player_id"),
									rs.getLong("account_id"),
									OperationType.valueOf(
											rs.getString("type")),
									rs.getBigDecimal("amount")
							))
			);
		} catch (EmptyResultDataAccessException exception) {
			return Optional.empty();
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
	public List<Operation> find(final long playerId, final long accountId) {
		String query = """
				SELECT *
				FROM operations
				WHERE player_id = :player_id AND account_id = :account_id
				ORDER BY operation_id DESC
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("player_id", playerId);
		mapSqlParameterSource.addValue("account_id", accountId);

		return this.namedParameterJdbcTemplate.query(query, mapSqlParameterSource,
				(rs, row) -> new Operation(rs.getLong("operation_id"),
						rs.getLong("player_id"),
						rs.getLong("account_id"),
						OperationType.valueOf(
								rs.getString("type")),
						rs.getBigDecimal("amount")
				)
		);
	}
}
