/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.wallet.aop.annotations.FixAction;
import dev.kalenchukov.wallet.dto.action.ActionDto;
import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.entity.mappers.ActionMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.InvalidAccessTokenException;
import dev.kalenchukov.wallet.exceptions.NoAccessPlayerException;
import dev.kalenchukov.wallet.in.controller.validation.Validation;
import dev.kalenchukov.wallet.in.controller.validation.validators.AccessTokenValidator;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.modules.AuthToken;
import dev.kalenchukov.wallet.type.ActionType;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.mapstruct.factory.Mappers;
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
@RequestMapping(
		path = "/players/{playerId}/actions",
		produces = MediaType.APPLICATION_JSON_VALUE
)
@Api(tags = "Управление действиями игроков")
public class ActionPlayerController {
	/**
	 * Сервис действий.
	 */
	private final ActionService actionService;

	/**
	 * Конструирует контроллер.
	 *
	 * @param actionService сервис действий.
	 */
	public ActionPlayerController(final ActionService actionService) {
		this.actionService = actionService;
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
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны"),
			@ApiResponse(responseCode = "401", description = "Если необходимо пройти авторизацию"),
			@ApiResponse(responseCode = "403", description = "Если доступ отсутствует")
	})
	@io.swagger.v3.oas.annotations.Operation(
			summary = "Получение списка действий",
			description = "Позволяет получить список действий"
	)
	@FixAction(actionType = ActionType.ACTIONS)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public ResponseEntity<List<ActionDto>> find(
			@Parameter(description = "Идентификатор игрока") @PathVariable final long playerId,
			@Parameter(description = "Токен доступа") @RequestHeader("Authorization") final String accessToken)
			throws ApplicationException {
		if (!Validation.isValid(accessToken, new AccessTokenValidator())) {
			throw new InvalidAccessTokenException(accessToken);
		}

		if (playerId != AuthToken.verifyToken(accessToken)) {
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
