/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.DuplicatePlayerException;
import dev.kalenchukov.wallet.exceptions.NotFoundPlayerException;

/**
 * Интерфейс для реализации класса сервиса игроков.
 */
public interface PlayerService {
	/**
	 * Добавляет игрока.
	 *
	 * @param name     имя.
	 * @param password пароль.
	 * @return игрока.
	 * @throws DuplicatePlayerException если игрок с указанным именем уже существует.
	 */
	Player add(String name, String password)
			throws DuplicatePlayerException;

	/**
	 * Возвращает игрока.
	 *
	 * @param name     имя.
	 * @param password пароль.
	 * @return игрока.
	 * @throws NotFoundPlayerException если игрок не найден.
	 */
	Player find(String name, String password)
			throws NotFoundPlayerException;
}
