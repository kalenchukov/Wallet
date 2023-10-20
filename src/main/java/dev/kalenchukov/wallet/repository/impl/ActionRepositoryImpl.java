/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.repository.ActionRepository;
import dev.kalenchukov.wallet.type.ActionType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Класс хранилища действий.
 */
public class ActionRepositoryImpl implements ActionRepository {
	/**
	 * Источник данных.
	 */
	private final DataSource dataSource;

	/**
	 * Конструирует хранилище действий.
	 *
	 * @param dataSource источник данных.
	 */
	public ActionRepositoryImpl(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param action {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Action save(final Action action) {
		Objects.requireNonNull(action);

		String query = "INSERT INTO actions (player_id, type, status) VALUES (?, ?, ?)";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setLong(1, action.getPlayerId());
			// Думаю лучше будет enum ordinal записывать в столбец, но для наглядности названия хорошо.
			// Или для целостности данных создать в БД enum на столбец.
			preparedStatement.setString(2, action.getActionType().name());
			preparedStatement.setString(3, action.getActionTypeStatus().name());
			preparedStatement.executeUpdate();

			try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
				resultSet.next();
				long actionId = resultSet.getLong(1);

				return new Action(
						actionId,
						action.getPlayerId(),
						action.getActionType(),
						action.getActionTypeStatus()
				);
			}
		} catch (SQLException exception) {
			throw new ApplicationException(exception);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Set<Action> find(final long playerId) {
		Set<Action> actions = new HashSet<>();
		String query = "SELECT * FROM actions WHERE player_id = ?";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setLong(1, playerId);
			preparedStatement.execute();

			try (ResultSet resultSet = preparedStatement.getResultSet()) {
				while (resultSet.next()) {
					Action actionEntity = new Action(
							resultSet.getLong("action_id"),
							resultSet.getLong("player_id"),
							ActionType.valueOf(resultSet.getString("type")),
							ActionType.Status.valueOf(resultSet.getString("status"))
					);

					actions.add(actionEntity);
				}
			}
		} catch (SQLException exception) {
			throw new ApplicationException(exception);
		}

		return Collections.unmodifiableSet(actions);
	}
}
