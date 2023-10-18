/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers;

import dev.kalenchukov.wallet.in.commands.AbstractCommandHandler;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.DuplicatePlayerException;
import dev.kalenchukov.wallet.exceptions.EmptyNamePlayerException;
import dev.kalenchukov.wallet.exceptions.EmptyPasswordPlayerException;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.PlayerRepositoryImpl;
import dev.kalenchukov.wallet.repository.modules.DataBase;
import dev.kalenchukov.wallet.type.ActionType;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.in.service.impl.PlayerServiceImpl;

import java.io.PrintStream;
import java.util.Objects;

/**
 * Класс обработчика команды регистрации игрока.
 */
public class NewPlayerCommandHandler extends AbstractCommandHandler {
	/**
	 * Сервис игроков.
	 */
	private final PlayerService playerService;

	/**
	 * Конструирует обработчик команды.
	 */
	public NewPlayerCommandHandler() {
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
		String password = data[2];

		try {
			Player player = this.playerService.add(name, password);
			this.fixAction(player.getPlayerId(), ActionType.NEW, ActionType.Status.SUCCESS);
			output.print("Создан игрок с ID: " + player.getPlayerId());
		} catch (EmptyNamePlayerException exception) {
			output.printf(exception.getMessage(), exception.getName());
		} catch (EmptyPasswordPlayerException | DuplicatePlayerException exception) {
			output.printf(exception.getMessage());
		}
	}
}
