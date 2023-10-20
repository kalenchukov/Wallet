/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers;

import dev.kalenchukov.wallet.Wallet;
import dev.kalenchukov.wallet.exceptions.NeedAuthException;
import dev.kalenchukov.wallet.in.commands.AbstractCommandHandler;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.repository.modules.DataBase;
import dev.kalenchukov.wallet.type.ActionType;

import java.io.PrintStream;
import java.util.Objects;

/**
 * Класс обработчика команды деавторизации игрока.
 */
public class LogoutPlayerCommandHandler extends AbstractCommandHandler {
	/**
	 * Конструирует обработчик команды.
	 */
	public LogoutPlayerCommandHandler() {
		super(new ActionServiceImpl(new ActionRepositoryImpl(DataBase.getDataSource())));
	}

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
		this.checkCountRequireArgs(data, 1);

		if (Wallet.AUTH_PLAYER == null) {
			throw new NeedAuthException();
		}

		this.fixAction(Wallet.AUTH_PLAYER.getPlayerId(), ActionType.LOGOUT, ActionType.Status.SUCCESS);
		Wallet.AUTH_PLAYER = null;
		output.print("OK.");
	}
}
