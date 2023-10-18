/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.repository.ActionRepository;
import dev.kalenchukov.wallet.type.ActionType;

import java.util.Objects;
import java.util.Set;

/**
 * Класс сервиса действий.
 */
public class ActionServiceImpl implements ActionService {
	/**
	 * Хранилище действий.
	 */
	private final ActionRepository actionRepository;

	/**
	 * Конструирует сервис действий.
	 *
	 * @param actionRepository хранилище действий.
	 */
	public ActionServiceImpl(final ActionRepository actionRepository) {
		Objects.requireNonNull(actionRepository);

		this.actionRepository = actionRepository;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId         {@inheritDoc}
	 * @param actionType       {@inheritDoc}
	 * @param actionTypeStatus {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Action add(final long playerId, final ActionType actionType, ActionType.Status actionTypeStatus) {
		Objects.requireNonNull(actionType);
		Objects.requireNonNull(actionTypeStatus);

		return this.actionRepository.save(
				new Action(playerId, actionType, actionTypeStatus)
		);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Set<Action> find(final long playerId) {
		return this.actionRepository.find(playerId);
	}
}
