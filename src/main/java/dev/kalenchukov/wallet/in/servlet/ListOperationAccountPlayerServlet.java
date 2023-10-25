/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.core.JacksonException;
import dev.kalenchukov.wallet.dto.operation.ListOperationDto;
import dev.kalenchukov.wallet.dto.operation.OperationDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.entity.mappers.OperationMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.account.InvalidIdAccountException;
import dev.kalenchukov.wallet.exceptions.token.InvalidAccessTokenException;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.OperationService;
import dev.kalenchukov.wallet.in.servlet.validation.Validation;
import dev.kalenchukov.wallet.in.servlet.validation.validators.AccessTokenValidator;
import dev.kalenchukov.wallet.in.servlet.validation.validators.IdValidator;
import dev.kalenchukov.wallet.modules.AuthToken;
import dev.kalenchukov.wallet.type.ActionType;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс обработки HTTP-запросов по адресу "/players/accounts/operations/list".
 */
public class ListOperationAccountPlayerServlet extends AbstractServlet {
	/**
	 * Сервис операций.
	 */
	private final OperationService operationService;

	/**
	 * Конструирует сервлет.
	 *
	 * @param actionService Сервис действий.
	 * @param operationService Сервис операций.
	 */
	public ListOperationAccountPlayerServlet(final ActionService actionService, final OperationService operationService) {
		super(actionService);
		this.operationService = operationService;
	}

	/**
	 * Возвращает список операций со счётом.
	 *
	 * @param request  запрос.
	 * @param response ответ.
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
		try (ServletInputStream bodyReader = request.getInputStream()) {
			ListOperationDto listOperationDto = this.validation(
					this.objectMapper.readValue(bodyReader.readAllBytes(), ListOperationDto.class)
			);

			long playerId = AuthToken.verifyToken(listOperationDto.getAccessToken());

			List<Operation> operations = this.operationService.find(
					listOperationDto.getAccountId(),
					playerId
			);

			this.generateResponse(this.mapping(operations), response, HttpServletResponse.SC_OK);
			this.fixAction(playerId, ActionType.OPERATIONS_ACCOUNT_LIST, ActionType.Status.SUCCESS);
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
	 * @throws InvalidAccessTokenException если токен доступа не корректен.
	 */
	private ListOperationDto validation(final ListOperationDto dto)
			throws InvalidAccessTokenException, InvalidIdAccountException {
		if (!Validation.isValid(dto.getAccessToken(), new AccessTokenValidator())) {
			throw new InvalidAccessTokenException(dto.getAccessToken());
		}

		if (!Validation.isValid(dto.getAccountId(), new IdValidator())) {
			throw new InvalidIdAccountException(dto.getAccountId());
		}

		return dto;
	}

	/**
	 * Преобразовывает сущности операций для транспортировки.
	 *
	 * @param operations сущности операций.
	 * @return операции для транспортировки.
	 */
	private List<OperationDto> mapping(final List<Operation> operations) {
		OperationMapper operationDtoMapper = Mappers.getMapper(OperationMapper.class);

		List<OperationDto> operationsDto = new ArrayList<>();
		for (Operation operation : operations) {
			operationsDto.add(operationDtoMapper.toDto(operation));
		}

		return operationsDto;
	}
}
