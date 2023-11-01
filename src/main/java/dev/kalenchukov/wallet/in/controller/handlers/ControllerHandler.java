/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller.handlers;

import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Класс обработчика исключений контроллеров.
 */
@RestControllerAdvice
public class ControllerHandler extends ResponseEntityExceptionHandler {
	/**
	 * Конструирует {@code ControllerHandler}.
	 */
	public ControllerHandler() {
		super();
	}

	/**
	 * Возвращает информацию о нарушении.
	 *
	 * @param exception исключение.
	 * @return нарушение.
	 */
	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ViolationDto> handleApplicationException(final ApplicationException exception) {
		return ResponseEntity.status(exception.getHttpCode())
				.body(new ViolationDto(exception.getMessage()));
	}
}
