/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.wallet.dto.player.AuthPlayerDto;
import dev.kalenchukov.wallet.dto.player.CreatePlayerDto;
import dev.kalenchukov.wallet.dto.player.PlayerDto;
import dev.kalenchukov.wallet.dto.token.AccessTokenDto;
import dev.kalenchukov.wallet.entity.AccessToken;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.entity.mappers.AccessTokenMapper;
import dev.kalenchukov.wallet.entity.mappers.PlayerMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.InvalidNamePlayerException;
import dev.kalenchukov.wallet.exceptions.InvalidPasswordPlayerException;
import dev.kalenchukov.wallet.in.controller.validation.Validation;
import dev.kalenchukov.wallet.in.controller.validation.validators.NameValidator;
import dev.kalenchukov.wallet.in.controller.validation.validators.PasswordValidator;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.modules.AuthToken;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.codec.digest.DigestUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Класс обработки HTTP-запросов по игрокам.
 */
@RestController
@RequestMapping(
		path = "/players",
		produces = MediaType.APPLICATION_JSON_VALUE
)
@Api(tags = "Игроки")
public class PlayerController {
	/**
	 * Сервис игроков.
	 */
	private final PlayerService playerService;

	/**
	 * Конструирует контроллер.
	 *
	 * @param playerService сервис игроков.
	 */
	public PlayerController(final PlayerService playerService) {
		this.playerService = playerService;
	}

	/**
	 * Выполняет добавление игрока.
	 *
	 * @param createPlayerDto данные создания игрока.
	 * @return игрока.
	 * @throws ApplicationException если произошла ошибка при выполнении данного запроса.
	 */
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Если запрос выполнен успешно"),
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны"),
			@ApiResponse(responseCode = "409", description = "Если имя игрока уже занято")
	})
	@io.swagger.v3.oas.annotations.Operation(
			summary = "Добавление игрока",
			description = "Позволяет добавить игрока"
	)
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PlayerDto> create(@RequestBody final CreatePlayerDto createPlayerDto)
			throws ApplicationException {
		if (!Validation.isValid(createPlayerDto.getName(), new NameValidator())) {
			throw new InvalidNamePlayerException(createPlayerDto.getName());
		}

		if (!Validation.isValid(createPlayerDto.getPassword(), new PasswordValidator())) {
			throw new InvalidPasswordPlayerException(createPlayerDto.getPassword());
		}

		Player player = this.playerService.add(createPlayerDto.getName(), createPlayerDto.getPassword());
		PlayerDto playerDto = Mappers.getMapper(PlayerMapper.class).toDto(player);

		return ResponseEntity.status(HttpStatus.CREATED).body(playerDto);
	}

	/**
	 * Выполняет авторизацию игрока.
	 *
	 * @param authPlayerDto данные авторизации игрока.
	 * @return токен доступа.
	 * @throws ApplicationException если произошла ошибка при выполнении данного запроса.
	 */
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Если запрос выполнен успешно"),
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны"),
			@ApiResponse(responseCode = "404", description = "Если игрок не существует")
	})
	@io.swagger.v3.oas.annotations.Operation(
			summary = "Авторизация игрока",
			description = "Позволяет выполнить авторизацию игрока"
	)
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(path = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccessTokenDto> auth(@RequestBody final AuthPlayerDto authPlayerDto)
			throws ApplicationException {
		if (!Validation.isValid(authPlayerDto.getName(), new NameValidator())) {
			throw new InvalidNamePlayerException(authPlayerDto.getName());
		}

		if (!Validation.isValid(authPlayerDto.getPassword(), new PasswordValidator())) {
			throw new InvalidPasswordPlayerException(authPlayerDto.getPassword());
		}

		Player player = this.playerService.find(
				authPlayerDto.getName(),
				DigestUtils.md5Hex(authPlayerDto.getPassword())
		);
		AccessToken accessToken = new AccessToken(AuthToken.createToken(player.getPlayerId()));
		AccessTokenDto accessTokenDto = Mappers.getMapper(AccessTokenMapper.class).toDto(accessToken);

		return ResponseEntity.status(HttpStatus.OK).body(accessTokenDto);
	}
}
