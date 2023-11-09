/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.fixaction.repository.impl;

import dev.kalenchukov.starter.fixaction.entity.Action;
import dev.kalenchukov.starter.fixaction.repository.FixActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * Класс хранилища действий.
 */
@Repository
public class FixActionRepositoryImpl implements FixActionRepository {
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
	public FixActionRepositoryImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		Objects.requireNonNull(namedParameterJdbcTemplate);
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

		String query = """
				INSERT INTO actions (player_id, type, status)
				VALUES (:player_id, :type, :status)
				""";

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("player_id", action.getPlayerId());
		mapSqlParameterSource.addValue("type", action.getActionType().name());
		mapSqlParameterSource.addValue("status", action.getActionTypeStatus().name());

		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.namedParameterJdbcTemplate.update(query, mapSqlParameterSource, keyHolder);

		long actionId = (long) Objects.requireNonNull(keyHolder.getKeys()).get("action_id");

		return new Action(actionId, action.getPlayerId(), action.getActionType(), action.getActionTypeStatus());
	}
}
