/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.DuplicatePlayerException;
import dev.kalenchukov.wallet.exceptions.EmptyNamePlayerException;
import dev.kalenchukov.wallet.exceptions.EmptyPasswordPlayerException;
import dev.kalenchukov.wallet.exceptions.NotFoundPlayerException;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.repository.PlayerRepository;
import org.apache.commons.codec.digest.DigestUtils;

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
	public Player add(final String name, final String password) throws DuplicatePlayerException {
		Objects.requireNonNull(name);
		Objects.requireNonNull(password);

		if (name.isEmpty()) {
			throw new EmptyNamePlayerException(name);
		}

		if (password.isEmpty()) {
			throw new EmptyPasswordPlayerException();
		}

		if (this.playerRepository.existsByName(name)) {
			throw new DuplicatePlayerException(name);
		}

		return this.playerRepository.save(
				new Player(name, DigestUtils.md5Hex(password))
		);
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
	public Player find(final String name, final String password)
			throws NotFoundPlayerException {
		Objects.requireNonNull(name);
		Objects.requireNonNull(password);

		Optional<Player> player = this.playerRepository.find(name, password);

		return player.orElseThrow(() -> new NotFoundPlayerException(name));
	}
}
