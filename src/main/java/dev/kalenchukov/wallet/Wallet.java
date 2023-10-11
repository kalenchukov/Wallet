/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet;

import dev.kalenchukov.wallet.exceptions.NeedAuthCommandException;
import dev.kalenchukov.wallet.in.commands.CommandHandler;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.in.commands.resources.CommandType;
import dev.kalenchukov.wallet.exceptions.MissingParametersCommandException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс запуска приложения.
 */
public class Wallet {
	/**
	 * Поток входящих данных.
	 */
	private static final InputStream INPUT = System.in;
	/**
	 * Приёмник исходящих данных.
	 */
	private static final PrintStream OUTPUT = System.out;
	/**
	 * Авторизированный игрок.
	 */
	public static Player AUTH_PLAYER = null;

	/**
	 * Метод запуска.
	 *
	 * @param args параметры.
	 */
	public static void main(String[] args) {
		OUTPUT.print("Добро пожаловать в Wallet!");

		try (Scanner scanner = new Scanner(INPUT)) {
			while (!Thread.currentThread().isInterrupted()) {
				OUTPUT.append(Wallet.getDecorator());

				String[] data = scanner.nextLine().split(scanner.delimiter().toString());

				if (data.length == 0 || data[0].isEmpty()) {
					OUTPUT.print("Необходимо ввести команду.");
					continue;
				}

				String command = data[0];

				if ("exit".equals(command)) {
					Thread.currentThread().interrupt();
					continue;
				}

				CommandType commandType = CommandType.findByName(command);

				if (commandType == null) {
					OUTPUT.print("Команда не найдена.");
					continue;
				}

				CommandHandler commandHandler = commandType.getCommandHandler();

				try {
					commandHandler.execute(data, OUTPUT);
				} catch (MissingParametersCommandException exception) {
					OUTPUT.printf(exception.getMessage(), exception.getCountParam());
				} catch (NeedAuthCommandException exception) {
					OUTPUT.printf(exception.getMessage());
				}
			}
		}
	}

	/**
	 * Возвращает декоратор.
	 *
	 * @return декоратор.
	 */
	public static String getDecorator() {
		StringBuilder decorator = new StringBuilder();
		decorator.append("\n");
		if (Objects.nonNull(AUTH_PLAYER)) {
			decorator.append(AUTH_PLAYER.getName());
		}
		decorator.append("> ");

		return decorator.toString();
	}
}
