/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import dev.kalenchukov.wallet.type.ActionType;

import java.util.Objects;

/**
 * Класс действий.
 */
public final class Action {
	/**
	 * Идентификатор.
	 */
	private final long actionId;

	/**
	 * Идентификатор игрока.
	 */
	private final long playerId;

	/**
	 * Тип.
	 */
	private final ActionType actionType;

	/**
	 * Статус.
	 */
	private final ActionType.Status actionTypeStatus;

	/**
	 * Конструирует действие.
	 *
	 * @param playerId         идентификатор игрока.
	 * @param actionType       тип.
	 * @param actionTypeStatus статус.
	 */
	public Action(final long playerId, final ActionType actionType, final ActionType.Status actionTypeStatus) {
		this(0L, playerId, actionType, actionTypeStatus);
	}

	/**
	 * Конструирует действие.
	 *
	 * @param actionId         идентификатор действия.
	 * @param playerId         идентификатор игрока.
	 * @param actionType       тип.
	 * @param actionTypeStatus статус.
	 */
	public Action(final long actionId, final long playerId, final ActionType actionType, final ActionType.Status actionTypeStatus) {
		Objects.requireNonNull(actionType);
		Objects.requireNonNull(actionTypeStatus);

		this.actionId = actionId;
		this.playerId = playerId;
		this.actionType = actionType;
		this.actionTypeStatus = actionTypeStatus;
	}

	/**
	 * Возвращает идентификатор.
	 *
	 * @return идентификатор.
	 */
	public long getActionId() {
		return this.actionId;
	}

	/**
	 * Возвращает идентификатор игрока.
	 *
	 * @return игрока.
	 */
	public long getPlayerId() {
		return this.playerId;
	}

	/**
	 * Возвращает тип.
	 *
	 * @return тип.
	 */
	public ActionType getActionType() {
		return this.actionType;
	}

	/**
	 * Возвращает статус.
	 *
	 * @return статус.
	 */
	public ActionType.Status getActionTypeStatus() {
		return this.actionTypeStatus;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Action{" +
				"actionId='" + this.actionId + "', " +
				"playerId='" + this.playerId + "', " +
				"actionType='" + this.actionType + "', " +
				"actionTypeStatus='" + this.actionTypeStatus + "'" +
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

		result = 31 * result + Objects.hashCode(this.actionId);

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

		Action action = (Action) obj;

		if (!Objects.equals(this.actionId, action.getActionId())) {
			return false;
		}

		return true;
	}
}
