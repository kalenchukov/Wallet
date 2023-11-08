/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.wallet.auth.AuthToken;
import dev.kalenchukov.wallet.dto.*;
import dev.kalenchukov.wallet.entity.AccessToken;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.entity.mappers.AccessTokenMapper;
import dev.kalenchukov.wallet.entity.mappers.PlayerMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.in.service.PlayerService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Класс обработки HTTP-запросов по игрокам.
 */
@RestController
@RequestMapping(path = "/players", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Игроки")
public class PlayerController {
	/**
	 * Сервис игроков.
	 */
	private final PlayerService playerService;

	/**
	 * Авторизационные токены.
	 */
	private final AuthToken authToken;

	/**
	 * Конструирует контроллер.
	 *
	 * @param playerService сервис игроков.
	 * @param authToken     авторизационные токены.
	 */
	@Autowired
	public PlayerController(final PlayerService playerService, final AuthToken authToken) {
		this.playerService = playerService;
		this.authToken = authToken;
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
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))}),
			@ApiResponse(responseCode = "409", description = "Если имя игрока уже занято", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))})
	})
	@io.swagger.v3.oas.annotations.Operation(summary = "Добавление игрока", description = "Позволяет добавить игрока")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PlayerDto> create(@Valid @RequestBody final CreatePlayerDto createPlayerDto)
			throws ApplicationException {
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
			@ApiResponse(responseCode = "400", description = "Если запрос или данные некорректны", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))}),
			@ApiResponse(responseCode = "404", description = "Если игрок не существует", content = {
					@Content(schema = @Schema(implementation = ViolationDto.class))})
	})
	@io.swagger.v3.oas.annotations.Operation(summary = "Авторизация игрока", description = "Позволяет выполнить авторизацию игрока")
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(path = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccessTokenDto> auth(@Valid @RequestBody final AuthPlayerDto authPlayerDto)
			throws ApplicationException {
		Player player = this.playerService.find(authPlayerDto.getName(),
				DigestUtils.md5Hex(authPlayerDto.getPassword())
		);
		AccessToken accessToken = new AccessToken(this.authToken.createToken(player.getPlayerId()));
		AccessTokenDto accessTokenDto = Mappers.getMapper(AccessTokenMapper.class).toDto(accessToken);

		return ResponseEntity.status(HttpStatus.OK).body(accessTokenDto);
	}
}
