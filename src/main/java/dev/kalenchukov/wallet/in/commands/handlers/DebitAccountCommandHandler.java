/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers;

import dev.kalenchukov.wallet.Wallet;
import dev.kalenchukov.wallet.in.commands.AbstractCommandHandler;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.*;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.repository.impl.AccountRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.OperationRepositoryImpl;
import dev.kalenchukov.wallet.repository.modules.DataBase;
import dev.kalenchukov.wallet.type.ActionType;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.in.service.impl.AccountServiceImpl;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Класс обработчика команды списания со счёта.
 */
public class DebitAccountCommandHandler extends AbstractCommandHandler {
	/**
	 * Сервис счетов.
	 */
	private final AccountService accountService;

	/**
	 * Конструирует обработчик команды.
	 */
	public DebitAccountCommandHandler() {
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
		this.checkCountRequireArgs(data, 3);

		if (Wallet.AUTH_PLAYER == null) {
			throw new NeedAuthException();
		}

		boolean success = false;

		try {
			long accountId = Long.parseLong(data[1]);

			try {
				BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(data[2]));
				Operation operation = this.accountService.debit(accountId, Wallet.AUTH_PLAYER.getPlayerId(), amount);
				output.print("Идентификатор операции: " + operation.getOperationId());
				success = true;
			} catch (NegativeAmountOperationException exception) {
				output.printf(exception.getMessage(), exception.getAmount());
			} catch (NotFoundAccountException exception) {
				output.printf(exception.getMessage(), exception.getAccountId());
			} catch (OutOfAmountOperationException exception) {
				output.printf(exception.getMessage(), exception.getAmount());
			} catch (NoAccessAccountException exception) {
				output.printf(exception.getMessage(), exception.getAccountId());
			} catch (NumberFormatException exception) {
				output.print("Некорректно указана сумма.");
			}
		} catch (NumberFormatException exception) {
			output.print("Некорректный идентификатор счёта.");
		}

		this.fixAction(
				Wallet.AUTH_PLAYER.getPlayerId(),
				ActionType.DEBIT_ACCOUNT,
				(success) ? ActionType.Status.SUCCESS : ActionType.Status.FAIL
		);
	}
}
