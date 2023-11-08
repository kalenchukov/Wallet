/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.starter.fixaction.annotations.FixAction;
import dev.kalenchukov.starter.fixaction.types.ActionType;
import dev.kalenchukov.wallet.auth.AuthToken;
import dev.kalenchukov.wallet.dto.OperationDto;
import dev.kalenchukov.wallet.dto.ViolationDto;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.entity.mappers.OperationMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.NoAccessPlayerException;
import dev.kalenchukov.wallet.in.service.OperationService;
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
 * Класс обработки HTTP-запросов по операциям счетов игроков.
 */
@RestController
@RequestMapping(path = "/players/{playerId}/accounts/{accountId}/operations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Управление операциями счетов игроков")
public class OperationAccountPlayerController {
	/**
	 * Сервис операций.
	 */
	private final OperationService operationService;

	/**
	 * Авторизационные токены.
	 */
	private final AuthToken authToken;

	/**
	 * Конструирует контроллер.
	 *
	 * @param operationService сервис операций.
	 * @param authToken        авторизационные токены.
	 */
	@Autowired
	public OperationAccountPlayerController(final OperationService operationService, final AuthToken authToken) {
		this.operationService = operationService;
		this.authToken = authToken;
	}

	/**
	 * Возвращает информацию об операции со счётом.
	 *
	 * @param playerId    идентификатор игрока.
	 * @param accountId   идентификатор счёта.
	 * @param operationId идентификатор операции.
	 * @param accessToken токен доступа.
	 * @return операцию.
	 * @throws ApplicationException если произошла ошибка при выполнении данного запроса.
	 */
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Если запрос выполнен успешно"),
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))}),
			@ApiResponse(responseCode = "401", description = "Если необходимо пройти авторизацию", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))}),
			@ApiResponse(responseCode = "403", description = "Если доступ отсутствует", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))}),
			@ApiResponse(responseCode = "404", description = "Если операция счёта игрока не существует", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))})
	})
	@io.swagger.v3.oas.annotations.Operation(summary = "Получение информации об операции со счётом", description = "Позволяет получить информацию об операции со счётом")
	@FixAction(actionType = ActionType.OPERATION_ACCOUNT)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/{operationId}")
	public ResponseEntity<OperationDto> get(
			@Parameter(description = "Идентификатор игрока") @PathVariable final long playerId,
			@Parameter(description = "Идентификатор счёта") @PathVariable final long accountId,
			@Parameter(description = "Идентификатор операции") @PathVariable final long operationId,
			@Parameter(description = "Токен доступа") @RequestHeader("Authorization") final String accessToken
	) throws ApplicationException {
		if (playerId != this.authToken.verifyToken(accessToken)) {
			throw new NoAccessPlayerException(playerId);
		}

		Operation operation = this.operationService.findById(playerId, accountId, operationId);
		OperationDto operationDto = Mappers.getMapper(OperationMapper.class).toDto(operation);

		return ResponseEntity.status(HttpStatus.OK).body(operationDto);
	}

	/**
	 * Возвращает список операций со счётом.
	 *
	 * @param playerId    идентификатор игрока.
	 * @param accountId   идентификатор счёта.
	 * @param accessToken токен доступа.
	 * @return список операций.
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
	@io.swagger.v3.oas.annotations.Operation(summary = "Получение списка операций со счётом", description = "Позволяет получить список операций со счётом")
	@FixAction(actionType = ActionType.OPERATIONS_ACCOUNT)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public ResponseEntity<List<OperationDto>> find(
			@Parameter(description = "Идентификатор игрока") @PathVariable final long playerId,
			@Parameter(description = "Идентификатор счёта") @PathVariable final long accountId,
			@Parameter(description = "Токен доступа") @RequestHeader("Authorization") final String accessToken
	) throws ApplicationException {
		if (playerId != this.authToken.verifyToken(accessToken)) {
			throw new NoAccessPlayerException(playerId);
		}

		List<Operation> operations = this.operationService.find(playerId, accountId);
		List<OperationDto> operationsDto = new ArrayList<>();
		for (Operation operation : operations) {
			operationsDto.add(Mappers.getMapper(OperationMapper.class).toDto(operation));
		}

		return ResponseEntity.status(HttpStatus.OK).body(operationsDto);
	}
}
