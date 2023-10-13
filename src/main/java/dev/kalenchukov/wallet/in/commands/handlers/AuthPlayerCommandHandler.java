/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers;

import dev.kalenchukov.wallet.Wallet;
import dev.kalenchukov.wallet.in.commands.AbstractCommandHandler;
import dev.kalenchukov.wallet.exceptions.NotFoundPlayerException;
import dev.kalenchukov.wallet.repository.PlayerRepositoryImpl;
import dev.kalenchukov.wallet.resources.ActionType;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.in.service.PlayerServiceImpl;

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
		this.playerService = new PlayerServiceImpl(new PlayerRepositoryImpl());
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
		this.checkCountRequireParameters(data, 3);

		String name = data[1];
		String password = data[2]; // не будем пока заморачиваться о хранении его в String Pool

		if (Wallet.AUTH_PLAYER != null) {
			output.printf("Вы уже авторизировались.");
		}

		try {
			Wallet.AUTH_PLAYER = this.playerService.getByNameAndPassword(name, password);
			this.fixAction(Wallet.AUTH_PLAYER.getPlayerId(), ActionType.AUTH, ActionType.Status.SUCCESS);
			output.print("OK.");
		} catch (NotFoundPlayerException exception) {
			output.printf(exception.getMessage(), exception.getName());
		}
	}
}
