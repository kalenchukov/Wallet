/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers;

import dev.kalenchukov.wallet.Wallet;
import dev.kalenchukov.wallet.exceptions.NeedAuthCommandException;
import dev.kalenchukov.wallet.in.commands.AbstractCommandHandler;
import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.in.commands.handlers.comparators.SortActionsByIdDescComparator;
import dev.kalenchukov.wallet.resources.ActionType;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Objects;

/**
 * Класс обработчика команды просмотра всех действий игрока.
 */
public class ActionListPlayerCommandHandler extends AbstractCommandHandler {
	/**
	 * {@inheritDoc}
	 *
	 * @param data   {@inheritDoc}
	 * @param output {@inheritDoc}
	 */
	@Override
	public void execute(final String[] data, final PrintStream output) {
		Objects.requireNonNull(data);
		Objects.requireNonNull(output);
		this.checkCountRequireParameters(data, 1);

		if (Wallet.AUTH_PLAYER == null) {
			throw new NeedAuthCommandException();
		}

		Collection<Action> actions = this.actionService.find(Wallet.AUTH_PLAYER.getPlayerId());

		if (actions.isEmpty()) {
			output.print("Действия не найдены.");
		}

		actions = actions.stream().sorted(new SortActionsByIdDescComparator()).toList();

		for (Action action : actions) {
			output.append("actionId: ").append(String.valueOf(action.getActionId()))
					.append("\t|\t")
					.append("playerId: ").append(String.valueOf(action.getPlayerId()))
					.append("\t|\t")
					.append("status: ").append(action.getActionTypeStatus().name())
					.append("\t|\t")
					.append("action: ").println(action.getActionType().name());
		}

		this.fixAction(Wallet.AUTH_PLAYER.getPlayerId(), ActionType.ACTIONS_LIST, ActionType.Status.SUCCESS);
	}
}
