/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet;

import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.AuthException;
import dev.kalenchukov.wallet.in.commands.CommandHandler;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.in.commands.type.CommandType;
import dev.kalenchukov.wallet.exceptions.MissingArgsCommandException;
import dev.kalenchukov.wallet.in.commands.util.ConsoleUtil;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
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
				OUTPUT.append(ConsoleUtil.getDecorator(AUTH_PLAYER));

				String[] inputData = scanner.nextLine().split(scanner.delimiter().toString());

				if (Wallet.isCommandEmpty(inputData)) {
					OUTPUT.print("Необходимо ввести команду.");
					continue;
				}

				String command = inputData[0];

				if (Wallet.isCommandExit(command)) {
					Thread.currentThread().interrupt();
					continue;
				}

				CommandType commandType = CommandType.findByName(command);

				if (Wallet.isCommandNotFound(commandType)) {
					OUTPUT.print("Команда не найдена.");
					continue;
				}

				try {
					CommandHandler commandHandler = commandType.getCommandHandler();
					commandHandler.execute(inputData, OUTPUT);
				} catch (MissingArgsCommandException exception) {
					OUTPUT.printf(exception.getMessage(), exception.getInputParam());
				} catch (AuthException exception) {
					OUTPUT.printf(exception.getMessage());
				} catch (ApplicationException exception) {
					OUTPUT.println("Произошла ошибка в приложении." +
							"Сообщите администратору, он не в курсе так как логирования пока нет.");
					OUTPUT.println(exception.getMessage());
					OUTPUT.println(exception.getCause());
					OUTPUT.println(Arrays.toString(exception.getStackTrace()));
				}
			}
		} catch (Exception exception) {
			OUTPUT.println("Ввод команд не ожидается.");
		}
	}

	/**
	 * Проверяет, введена ли команда.
	 * @param inputData введённые данные.
	 * @return {@code true} если команда введена, иначе {@code false}.
	 */
	public static boolean isCommandEmpty(final String[] inputData) {
		return (inputData.length == 0 || inputData[0].isEmpty());
	}

	/**
	 * Проверяет, является ли команда, командой выхода из приложения.
	 *
	 * @param command команда.
	 * @return {@code true} если команда является командой выхода, иначе {@code false}.
	 */
	public static boolean isCommandExit(final String command) {
		return ("exit".equals(command));
	}

	/**
	 * Проверяет, найдена ли команда.
	 *
	 * @param commandType тип команды.
	 * @return {@code true} если команда не найдена, иначе {@code false}.
	 */
	public static boolean isCommandNotFound(final CommandType commandType) {
		return (commandType == null);
	}
}
