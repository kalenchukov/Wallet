/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers;

import dev.kalenchukov.wallet.Wallet;
import dev.kalenchukov.wallet.exceptions.NeedAuthException;
import dev.kalenchukov.wallet.in.commands.AbstractCommandHandler;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.in.commands.handlers.comparators.SortOperationsByIdDescComparator;
import dev.kalenchukov.wallet.in.service.OperationService;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.in.service.impl.OperationServiceImpl;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.OperationRepositoryImpl;
import dev.kalenchukov.wallet.repository.modules.DataBase;
import dev.kalenchukov.wallet.type.ActionType;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Objects;

/**
 * Класс обработчика команды просмотра всех операций со счётом.
 */
public class OperationListAccountCommandHandler extends AbstractCommandHandler {
	/**
	 * Сервис операций.
	 */
	private final OperationService operationService;

	/**
	 * Конструирует обработчик команды.
	 */
	public OperationListAccountCommandHandler() {
		super(new ActionServiceImpl(new ActionRepositoryImpl(DataBase.getDataSource())));
		this.operationService = new OperationServiceImpl(new OperationRepositoryImpl(DataBase.getDataSource()));
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
		this.checkCountRequireArgs(data, 2);

		if (Wallet.AUTH_PLAYER == null) {
			throw new NeedAuthException();
		}

		boolean success = false;

		try {
			long accountId = Long.parseLong(data[1]);

			Collection<Operation> operations = this.operationService.find(accountId, Wallet.AUTH_PLAYER.getPlayerId());

			operations = operations.stream().sorted(new SortOperationsByIdDescComparator()).toList();

			if (operations.isEmpty()) {
				output.print("Операции не найдены.");
			}

			for (Operation operation : operations) {
				output.append("operationId: ").append(String.valueOf(operation.getOperationId()))
						.append("\t|\t")
						.append("playerId: ").append(String.valueOf(operation.getPlayerId()))
						.append("\t|\t")
						.append("accountId: ").append(String.valueOf(operation.getAccountId()))
						.append("\t|\t")
						.append("amount: ").append(operation.getAmount().toString())
						.append("\t|\t")
						.append("operation: ").println(operation.getOperationType().name());
			}

			success = true;
		} catch (NumberFormatException exception) {
			output.print("Некорректный идентификатор счёта.");
		}

		this.fixAction(
				Wallet.AUTH_PLAYER.getPlayerId(),
				ActionType.OPERATIONS_ACCOUNT_LIST,
				(success) ? ActionType.Status.SUCCESS : ActionType.Status.FAIL
		);
	}
}
