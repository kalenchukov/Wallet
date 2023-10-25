/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.core.JacksonException;
import dev.kalenchukov.wallet.dto.account.CreditAccountDto;
import dev.kalenchukov.wallet.dto.operation.OperationDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.entity.mappers.OperationMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.account.InvalidIdAccountException;
import dev.kalenchukov.wallet.exceptions.operation.InvalidAmountOperationException;
import dev.kalenchukov.wallet.exceptions.token.InvalidAccessTokenException;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.servlet.validation.Validation;
import dev.kalenchukov.wallet.in.servlet.validation.validators.AccessTokenValidator;
import dev.kalenchukov.wallet.in.servlet.validation.validators.AmountValidator;
import dev.kalenchukov.wallet.in.servlet.validation.validators.IdValidator;
import dev.kalenchukov.wallet.modules.AuthToken;
import dev.kalenchukov.wallet.type.ActionType;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;

import javax.sql.DataSource;

/**
 * Класс обработки HTTP-запросов по адресу "/players/accounts/credit".
 */
public class CreditAccountPlayerServlet extends AbstractServlet {
	/**
	 * Сервис счетов.
	 */
	private final AccountService accountService;

	/**
	 * Конструирует сервлет.
	 *
	 * @param actionService Сервис действий.
	 * @param accountService Сервис счетов.
	 */
	public CreditAccountPlayerServlet(final ActionService actionService, final AccountService accountService) {
		super(actionService);
		this.accountService = accountService;
	}

	/**
	 * Выполняет пополнение счёта.
	 *
	 * @param request  запрос.
	 * @param response ответ.
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
		try (ServletInputStream bodyReader = request.getInputStream()) {
			CreditAccountDto creditAccountDto = this.validation(
					this.objectMapper.readValue(bodyReader.readAllBytes(), CreditAccountDto.class)
			);

			long playerId = AuthToken.verifyToken(creditAccountDto.getAccessToken());

			Operation operation = this.accountService.credit(
					creditAccountDto.getAccountId(),
					playerId,
					creditAccountDto.getAmount()
			);

			this.generateResponse(this.mapping(operation), response, HttpServletResponse.SC_OK);
			this.fixAction(playerId, ActionType.CREDIT_ACCOUNT, ActionType.Status.SUCCESS);
		} catch (ApplicationException exception) {
			this.generateResponse(new ViolationDto(exception.getMessage()), response, exception.getHttpCode());
		} catch (JacksonException exception) {
			this.generateResponse(null, response, HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception exception) {
			this.generateResponse(null, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Проверяет корректность переданных данных.
	 *
	 * @param dto объект, данные которого необходимо проверить.
	 * @throws InvalidAccessTokenException     если токен доступа не корректен.
	 * @throws InvalidIdAccountException       если идентификатор счёта некорректен.
	 * @throws InvalidAmountOperationException если сумма операции некорректна.
	 */
	private CreditAccountDto validation(final CreditAccountDto dto)
			throws InvalidIdAccountException, InvalidAmountOperationException, InvalidAccessTokenException {
		if (!Validation.isValid(dto.getAmount(), new AmountValidator())) {
			throw new InvalidAmountOperationException(dto.getAmount());
		}

		if (!Validation.isValid(dto.getAccountId(), new IdValidator())) {
			throw new InvalidIdAccountException(dto.getAccountId());
		}
		if (!Validation.isValid(dto.getAccessToken(), new AccessTokenValidator())) {
			throw new InvalidAccessTokenException(dto.getAccessToken());
		}

		return dto;
	}

	/**
	 * Преобразовывает сущность операции для транспортировки.
	 *
	 * @param operation сущность операции.
	 * @return операция для транспортировки.
	 */
	private OperationDto mapping(final Operation operation) {
		OperationMapper operationDtoMapper = Mappers.getMapper(OperationMapper.class);
		return operationDtoMapper.toDto(operation);
	}
}
