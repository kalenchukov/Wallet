/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.aop.aspects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Класс аспекта для фиксации выполнения методов.
 */
@Aspect
public class ExecuteAllMethodAspect {
	/**
	 * Логгер.
	 */
	private static final Logger LOG = LogManager.getLogger(ExecuteAllMethodAspect.class);

	/**
	 * Срез отбора методов, выполнение которых необходимо отражать в логах.
	 *
	 * @param proceedingJoinPoint точка соединения.
	 * @return значение результата целевого метода.
	 * @throws Throwable если выполнить целевой метод не удалось.
	 */
	@Around("execution(* *(..))")
	public Object executeAnyMethod(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object object = proceedingJoinPoint.proceed();
		long spentTime = (System.currentTimeMillis() - startTime);
		LOG.info("Метод '" + proceedingJoinPoint.getSignature() + "' выполнен за " + spentTime + " миллисекунд.");
		return object;
	}
}
