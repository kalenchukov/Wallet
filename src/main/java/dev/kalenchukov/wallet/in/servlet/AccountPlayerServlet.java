/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.core.JacksonException;
import dev.kalenchukov.wallet.dto.account.AccountDto;
import dev.kalenchukov.wallet.dto.account.CreateAccountDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.mappers.AccountMapper;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.token.InvalidAccessTokenException;
import dev.kalenchukov.wallet.in.service.AccountService;
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

/**
 * Класс обработки HTTP-запросов по адресу "/players/accounts".
 */
public class AccountPlayerServlet extends AbstractServlet {
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
	public AccountPlayerServlet(final ActionService actionService, final AccountService accountService) {
		super(actionService);
		this.accountService = accountService;
	}

	/**
	 * Выполняет добавление счёта.
	 *
	 * @param request  запрос.
	 * @param response ответ.
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
		try (ServletInputStream bodyReader = request.getInputStream()) {
			CreateAccountDto createAccountDto = this.validation(
					this.objectMapper.readValue(bodyReader.readAllBytes(), CreateAccountDto.class)
			);

			long playerId = AuthToken.verifyToken(createAccountDto.getAccessToken());

			Account account = this.accountService.add(playerId);

			this.generateResponse(this.mapping(account), response, HttpServletResponse.SC_CREATED);
			this.fixAction(playerId, ActionType.CREATE_ACCOUNT, ActionType.Status.SUCCESS);
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
	private CreateAccountDto validation(final CreateAccountDto dto) throws InvalidAccessTokenException {
		if (!Validation.isValid(dto.getAccessToken(), new AccessTokenValidator())) {
			throw new InvalidAccessTokenException(dto.getAccessToken());
		}

		return dto;
	}

	/**
	 * Преобразовывает сущность счёта для транспортировки.
	 *
	 * @param account сущность счёта.
	 * @return счёт для транспортировки.
	 */
	private AccountDto mapping(final Account account) {
		AccountMapper accountDtoMapper = Mappers.getMapper(AccountMapper.class);
		return accountDtoMapper.toDto(account);
	}
}
