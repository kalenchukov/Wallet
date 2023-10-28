/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.core.JacksonException;
import dev.kalenchukov.wallet.dto.operation.GetOperationDto;
import dev.kalenchukov.wallet.dto.operation.OperationDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.entity.mappers.OperationMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.operation.InvalidIdOperationException;
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

/**
 * Класс обработки HTTP-запросов по адресу "/players/accounts/operations".
 */
public class OperationAccountPlayerServlet extends AbstractServlet {
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
	public OperationAccountPlayerServlet(final ActionService actionService, final OperationService operationService) {
		super(actionService);
		this.operationService = operationService;
	}

	/**
	 * Возвращает информацию об операции со счётом.
	 *
	 * @param request  запрос.
	 * @param response ответ.
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
		try (ServletInputStream bodyReader = request.getInputStream()) {
			GetOperationDto getOperationDto = this.validation(
					this.objectMapper.readValue(bodyReader.readAllBytes(), GetOperationDto.class)
			);

			long playerId = AuthToken.verifyToken(getOperationDto.getAccessToken());

			Operation operation = this.operationService.findById(
					getOperationDto.getOperationId(),
					playerId
			);

			OperationDto operationDto = Mappers.getMapper(OperationMapper.class)
					.toDto(operation);

			this.generateResponse(operationDto, response, HttpServletResponse.SC_OK);
			this.fixAction(playerId, ActionType.OPERATION_ACCOUNT, ActionType.Status.SUCCESS);
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
	private GetOperationDto validation(final GetOperationDto dto)
			throws InvalidAccessTokenException, InvalidIdOperationException {
		if (!Validation.isValid(dto.getAccessToken(), new AccessTokenValidator())) {
			throw new InvalidAccessTokenException(dto.getAccessToken());
		}

		if (!Validation.isValid(dto.getOperationId(), new IdValidator())) {
			throw new InvalidIdOperationException(dto.getOperationId());
		}

		return dto;
	}
}
