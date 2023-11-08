/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.starter.fixaction.annotations.FixAction;
import dev.kalenchukov.starter.fixaction.entity.Action;
import dev.kalenchukov.starter.fixaction.types.ActionType;
import dev.kalenchukov.wallet.auth.AuthToken;
import dev.kalenchukov.wallet.dto.ActionDto;
import dev.kalenchukov.wallet.dto.ViolationDto;
import dev.kalenchukov.wallet.entity.mappers.ActionMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.NoAccessPlayerException;
import dev.kalenchukov.wallet.in.service.ActionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс обработки HTTP-запросов по действиям игроков.
 */
@RestController
@RequestMapping(path = "/players/{playerId}/actions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Управление действиями игроков")
public class ActionPlayerController {
	/**
	 * Сервис действий.
	 */
	private final ActionService actionService;

	/**
	 * Авторизационные токены.
	 */
	private final AuthToken authToken;

	/**
	 * Конструирует контроллер.
	 *
	 * @param actionService сервис действий.
	 * @param authToken     авторизационные токены.
	 */
	@Autowired
	public ActionPlayerController(final ActionService actionService, final AuthToken authToken) {
		this.actionService = actionService;
		this.authToken = authToken;
	}

	/**
	 * Возвращает список действий игрока.
	 *
	 * @param playerId    идентификатор игрока.
	 * @param accessToken токен доступа.
	 * @return список действий.
	 * @throws ApplicationException если произошла ошибка при выполнении данного запроса.
	 */
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Если запрос выполнен успешно"),
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))}),
			@ApiResponse(responseCode = "401", description = "Если необходимо пройти авторизацию", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))}),
			@ApiResponse(responseCode = "403", description = "Если доступ отсутствует", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))})
	})
	@io.swagger.v3.oas.annotations.Operation(summary = "Получение списка действий", description = "Позволяет получить список действий")
	@FixAction(actionType = ActionType.ACTIONS)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public ResponseEntity<List<ActionDto>> find(
			@Parameter(description = "Идентификатор игрока") @PathVariable final long playerId,
			@Parameter(description = "Токен доступа") @RequestHeader("Authorization") final String accessToken
	) throws ApplicationException {
		if (playerId != this.authToken.verifyToken(accessToken)) {
			throw new NoAccessPlayerException(playerId);
		}

		List<Action> actions = this.actionService.find(playerId);
		List<ActionDto> actionsDto = new ArrayList<>();
		for (Action action : actions) {
			actionsDto.add(Mappers.getMapper(ActionMapper.class).toDto(action));
		}

		return ResponseEntity.status(HttpStatus.OK).body(actionsDto);
	}
}
