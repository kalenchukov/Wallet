/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands;

import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.MissingParametersCommandException;
import dev.kalenchukov.wallet.repository.ActionRepositoryImpl;
import dev.kalenchukov.wallet.resources.ActionType;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.ActionServiceImpl;

import java.io.PrintStream;
import java.util.Objects;

/**
 * Класс для реализации обработчика команды.
 */
public abstract class AbstractCommandHandler implements CommandHandler {
	/**
	 * Сервис действий.
	 */
	protected final ActionService actionService;

	/**
	 * Конструирует обработчик команды.
	 */
	public AbstractCommandHandler() {
		this.actionService = new ActionServiceImpl(new ActionRepositoryImpl());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param data   {@inheritDoc}
	 * @param output {@inheritDoc}
	 */
	@Override
	public abstract void execute(final String[] data, PrintStream output);

	/**
	 * Фиксирует действия игрока.
	 *
	 * @param playerId         идентификатор игрока.
	 * @param actionType       тип действия.
	 * @param actionTypeStatus статус действия.
	 */
	protected void fixAction(final long playerId, final ActionType actionType, final ActionType.Status actionTypeStatus) {
		Objects.requireNonNull(actionType);
		Objects.requireNonNull(actionTypeStatus);

		this.actionService.add(playerId, actionType, actionTypeStatus);
	}

	/**
	 * Проверяет количество параметров для команды.
	 *
	 * @param data        данные.
	 * @param countParams количество необходимых параметров.
	 * @throws MissingParametersCommandException если указаны не все параметры для данной команды.
	 */
	protected void checkCountRequireParameters(final String[] data, final int countParams) {
		Objects.requireNonNull(data);

		if (data.length < countParams) {
			throw new MissingParametersCommandException(data.length);
		}
	}
}
