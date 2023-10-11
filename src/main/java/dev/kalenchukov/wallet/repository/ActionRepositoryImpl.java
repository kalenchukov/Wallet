/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Action;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс хранилища действий.
 */
public class ActionRepositoryImpl implements ActionRepository {
	/**
	 * Хранилище действий.
	 */
	private static Set<Action> DATA = new HashSet<>();

	/**
	 * Конструирует хранилище действий.
	 */
	public ActionRepositoryImpl() {
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
		DATA.add(action);
		return action;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Set<Action> find(final long playerId) {
		return DATA.stream()
				.filter(elm -> Objects.equals(elm.getPlayerId(), playerId))
				.collect(Collectors.toUnmodifiableSet());
	}
}
