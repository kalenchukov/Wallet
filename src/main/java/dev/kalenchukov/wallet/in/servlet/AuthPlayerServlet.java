/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.core.JacksonException;
import dev.kalenchukov.wallet.dto.player.AuthPlayerDto;
import dev.kalenchukov.wallet.dto.token.AccessTokenDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.AccessToken;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.entity.mappers.AccessTokenMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.player.InvalidNamePlayerException;
import dev.kalenchukov.wallet.exceptions.player.InvalidPasswordPlayerException;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.in.servlet.validation.Validation;
import dev.kalenchukov.wallet.in.servlet.validation.validators.NameValidator;
import dev.kalenchukov.wallet.in.servlet.validation.validators.PasswordValidator;
import dev.kalenchukov.wallet.modules.AuthToken;
import dev.kalenchukov.wallet.type.ActionType;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.mapstruct.factory.Mappers;

import javax.sql.DataSource;

/**
 * Класс обработки HTTP-запросов по адресу "/players/auth".
 */
public class AuthPlayerServlet extends AbstractServlet {
	/**
	 * Сервис игроков.
	 */
	private final PlayerService playerService;

	/**
	 * Конструирует сервлет.
	 *
	 * @param actionService Сервис действий.
	 * @param playerService Сервис игроков.
	 */
	public AuthPlayerServlet(final ActionService actionService, final PlayerService playerService) {
		super(actionService);
		this.playerService = playerService;
	}

	/**
	 * Выполняет авторизацию игрока.
	 *
	 * @param request  запрос.
	 * @param response ответ.
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
		try (ServletInputStream bodyReader = request.getInputStream()) {
			AuthPlayerDto authPlayerDto = this.validation(
					this.objectMapper.readValue(bodyReader.readAllBytes(), AuthPlayerDto.class)
			);

			Player player = this.playerService.find(
					authPlayerDto.getName(),
					DigestUtils.md5Hex(authPlayerDto.getPassword())
			);

			AccessToken accessToken = new AccessToken(
					AuthToken.createToken(player.getPlayerId())
			);

			this.generateResponse(this.mapping(accessToken), response, HttpServletResponse.SC_OK);
			this.fixAction(player.getPlayerId(), ActionType.AUTH, ActionType.Status.SUCCESS);
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
	 * @throws InvalidNamePlayerException     если имя игрока некорректно.
	 * @throws InvalidPasswordPlayerException если пароль игрока некорректен.
	 */
	private AuthPlayerDto validation(final AuthPlayerDto dto)
			throws InvalidNamePlayerException, InvalidPasswordPlayerException {
		if (!Validation.isValid(dto.getName(), new NameValidator())) {
			throw new InvalidNamePlayerException(dto.getName());
		}

		if (!Validation.isValid(dto.getPassword(), new PasswordValidator())) {
			throw new InvalidPasswordPlayerException(dto.getPassword());
		}

		return dto;
	}

	/**
	 * Преобразовывает сущность токена доступа для транспортировки.
	 *
	 * @param accessToken сущность токена доступа.
	 * @return токен доступа для транспортировки.
	 */
	private AccessTokenDto mapping(final AccessToken accessToken) {
		AccessTokenMapper accessTokenDtoMapper = Mappers.getMapper(AccessTokenMapper.class);
		return accessTokenDtoMapper.toDto(accessToken);
	}
}
