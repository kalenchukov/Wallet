/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.fixaction;

import dev.kalenchukov.starter.fixaction.annotations.FixAction;
import dev.kalenchukov.starter.fixaction.service.FixActionService;
import dev.kalenchukov.starter.fixaction.types.ActionType;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Класс аспекта фиксации действия игроков.
 */
@Component
@Aspect
public class FixActionAspect {
	/**
	 * Сервис действий.
	 */
	private final FixActionService fixActionService;

	/**
	 * Конструирует аспект.
	 *
	 * @param fixActionService сервис действий.
	 */
	@Autowired
	public FixActionAspect(final FixActionService fixActionService) {
		this.fixActionService = fixActionService;
	}

	/**
	 * Срез отбора методов, выполнение которых необходимо отражать в действиях игрока.
	 *
	 * @param fixAction аннотация описывающая тип действия.
	 * @param playerId  идентификатор игрока.
	 */
	@Pointcut(value = "@annotation(fixAction) && " + "execution(* dev.kalenchukov.wallet.in.controller.*Controller.*(..)) && " + "args(playerId,..)", argNames = "fixAction,playerId")
	public void executedAnnotatedService(final FixAction fixAction, final long playerId) {

	}

	/**
	 * Добавляет удачное действие игрока.
	 *
	 * @param fixAction аннотация описывающая тип действия.
	 * @param playerId  идентификатор игрока.
	 */
	@AfterReturning(value = "executedAnnotatedService(fixAction, playerId)", argNames = "fixAction,playerId")
	public void addActionSuccess(final FixAction fixAction, final long playerId) {
		this.addAction(playerId, fixAction.actionType(), ActionType.Status.SUCCESS);
	}

	/**
	 * Добавляет неудачное действие игрока.
	 *
	 * @param fixAction аннотация описывающая тип действия.
	 * @param playerId  идентификатор игрока.
	 */
	@AfterThrowing(value = "executedAnnotatedService(fixAction, playerId)", argNames = "fixAction,playerId")
	public void addActionFail(final FixAction fixAction, final long playerId) {
		this.addAction(playerId, fixAction.actionType(), ActionType.Status.FAIL);
	}

	/**
	 * Добавляет действие игрока в репозиторий.
	 *
	 * @param playerId   идентификатор игрока.
	 * @param actionType тип действия.
	 * @param status     статус действия.
	 */
	private void addAction(final long playerId, final ActionType actionType, final ActionType.Status status) {
		this.fixActionService.add(playerId, actionType, status);
	}
}
