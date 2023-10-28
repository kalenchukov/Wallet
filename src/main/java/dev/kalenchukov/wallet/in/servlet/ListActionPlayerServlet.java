/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.core.JacksonException;
import dev.kalenchukov.wallet.dto.action.ActionDto;
import dev.kalenchukov.wallet.dto.action.ListActionDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.entity.mappers.ActionMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.token.InvalidAccessTokenException;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.servlet.validation.Validation;
import dev.kalenchukov.wallet.in.servlet.validation.validators.AccessTokenValidator;
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
 * Класс обработки HTTP-запросов по адресу "/players/actions/list".
 */
public class ListActionPlayerServlet extends AbstractServlet {
	/**
	 * Конструирует сервлет.
	 *
	 * @param actionService Сервис действий.
	 */
	public ListActionPlayerServlet(final ActionService actionService) {
		super(actionService);
	}

	/**
	 * Возвращает список действий игрока.
	 *
	 * @param request  запрос.
	 * @param response ответ.
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
		try (ServletInputStream bodyReader = request.getInputStream()) {
			ListActionDto listActionDto = this.validation(
					this.objectMapper.readValue(bodyReader.readAllBytes(), ListActionDto.class)
			);

			long playerId = AuthToken.verifyToken(listActionDto.getAccessToken());

			List<Action> actions = this.actionService.find(playerId);

			List<ActionDto> actionsDto = new ArrayList<>();
			for (Action action : actions) {
				actionsDto.add(Mappers.getMapper(ActionMapper.class).toDto(action));
			}

			this.generateResponse(actionsDto, response, HttpServletResponse.SC_OK);
			this.fixAction(playerId, ActionType.ACTIONS_LIST, ActionType.Status.SUCCESS);
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
	private ListActionDto validation(final ListActionDto dto)
			throws InvalidAccessTokenException {
		if (!Validation.isValid(dto.getAccessToken(), new AccessTokenValidator())) {
			throw new InvalidAccessTokenException(dto.getAccessToken());
		}

		return dto;
	}
}
