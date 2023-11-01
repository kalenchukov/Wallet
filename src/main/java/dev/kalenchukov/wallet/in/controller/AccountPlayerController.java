/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.wallet.aop.annotations.FixAction;
import dev.kalenchukov.wallet.dto.account.AccountDto;
import dev.kalenchukov.wallet.dto.account.CreditAccountDto;
import dev.kalenchukov.wallet.dto.account.DebitAccountDto;
import dev.kalenchukov.wallet.dto.operation.OperationDto;
import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.entity.mappers.AccountMapper;
import dev.kalenchukov.wallet.entity.mappers.OperationMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.InvalidAccessTokenException;
import dev.kalenchukov.wallet.exceptions.InvalidAmountOperationException;
import dev.kalenchukov.wallet.exceptions.NoAccessPlayerException;
import dev.kalenchukov.wallet.in.controller.validation.Validation;
import dev.kalenchukov.wallet.in.controller.validation.validators.AccessTokenValidator;
import dev.kalenchukov.wallet.in.controller.validation.validators.AmountValidator;
import dev.kalenchukov.wallet.in.service.AccountService;
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

/**
 * Класс обработки HTTP-запросов по счетам игроков.
 */
@RestController
@RequestMapping(
		path = "/players/{playerId}/accounts",
		produces = MediaType.APPLICATION_JSON_VALUE
)
@Api(tags = "Управление счетами игроков")
public class AccountPlayerController {
	/**
	 * Сервис счетов.
	 */
	private final AccountService accountService;

	/**
	 * Конструирует контроллер.
	 *
	 * @param accountService сервис счетов.
	 */
	public AccountPlayerController(final AccountService accountService) {
		this.accountService = accountService;
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
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны"),
			@ApiResponse(responseCode = "401", description = "Если необходимо пройти авторизацию")
	})
	@io.swagger.v3.oas.annotations.Operation(
			summary = "Добавление счёта",
			description = "Позволяет добавить счёт"
	)
	@FixAction(actionType = ActionType.CREATE_ACCOUNT)
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ResponseEntity<AccountDto> create(
			@Parameter(description = "Идентификатор игрока") @PathVariable final long playerId,
			@Parameter(description = "Токен доступа") @RequestHeader("Authorization") final String accessToken)
			throws ApplicationException {
		if (!Validation.isValid(accessToken, new AccessTokenValidator())) {
			throw new InvalidAccessTokenException(accessToken);
		}

		if (playerId != AuthToken.verifyToken(accessToken)) {
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
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны"),
			@ApiResponse(responseCode = "401", description = "Если необходимо пройти авторизацию"),
			@ApiResponse(responseCode = "403", description = "Если доступ отсутствует"),
			@ApiResponse(responseCode = "404", description = "Если счёта игрока не существует")
	})
	@io.swagger.v3.oas.annotations.Operation(
			summary = "Пополнение счёта",
			description = "Позволяет пополнить счёт"
	)
	@FixAction(actionType = ActionType.CREDIT_ACCOUNT)
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(path = "/{accountId}/credit", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OperationDto> credit(
			@Parameter(description = "Идентификатор игрока") @PathVariable final long playerId,
			@Parameter(description = "Идентификатор счёта") @PathVariable final long accountId,
			@Parameter(description = "Токен доступа") @RequestHeader("Authorization") final String accessToken,
			@RequestBody final CreditAccountDto creditAccountDto)
			throws ApplicationException {
		if (!Validation.isValid(accessToken, new AccessTokenValidator())) {
			throw new InvalidAccessTokenException(accessToken);
		}

		if (!Validation.isValid(creditAccountDto.getAmount(), new AmountValidator())) {
			throw new InvalidAmountOperationException(creditAccountDto.getAmount());
		}

		if (playerId != AuthToken.verifyToken(accessToken)) {
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
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны"),
			@ApiResponse(responseCode = "401", description = "Если необходимо пройти авторизацию"),
			@ApiResponse(responseCode = "403", description = "Если доступ отсутствует"),
			@ApiResponse(responseCode = "404", description = "Если счёта игрока не существует")
	})
	@io.swagger.v3.oas.annotations.Operation(
			summary = "Списание со счёта",
			description = "Позволяет списать со счёта"
	)
	@FixAction(actionType = ActionType.DEBIT_ACCOUNT)
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(path = "/{accountId}/debit", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OperationDto> debit(
			@Parameter(description = "Идентификатор игрока") @PathVariable final long playerId,
			@Parameter(description = "Идентификатор счёта") @PathVariable final long accountId,
			@Parameter(description = "Токен доступа") @RequestHeader("Authorization") final String accessToken,
			@RequestBody final DebitAccountDto debitAccountDto) throws ApplicationException {
		if (!Validation.isValid(accessToken, new AccessTokenValidator())) {
			throw new InvalidAccessTokenException(accessToken);
		}

		if (!Validation.isValid(debitAccountDto.getAmount(), new AmountValidator())) {
			throw new InvalidAmountOperationException(debitAccountDto.getAmount());
		}

		if (playerId != AuthToken.verifyToken(accessToken)) {
			throw new NoAccessPlayerException(playerId);
		}

		Operation operation = this.accountService.debit(playerId, accountId, debitAccountDto.getAmount());
		OperationDto operationDto = Mappers.getMapper(OperationMapper.class).toDto(operation);

		return ResponseEntity.status(HttpStatus.OK).body(operationDto);
	}
}
