/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.DuplicatePlayerException;
import dev.kalenchukov.wallet.exceptions.EmptyNamePlayerException;
import dev.kalenchukov.wallet.exceptions.EmptyPasswordPlayerException;
import dev.kalenchukov.wallet.exceptions.NotFoundPlayerException;
import dev.kalenchukov.wallet.repository.PlayerRepository;

import java.util.Objects;
import java.util.Optional;

/**
 * Класс сервиса игроков.
 */
public class PlayerServiceImpl implements PlayerService {
	/**
	 * Хранилище игроков.
	 */
	private final PlayerRepository playerRepository;

	/**
	 * Конструирует сервис игроков.
	 *
	 * @param playerRepository хранилище игроков.
	 */
	public PlayerServiceImpl(final PlayerRepository playerRepository) {
		Objects.requireNonNull(playerRepository);

		this.playerRepository = playerRepository;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param name     {@inheritDoc}
	 * @param password {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws DuplicatePlayerException     {@inheritDoc}
	 * @throws EmptyNamePlayerException     если имя игрока пустое.
	 * @throws EmptyPasswordPlayerException если пароль пустой.
	 */
	@Override
	public Player add(final String name, final String password)
			throws DuplicatePlayerException {
		Objects.requireNonNull(name);
		Objects.requireNonNull(password);

		if (this.playerRepository.isByName(name)) {
			throw new DuplicatePlayerException(name);
		}

		Player player = new Player(name, password);

		return this.playerRepository.save(player);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param name     {@inheritDoc}
	 * @param password {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundPlayerException {@inheritDoc}
	 */
	@Override
	public Player getByNameAndPassword(final String name, final String password)
			throws NotFoundPlayerException {
		Objects.requireNonNull(name);
		Objects.requireNonNull(password);

		Optional<Player> player = this.playerRepository.getByNameAndPassword(name, password);

		return player.orElseThrow(() -> new NotFoundPlayerException(name));
	}
}
