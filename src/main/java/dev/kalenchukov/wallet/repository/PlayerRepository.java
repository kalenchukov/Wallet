/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.ApplicationException;

import java.util.Optional;

/**
 * Интерфейс для реализации класса хранилища игроков.
 */
public interface PlayerRepository {
	/**
	 * Сохраняет игрока.
	 *
	 * @param player игрок.
	 * @return игрока.
	 * @throws ApplicationException если произошла ошибка при работе с приложением.
	 */
	Player save(Player player);

	/**
	 * Проверяет игрока.
	 *
	 * @param name имя.
	 * @return {@code true}, если игрок существует, иначе {@code false}.
	 * @throws ApplicationException если произошла ошибка при работе с приложением.
	 */
	boolean existsByName(String name);

	/**
	 * Ищет игрока.
	 *
	 * @param name     имя.
	 * @param password пароль.
	 * @return игрока.
	 * @throws ApplicationException если произошла ошибка при работе с приложением.
	 */
	Optional<Player> find(String name, String password);
}
