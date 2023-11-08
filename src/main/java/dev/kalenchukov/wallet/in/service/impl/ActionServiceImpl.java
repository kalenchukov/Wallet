/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.starter.fixaction.entity.Action;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Класс сервиса действий.
 */
@Service
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
	@Autowired
	public ActionServiceImpl(final ActionRepository actionRepository) {
		Objects.requireNonNull(actionRepository);

		this.actionRepository = actionRepository;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public List<Action> find(final long playerId) {
		return this.actionRepository.find(playerId);
	}
}
