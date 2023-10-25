/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.aop.aspects;

import dev.kalenchukov.wallet.type.ActionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.*;

/**
 * Класс аспекта при выполнении метода фиксации действия игрока.
 */
@Aspect
public class ExecuteFixActionMethodAspect {
	/**
	 * Логгер.
	 */
	private static final Logger LOG = LogManager.getLogger(ExecuteAllMethodAspect.class);

	/**
	 * Фиксирует выполнение метода с указанными параметрами.
	 *
	 * @param playerId идентификатор игрока.
	 * @param actionType тип действия.
	 * @param actionTypeStatus статус действия.
	 */
	@Pointcut(
			value = "execution(* dev.kalenchukov.wallet.in.servlet.*Servlet.fixAction(..)) && args(playerId,actionType,actionTypeStatus)",
			argNames = "playerId,actionType,actionTypeStatus"
	)
	public void executionFixAction(long playerId, ActionType actionType, ActionType.Status actionTypeStatus) {
	}

	/**
	 * Записывать выполнение действия игрока.
	 *
	 * @param playerId идентификатор игрока.
	 * @param actionType тип действия.
	 * @param actionTypeStatus статус действия.
	 */
	@Before(
			value = "executionFixAction(playerId,actionType,actionTypeStatus)",
			argNames = "playerId,actionType,actionTypeStatus"
	)
	public void executeMethodAnnotatedByFixAction(long playerId, ActionType actionType, ActionType.Status actionTypeStatus) {
		LOG.info("Игрок ID'" + playerId + "' выполнил действие " + actionType + " со статусом " + actionTypeStatus);
	}
}
