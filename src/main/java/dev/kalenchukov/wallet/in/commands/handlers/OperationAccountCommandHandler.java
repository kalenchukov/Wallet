/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers;

import dev.kalenchukov.wallet.Wallet;
import dev.kalenchukov.wallet.exceptions.NeedAuthCommandException;
import dev.kalenchukov.wallet.in.commands.AbstractCommandHandler;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NotFoundOperationException;
import dev.kalenchukov.wallet.repository.AccountRepositoryImpl;
import dev.kalenchukov.wallet.repository.OperationRepositoryImpl;
import dev.kalenchukov.wallet.resources.ActionType;
import dev.kalenchukov.wallet.in.service.OperationService;
import dev.kalenchukov.wallet.in.service.OperationServiceImpl;

import java.io.PrintStream;
import java.util.Objects;

/**
 * Класс обработчика команды просмотра операции.
 */
public class OperationAccountCommandHandler extends AbstractCommandHandler {
	/**
	 * Сервис операций.
	 */
	private final OperationService operationService;

	/**
	 * Конструирует обработчик команды.
	 */
	public OperationAccountCommandHandler() {
		this.operationService = new OperationServiceImpl(new OperationRepositoryImpl(), new AccountRepositoryImpl());
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
		this.checkCountRequireParameters(data, 2);

		if (Wallet.AUTH_PLAYER == null) {
			throw new NeedAuthCommandException();
		}

		boolean success = false;

		try {
			long operationId = Long.parseLong(data[1]);

			Operation operation = this.operationService.getById(operationId, Wallet.AUTH_PLAYER.getPlayerId());

			output.append("operationId: ").append(String.valueOf(operation.getOperationId()))
					.append("\t|\t")
					.append("accountId: ").append(String.valueOf(operation.getAccount().getAccountId()))
					.append("\t|\t")
					.append("playerId: ").append(String.valueOf(operation.getAccount().getPlayerId()))
					.append("\t|\t")
					.append("amount: ").append(operation.getAmount().toString())
					.append("\t|\t")
					.append("operation: ").print(operation.getOperationType().name());

			success = true;
		} catch (NotFoundOperationException exception) {
			output.printf(exception.getMessage(), exception.getOperationId());
		} catch (NumberFormatException exception) {
			output.print("Некорректный идентификатор операции.");
		}

		this.fixAction(
				Wallet.AUTH_PLAYER.getPlayerId(),
				ActionType.OPERATION_ACCOUNT,
				(success) ? ActionType.Status.SUCCESS : ActionType.Status.FAIL
		);
	}
}
