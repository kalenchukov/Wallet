/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.core.JacksonException;
import dev.kalenchukov.wallet.dto.player.CreatePlayerDto;
import dev.kalenchukov.wallet.dto.player.PlayerDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.entity.mappers.PlayerMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.player.InvalidNamePlayerException;
import dev.kalenchukov.wallet.exceptions.player.InvalidPasswordPlayerException;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.in.servlet.validation.Validation;
import dev.kalenchukov.wallet.in.servlet.validation.validators.NameValidator;
import dev.kalenchukov.wallet.in.servlet.validation.validators.PasswordValidator;
import dev.kalenchukov.wallet.type.ActionType;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;

import javax.sql.DataSource;

/**
 * Класс обработки HTTP-запросов по адресу "/players".
 */
public class PlayerServlet extends AbstractServlet {
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
	public PlayerServlet(final ActionService actionService, final PlayerService playerService) {
		super(actionService);
		this.playerService = playerService;
	}

	/**
	 * Выполняет добавление игрока.
	 *
	 * @param request  запрос.
	 * @param response ответ.
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
		try (ServletInputStream bodyReader = request.getInputStream()) {
			CreatePlayerDto createPlayerDto = this.validation(
					this.objectMapper.readValue(bodyReader.readAllBytes(), CreatePlayerDto.class)
			);

			Player player = this.playerService.add(createPlayerDto.getName(), createPlayerDto.getPassword());

			PlayerDto playerDto = Mappers.getMapper(PlayerMapper.class)
					.toDto(player);

			this.generateResponse(playerDto, response, HttpServletResponse.SC_CREATED);
			this.fixAction(player.getPlayerId(), ActionType.NEW, ActionType.Status.SUCCESS);
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
	private CreatePlayerDto validation(final CreatePlayerDto dto)
			throws InvalidNamePlayerException, InvalidPasswordPlayerException {
		if (!Validation.isValid(dto.getName(), new NameValidator())) {
			throw new InvalidNamePlayerException(dto.getName());
		}

		if (!Validation.isValid(dto.getPassword(), new PasswordValidator())) {
			throw new InvalidPasswordPlayerException(dto.getPassword());
		}

		return dto;
	}
}
