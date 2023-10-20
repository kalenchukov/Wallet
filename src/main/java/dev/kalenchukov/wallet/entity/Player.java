/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import java.util.Objects;

/**
 * Класс игрока.
 */
public final class Player {
	/**
	 * Идентификатор.
	 */
	private final long playerId;

	/**
	 * Имя.
	 */
	private final String name;

	/**
	 * Пароль.
	 */
	private final String password;

	/**
	 * Конструирует игрока.
	 *
	 * @param name     имя.
	 * @param password пароль.
	 */
	public Player(final String name, final String password) {
		this(0L, name, password);
	}

	/**
	 * Конструирует игрока.
	 *
	 * @param playerId идентификатор.
	 * @param name     имя.
	 * @param password пароль.
	 */
	public Player(final long playerId, final String name, final String password) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(password);

		this.playerId = playerId;
		this.name = name;
		this.password = password;
	}

	/**
	 * Возвращает идентификатор.
	 *
	 * @return идентификатор.
	 */
	public long getPlayerId() {
		return this.playerId;
	}

	/**
	 * Возвращает имя.
	 *
	 * @return имя.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Возвращает пароль.
	 *
	 * @return пароль.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Player{" +
				"playerId='" + this.playerId + "', " +
				"name='" + this.name + "', " +
				"password='" + this.password + "'" +
				'}';
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int result = 0;

		result = 31 * result + Objects.hashCode(this.playerId);

		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param obj {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}

		Player player = (Player) obj;

		if (!Objects.equals(this.playerId, player.getPlayerId())) {
			return false;
		}

		return true;
	}
}
