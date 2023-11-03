/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс хранилища игроков.
 */
@Repository
public class PlayerRepositoryImpl implements PlayerRepository {
	/**
	 * Источник данных.
	 */
	private final DataSource dataSource;

	/**
	 * Конструирует хранилище игроков.
	 *
	 * @param dataSource источник данных.
	 */
	@Autowired
	public PlayerRepositoryImpl(final DataSource dataSource) {
		Objects.requireNonNull(dataSource);
		this.dataSource = dataSource;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param player {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Player save(final Player player) {
		Objects.requireNonNull(player);

		String query = "INSERT INTO players (name, password) VALUES (?, ?)";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, player.getName());
			preparedStatement.setString(2, player.getPassword());
			preparedStatement.executeUpdate();

			try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
				resultSet.next();
				long playerId = resultSet.getLong(1);

				return new Player(
						playerId,
						player.getName(),
						player.getPassword()
				);
			}
		} catch (SQLException exception) {
			throw new RuntimeException("Возникла ошибка при работе с базой данных");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param name {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean existsByName(final String name) {
		Objects.requireNonNull(name);

		boolean result = false;
		String query = "SELECT EXISTS (SELECT * FROM players WHERE name = ?)";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, name);
			preparedStatement.execute();

			try (ResultSet resultSet = preparedStatement.getResultSet()) {
				if (resultSet.next()) {
					result = resultSet.getBoolean(1);
				}
			}
		} catch (SQLException exception) {
			throw new RuntimeException("Возникла ошибка при работе с базой данных");
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param name     {@inheritDoc}
	 * @param password {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Optional<Player> find(final String name, final String password) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(password);

		Optional<Player> player = Optional.empty();
		String query = "SELECT * FROM players WHERE name = ? AND password = ?";

		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, password);
			preparedStatement.execute();

			try (ResultSet resultSet = preparedStatement.getResultSet()) {
				if (resultSet.next()) {
					Player playerEntity = new Player(
							resultSet.getLong("player_id"),
							resultSet.getString("name"),
							resultSet.getString("password")
					);

					player = Optional.of(playerEntity);
				}
			}
		} catch (SQLException exception) {
			throw new RuntimeException("Возникла ошибка при работе с базой данных");
		}

		return player;
	}
}
