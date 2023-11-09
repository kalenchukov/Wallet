/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.fixaction.service.impl;

import dev.kalenchukov.starter.fixaction.entity.Action;
import dev.kalenchukov.starter.fixaction.repository.FixActionRepository;
import dev.kalenchukov.starter.fixaction.service.FixActionService;
import dev.kalenchukov.starter.fixaction.types.ActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Класс сервиса действий.
 */
@Service
public class FixActionServiceImpl implements FixActionService {
	/**
	 * Хранилище действий.
	 */
	private final FixActionRepository fixActionRepository;

	/**
	 * Конструирует сервис действий.
	 *
	 * @param fixActionRepository хранилище действий.
	 */
	@Autowired
	public FixActionServiceImpl(final FixActionRepository fixActionRepository) {
		Objects.requireNonNull(fixActionRepository);
		this.fixActionRepository = fixActionRepository;
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
	public Action add(final long playerId, final ActionType actionType, final ActionType.Status actionTypeStatus) {
		Objects.requireNonNull(actionType);
		Objects.requireNonNull(actionTypeStatus);

		return this.fixActionRepository.save(new Action(0L, playerId, actionType, actionTypeStatus));
	}
}
