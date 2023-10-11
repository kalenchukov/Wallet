/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers;

import dev.kalenchukov.wallet.Wallet;
import dev.kalenchukov.wallet.exceptions.NeedAuthCommandException;
import dev.kalenchukov.wallet.exceptions.NoAccessAccountException;
import dev.kalenchukov.wallet.in.commands.AbstractCommandHandler;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NotFoundAccountException;
import dev.kalenchukov.wallet.exceptions.NegativeAmountOperationException;
import dev.kalenchukov.wallet.repository.AccountRepositoryImpl;
import dev.kalenchukov.wallet.repository.OperationRepositoryImpl;
import dev.kalenchukov.wallet.resources.ActionType;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.in.service.AccountServiceImpl;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Класс обработчика команды пополнения счёта.
 */
public class CreditAccountCommandHandler extends AbstractCommandHandler {
	/**
	 * Сервис счетов.
	 */
	private final AccountService accountService;

	/**
	 * Конструирует обработчик команды.
	 */
	public CreditAccountCommandHandler() {
		this.accountService = new AccountServiceImpl(new AccountRepositoryImpl(), new OperationRepositoryImpl());
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

		if (Wallet.AUTH_PLAYER == null) {
			throw new NeedAuthCommandException();
		}

		boolean success = false;

		try {
			long accountId = Long.parseLong(data[1]);

			try {
				BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(data[2]));
				Operation operation = this.accountService.credit(accountId, Wallet.AUTH_PLAYER.getPlayerId(), amount);
				output.print("Идентификатор операции: " + operation.getOperationId());
				success = true;
			} catch (NegativeAmountOperationException exception) {
				output.printf(exception.getMessage(), exception.getAmount());
			} catch (NotFoundAccountException exception) {
				output.printf(exception.getMessage(), exception.getAccountId());
			} catch (NumberFormatException exception) {
				output.print("Некорректно указана сумма.");
			} catch (NoAccessAccountException exception) {
				output.printf(exception.getMessage(), exception.getAccountId());
			}
		} catch (NumberFormatException exception) {
			output.print("Некорректный идентификатор счёта.");
		}

		this.fixAction(
				Wallet.AUTH_PLAYER.getPlayerId(),
				ActionType.CREDIT_ACCOUNT,
				(success) ? ActionType.Status.SUCCESS : ActionType.Status.FAIL
		);
	}
}
