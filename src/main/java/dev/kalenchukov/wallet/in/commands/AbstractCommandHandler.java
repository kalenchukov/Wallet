/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands;

import dev.kalenchukov.wallet.exceptions.MissingArgsCommandException;
import dev.kalenchukov.wallet.type.ActionType;
import dev.kalenchukov.wallet.in.service.ActionService;

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
	 * Конструирует обработчик абстрактной команды.
	 *
	 * @param actionService сервис действий.
	 */
	public AbstractCommandHandler(final ActionService actionService) {
		this.actionService = actionService;
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
	 * Проверяет количество аргументов для команды.
	 *
	 * @param data        данные.
	 * @param requireArgs количество необходимых аргументов.
	 * @throws MissingArgsCommandException если указаны не все аргументы для данной команды.
	 */
	protected void checkCountRequireArgs(final String[] data, final int requireArgs) {
		Objects.requireNonNull(data);

		if (data.length < requireArgs) {
			throw new MissingArgsCommandException(data.length - 1);
		}
	}
}
