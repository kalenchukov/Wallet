/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.loggingexecutemethod;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Класс аспекта для записи в лог времени выполнения методов.
 */
@Component
@Aspect
public class ExecuteMethodAspect {
	/**
	 * Логгер.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ExecuteMethodAspect.class);

	/**
	 * Срез отбора методов, выполнение которых необходимо отражать в логах.
	 *
	 * @param proceedingJoinPoint точка соединения.
	 * @return значение результата целевого метода.
	 * @throws Throwable если выполнить целевой метод не удалось.
	 */
	@Around("execution(* dev.kalenchukov.wallet..*(..))")
	public Object executeMethod(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object object = proceedingJoinPoint.proceed();
		long spentTime = (System.currentTimeMillis() - startTime);
		LOG.info("Метод '" + proceedingJoinPoint.getSignature() + "' выполнен за " + spentTime + " миллисекунд.");
		return object;
	}
}
