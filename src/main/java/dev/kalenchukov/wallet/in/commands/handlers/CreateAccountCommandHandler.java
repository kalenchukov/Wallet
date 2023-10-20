/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers;

import dev.kalenchukov.wallet.Wallet;
import dev.kalenchukov.wallet.exceptions.NeedAuthException;
import dev.kalenchukov.wallet.in.commands.AbstractCommandHandler;
import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.repository.impl.AccountRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.OperationRepositoryImpl;
import dev.kalenchukov.wallet.repository.modules.DataBase;
import dev.kalenchukov.wallet.type.ActionType;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.in.service.impl.AccountServiceImpl;

import java.io.PrintStream;
import java.util.Objects;

/**
 * Класс обработчика команды создания счёта.
 */
public class CreateAccountCommandHandler extends AbstractCommandHandler {
	/**
	 * Сервис счетов.
	 */
	private final AccountService accountService;

	/**
	 * Конструирует обработчик команды.
	 */
	public CreateAccountCommandHandler() {
		super(new ActionServiceImpl(new ActionRepositoryImpl(DataBase.getDataSource())));
		this.accountService = new AccountServiceImpl(new AccountRepositoryImpl(DataBase.getDataSource()), new OperationRepositoryImpl(DataBase.getDataSource()));
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

		Account account = this.accountService.add(Wallet.AUTH_PLAYER.getPlayerId());
		this.fixAction(Wallet.AUTH_PLAYER.getPlayerId(), ActionType.CREATE_ACCOUNT, ActionType.Status.SUCCESS);
		output.print("Создан счёт с ID: " + account.getAccountId());
	}
}
