/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller.handlers;

import dev.kalenchukov.wallet.dto.ViolationDto;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс обработчика исключений контроллеров.
 */
@RestControllerAdvice
public class ControllerHandler {
	/**
	 * Возвращает информацию о нарушении.
	 *
	 * @param exception исключение.
	 * @return нарушение.
	 */
	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ViolationDto> handleApplicationException(
			final ApplicationException exception
	) {
		return ResponseEntity.status(exception.getHttpCode()).body(new ViolationDto(exception.getMessage()));
	}

	/**
	 * Возвращает информацию о нарушении.
	 *
	 * @param exception исключение.
	 * @return нарушение.
	 */
	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ViolationDto> handleMissingRequestHeaderException(
			final MissingRequestHeaderException exception
	) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ViolationDto(exception.getMessage()));
	}

	/**
	 * Возвращает информацию о нарушении.
	 *
	 * @param exception исключение.
	 * @return нарушения.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ViolationDto>> handleMethodArgumentNotValidException(
			final MethodArgumentNotValidException exception
	) {
		List<ViolationDto> violations = new ArrayList<>();
		exception.getBindingResult().getAllErrors().forEach(violation -> {
			violations.add(new ViolationDto(violation.getDefaultMessage()));
		});

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(violations);
	}
}
