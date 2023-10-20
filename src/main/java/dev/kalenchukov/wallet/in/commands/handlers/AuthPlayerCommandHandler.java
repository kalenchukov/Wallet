/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers;

import dev.kalenchukov.wallet.Wallet;
import dev.kalenchukov.wallet.exceptions.NotNeedAuthException;
import dev.kalenchukov.wallet.in.commands.AbstractCommandHandler;
import dev.kalenchukov.wallet.exceptions.NotFoundPlayerException;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.PlayerRepositoryImpl;
import dev.kalenchukov.wallet.repository.modules.DataBase;
import dev.kalenchukov.wallet.type.ActionType;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.in.service.impl.PlayerServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.PrintStream;
import java.util.Objects;

/**
 * Класс обработчика команды авторизации игрока.
 */
public class AuthPlayerCommandHandler extends AbstractCommandHandler {
	/**
	 * Сервис игроков.
	 */
	private final PlayerService playerService;

	/**
	 * Конструирует обработчик команды.
	 */
	public AuthPlayerCommandHandler() {
		super(new ActionServiceImpl(new ActionRepositoryImpl(DataBase.getDataSource())));
		this.playerService = new PlayerServiceImpl(new PlayerRepositoryImpl(DataBase.getDataSource()));
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
		this.checkCountRequireArgs(data, 3);

		String name = data[1];
		String password = DigestUtils.md5Hex(data[2]);

		if (Wallet.AUTH_PLAYER != null) {
			throw new NotNeedAuthException();
		}

		try {
			Wallet.AUTH_PLAYER = this.playerService.find(name, password);
			this.fixAction(Wallet.AUTH_PLAYER.getPlayerId(), ActionType.AUTH, ActionType.Status.SUCCESS);
			output.print("OK.");
		} catch (NotFoundPlayerException exception) {
			output.printf(exception.getMessage(), exception.getName());
		}
	}
}
