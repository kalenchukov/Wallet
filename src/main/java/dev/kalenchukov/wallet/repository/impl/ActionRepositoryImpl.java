/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.starter.fixaction.entity.Action;
import dev.kalenchukov.starter.fixaction.types.ActionType;
import dev.kalenchukov.wallet.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Класс хранилища действий.
 */
@Repository
public class ActionRepositoryImpl implements ActionRepository {
	/**
	 * Источник данных.
	 */
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * Конструирует хранилище действий.
	 *
	 * @param namedParameterJdbcTemplate источник данных.
	 */
	@Autowired
	public ActionRepositoryImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		Objects.requireNonNull(namedParameterJdbcTemplate);
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public List<Action> find(final long playerId) {
		String query = """
				SELECT *
				FROM actions
				WHERE player_id = :player_id
				ORDER BY action_id DESC
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("player_id", playerId);

		return this.namedParameterJdbcTemplate.query(query, mapSqlParameterSource,
				(rs, row) -> new Action(rs.getLong("action_id"),
						rs.getLong("player_id"),
						ActionType.valueOf(rs.getString("type")),
						ActionType.Status.valueOf(
								rs.getString("status"))
				)
		);
	}
}
