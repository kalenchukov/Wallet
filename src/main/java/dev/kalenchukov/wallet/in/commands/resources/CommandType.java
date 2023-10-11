/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.resources;

import dev.kalenchukov.wallet.in.commands.CommandHandler;
import dev.kalenchukov.wallet.in.commands.handlers.*;

import java.util.Objects;

/**
 * Перечисление типов команд.
 */
public enum CommandType {
	/**
	 * Регистрация игрока.
	 */
	NEW("new", new NewPlayerCommandHandler()),

	/**
	 * Авторизация игрока.
	 */
	AUTH("auth", new AuthPlayerCommandHandler()),

	/**
	 * Создание счёта.
	 */
	CREATE("create", new CreateAccountCommandHandler()),

	/**
	 * Пополнение счёта.
	 */
	CREDIT("credit", new CreditAccountCommandHandler()),

	/**
	 * Списание со счёта.
	 */
	DEBIT("debit", new DebitAccountCommandHandler()),

	/**
	 * Информация по операции.
	 */
	OPERATION("operation", new OperationAccountCommandHandler()),

	/**
	 * Информация по всем операциям.
	 */
	OPERATIONS("operations", new OperationListAccountCommandHandler()),

	/**
	 * Информация по всем действиям.
	 */
	ACTIONS("actions", new ActionListPlayerCommandHandler()),

	/**
	 * Деавторизация пользователя.
	 */
	LOGOUT("logout", new LogoutPlayerCommandHandler());

	/**
	 * Название.
	 * <p>Должно быть только из букв [a-z].</p>
	 */
	private final String name;

	/**
	 * Обработчик.
	 */
	private final CommandHandler commandHandler;

	/**
	 * Конструирует типы команд.
	 *
	 * @param name           команда.
	 * @param commandHandler обработчик.
	 */
	CommandType(final String name, final CommandHandler commandHandler) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(commandHandler);

		this.name = name;
		this.commandHandler = commandHandler;
	}

	/**
	 * Ищет команду по названию.
	 *
	 * @param name название.
	 * @return тип команды.
	 */
	public static CommandType findByName(final String name) {
		for (CommandType commandType : CommandType.values()) {
			if (commandType.getName().equals(name)) {
				return commandType;
			}
		}
		return null;
	}

	/**
	 * Возвращает название.
	 *
	 * @return название.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Возвращает обработчик.
	 *
	 * @return обработчик.
	 */
	public CommandHandler getCommandHandler() {
		return this.commandHandler;
	}
}
