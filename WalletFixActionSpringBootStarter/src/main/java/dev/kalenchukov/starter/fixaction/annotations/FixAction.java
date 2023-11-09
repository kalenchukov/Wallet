/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.fixaction.annotations;

import dev.kalenchukov.starter.fixaction.types.ActionType;

import java.lang.annotation.*;

/**
 * Аннотация пометки метода, позволяющая указать на необходимость фиксации действия игрока.
 * Данная аннотация должна использоваться на методах, которые принимают в качестве первого параметра {@code playerId}.
 * Пример:
 * <pre>
 *     Account createAccount(long playerId...) {
 * 	...
 *     }
 * </pre>
 *
 * @see EnableFixAction
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FixAction {
	/**
	 * Возвращает тип действия.
	 *
	 * @return тип действия.
	 */
	ActionType actionType();
}
