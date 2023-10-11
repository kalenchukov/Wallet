/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Player;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;

/**
 * Класс хранилища игроков.
 */
public class PlayerRepositoryImpl implements PlayerRepository {
	/**
	 * Хранилище игроков.
	 */
	private static Set<Player> DATA = new HashSet<>();

	/**
	 * Конструирует хранилище игроков.
	 */
	public PlayerRepositoryImpl() {
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param player {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Player save(final Player player) {
		Objects.requireNonNull(player);
		DATA.add(player);
		return player;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param name {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean isByName(final String name) {
		Objects.requireNonNull(name);
		return DATA.stream()
				.anyMatch(player -> player.getName().equals(name));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param name     {@inheritDoc}
	 * @param password {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Optional<Player> getByNameAndPassword(final String name, final String password) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(password);
		return DATA.stream()
				.filter(player -> (player.getName().equals(name) && player.getPassword().equals(DigestUtils.md2Hex(password))))
				.findFirst();
	}
}
