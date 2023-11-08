/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * Конструирует хранилище игроков.
	 *
	 * @param namedParameterJdbcTemplate источник данных.
	 */
	@Autowired
	public PlayerRepositoryImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		Objects.requireNonNull(namedParameterJdbcTemplate);
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

		String query = """
				INSERT INTO players (name, password)
				VALUES (:name, :password)
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("name", player.getName());
		mapSqlParameterSource.addValue("password", player.getPassword());

		KeyHolder keyHolder = new GeneratedKeyHolder();

		this.namedParameterJdbcTemplate.update(query, mapSqlParameterSource, keyHolder);
		long playerId = (long) Objects.requireNonNull(keyHolder.getKeys()).get("player_id");

		return new Player(playerId, player.getName(), player.getPassword());
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

		String query = """
				SELECT COUNT(*)
				FROM players
				WHERE name = :name
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("name", name);

		long countObject = Objects.requireNonNull(
				this.namedParameterJdbcTemplate.queryForObject(query, mapSqlParameterSource, Long.class));

		return (countObject > 0);
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

		String query = """
				SELECT *
				FROM players
				WHERE name = :name AND password = :password
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("name", name);
		mapSqlParameterSource.addValue("password", password);

		try {
			return this.namedParameterJdbcTemplate.queryForObject(query, mapSqlParameterSource,
					(rs, row) -> Optional.of(
							new Player(rs.getLong("player_id"),
									rs.getString("name"),
									rs.getString("password")
							))
			);
		} catch (EmptyResultDataAccessException exception) {
			return Optional.empty();
		}
	}
}
