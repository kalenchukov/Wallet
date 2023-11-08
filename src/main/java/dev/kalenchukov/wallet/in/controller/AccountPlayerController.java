/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.starter.fixaction.annotations.FixAction;
import dev.kalenchukov.starter.fixaction.types.ActionType;
import dev.kalenchukov.wallet.auth.AuthToken;
import dev.kalenchukov.wallet.dto.*;
import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.entity.mappers.AccountMapper;
import dev.kalenchukov.wallet.entity.mappers.OperationMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.NoAccessPlayerException;
import dev.kalenchukov.wallet.in.service.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Класс обработки HTTP-запросов по счетам игроков.
 */
@RestController
@RequestMapping(path = "/players/{playerId}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Управление счетами игроков")
public class AccountPlayerController {
	/**
	 * Сервис счетов.
	 */
	private final AccountService accountService;

	/**
	 * Авторизационные токены.
	 */
	private final AuthToken authToken;

	/**
	 * Конструирует контроллер.
	 *
	 * @param accountService сервис счетов.
	 * @param authToken      авторизационные токены.
	 */
	@Autowired
	public AccountPlayerController(final AccountService accountService, final AuthToken authToken) {
		this.accountService = accountService;
		this.authToken = authToken;
	}

	/**
	 * Выполняет добавление счёта.
	 *
	 * @param playerId    идентификатор игрока.
	 * @param accessToken токен доступа.
	 * @return счёт.
	 * @throws ApplicationException если произошла ошибка при выполнении данного запроса.
	 */
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Если запрос выполнен успешно"),
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))}),
			@ApiResponse(responseCode = "401", description = "Если необходимо пройти авторизацию", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))})
	})
	@io.swagger.v3.oas.annotations.Operation(summary = "Добавление счёта", description = "Позволяет добавить счёт")
	@FixAction(actionType = ActionType.CREATE_ACCOUNT)
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ResponseEntity<AccountDto> create(
			@Parameter(description = "Идентификатор игрока") @PathVariable final long playerId,
			@Parameter(description = "Токен доступа") @RequestHeader("Authorization") final String accessToken
	) throws ApplicationException {
		if (playerId != this.authToken.verifyToken(accessToken)) {
			throw new NoAccessPlayerException(playerId);
		}

		Account account = this.accountService.add(playerId);
		AccountDto accountDto = Mappers.getMapper(AccountMapper.class).toDto(account);

		return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
	}

	/**
	 * Выполняет пополнение счёта.
	 *
	 * @param playerId         идентификатор игрока.
	 * @param accountId        идентификатор счёта.
	 * @param accessToken      токен доступа.
	 * @param creditAccountDto данные пополнения счёта.
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
			@ApiResponse(responseCode = "404", description = "Если счёта игрока не существует", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))})
	})
	@io.swagger.v3.oas.annotations.Operation(summary = "Пополнение счёта", description = "Позволяет пополнить счёт")
	@FixAction(actionType = ActionType.CREDIT_ACCOUNT)
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(path = "/{accountId}/credit", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OperationDto> credit(
			@Parameter(description = "Идентификатор игрока") @PathVariable final long playerId,
			@Parameter(description = "Идентификатор счёта") @PathVariable final long accountId,
			@Parameter(description = "Токен доступа") @RequestHeader("Authorization") final String accessToken,
			@Valid @RequestBody final CreditAccountDto creditAccountDto
	) throws ApplicationException {
		if (playerId != this.authToken.verifyToken(accessToken)) {
			throw new NoAccessPlayerException(playerId);
		}

		Operation operation = this.accountService.credit(playerId, accountId, creditAccountDto.getAmount());
		OperationDto operationDto = Mappers.getMapper(OperationMapper.class).toDto(operation);

		return ResponseEntity.status(HttpStatus.OK).body(operationDto);
	}

	/**
	 * Выполняет списание со счёта.
	 *
	 * @param playerId        идентификатор игрока.
	 * @param accountId       идентификатор счёта.
	 * @param accessToken     токен доступа.
	 * @param debitAccountDto данные списания со счёта.
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
			@ApiResponse(responseCode = "404", description = "Если счёта игрока не существует", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))})
	})
	@io.swagger.v3.oas.annotations.Operation(summary = "Списание со счёта", description = "Позволяет списать со счёта")
	@FixAction(actionType = ActionType.DEBIT_ACCOUNT)
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(path = "/{accountId}/debit", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OperationDto> debit(
			@Parameter(description = "Идентификатор игрока") @PathVariable final long playerId,
			@Parameter(description = "Идентификатор счёта") @PathVariable final long accountId,
			@Parameter(description = "Токен доступа") @RequestHeader("Authorization") final String accessToken,
			@Valid @RequestBody final DebitAccountDto debitAccountDto
	) throws ApplicationException {
		if (playerId != this.authToken.verifyToken(accessToken)) {
			throw new NoAccessPlayerException(playerId);
		}

		Operation operation = this.accountService.debit(playerId, accountId, debitAccountDto.getAmount());
		OperationDto operationDto = Mappers.getMapper(OperationMapper.class).toDto(operation);

		return ResponseEntity.status(HttpStatus.OK).body(operationDto);
	}
}
