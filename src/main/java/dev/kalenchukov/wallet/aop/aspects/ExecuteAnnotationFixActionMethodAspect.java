/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.aop.aspects;

import dev.kalenchukov.wallet.aop.annotations.FixAction;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.modules.DataBase;
import dev.kalenchukov.wallet.properties.Props;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.type.ActionType;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Класс аспекта для фиксации действия игрока.
 */
@Aspect
public class ExecuteAnnotationFixActionMethodAspect {
	/**
	 * Срез отбора методов, выполнение которых необходимо отражать в действиях игрока.
	 *
	 * @param fixAction аннотация описывающая тип действия.
	 * @param playerId  идентификатор игрока.
	 */
	@Pointcut(
			value = "@annotation(fixAction) && " +
					"execution(* dev.kalenchukov.wallet.in.controller.*Controller.*(..)) && " +
					"args(playerId,..)",
			argNames = "fixAction,playerId"
	)
	public void executedAnnotatedService(final FixAction fixAction, final long playerId) {

	}

	/**
	 * Добавляет удачное действие игрока.
	 *
	 * @param fixAction аннотация описывающая тип действия.
	 * @param playerId  идентификатор игрока.
	 */
	@AfterReturning(
			value = "executedAnnotatedService(fixAction, playerId)",
			argNames = "fixAction,playerId"
	)
	public void addActionSuccess(final FixAction fixAction, final long playerId) {
		this.addAction(playerId, fixAction.actionType(), ActionType.Status.SUCCESS);
	}

	/**
	 * Добавляет неудачное действие игрока.
	 *
	 * @param fixAction аннотация описывающая тип действия.
	 * @param playerId  идентификатор игрока.
	 */
	@AfterThrowing(
			value = "executedAnnotatedService(fixAction, playerId)",
			argNames = "fixAction,playerId"
	)
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
		if ("production".equals(Props.get().getContext())) {
			ActionService actionService = new ActionServiceImpl(
					new ActionRepositoryImpl(DataBase.getDataSource())
			);
			actionService.add(playerId, actionType, status);
		}
	}
}
