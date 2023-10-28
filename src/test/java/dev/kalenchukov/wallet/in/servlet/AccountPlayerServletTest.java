/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kalenchukov.wallet.dto.account.AccountDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.player.NeedAuthPlayerException;
import dev.kalenchukov.wallet.exceptions.token.InvalidAccessTokenException;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.servlet.type.MimeType;
import dev.kalenchukov.wallet.modules.AuthToken;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountPlayerServletTest {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	@Mock
	private static ActionService ACTION_SERVICE;
	@Mock
	private static AccountService ACCOUNT_SERVICE;
	@Mock
	private static ServletInputStream INPUT_STREAM;
	@Mock
	private static PrintWriter PRINT_WRITER;
	@Mock
	private static HttpServletRequest REQUEST;
	@Mock
	private static HttpServletResponse RESPONSE;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
	}

	@Nested
	public class DoPost {
		/**
		 * Проверка метода {@link AccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}.
		 */
		@Test
		public void doPost() throws IOException {
			String inputData = "{" +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Объект, который возвращает сервис
			Account account = mock(Account.class);
			when(account.getAccountId()).thenReturn(1L);
			when(account.getPlayerId()).thenReturn(1L);
			when(account.getAmount()).thenReturn(BigDecimal.ZERO);
			// Объект, который должен вернуть сервер
			AccountDto accountDto = mock(AccountDto.class);
			when(accountDto.getAccountId()).thenReturn(1L);
			when(accountDto.getPlayerId()).thenReturn(1L);
			when(accountDto.getAmount()).thenReturn(BigDecimal.ZERO);
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(ACCOUNT_SERVICE.add(anyLong())).thenReturn(account);

			AccountPlayerServlet servlet = new AccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(1L);

				servlet.doPost(REQUEST, RESPONSE);
			}

			verify(ACCOUNT_SERVICE, times(1)).add(anyLong());
			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_CREATED);
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(accountDto));
		}

		/**
		 * Проверка метода {@link AccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с лишним значением во входящих данных.
		 */
		@Test
		public void doPostWithUnnecessaryParamInputData() throws IOException {
			String inputData = "{" +
					"\"unnecessary\":\"unnecessary data\"," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Объект, который возвращает сервис
			Account account = mock(Account.class);
			when(account.getAccountId()).thenReturn(1L);
			when(account.getPlayerId()).thenReturn(1L);
			when(account.getAmount()).thenReturn(BigDecimal.ZERO);
			// Объект, который должен вернуть сервер
			AccountDto accountDto = mock(AccountDto.class);
			when(accountDto.getAccountId()).thenReturn(1L);
			when(accountDto.getPlayerId()).thenReturn(1L);
			when(accountDto.getAmount()).thenReturn(BigDecimal.ZERO);
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(ACCOUNT_SERVICE.add(anyLong())).thenReturn(account);

			AccountPlayerServlet servlet = new AccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(1L);

				servlet.doPost(REQUEST, RESPONSE);
			}

			verify(ACCOUNT_SERVICE, times(1)).add(anyLong());
			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_CREATED);
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(accountDto));
		}

		/**
		 * Проверка метода {@link AccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * при некорректных входящих данных.
		 */
		@Test
		public void doPostWithInvalidInputData() throws IOException {
			String inputData = "{";
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());

			AccountPlayerServlet servlet = new AccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		/**
		 * Проверка метода {@link AccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с {@code null} в качестве токена доступа.
		 */
		@Test
		public void doPostWithNullAccessToken() throws IOException {
			String inputData = "{" +
					"\"accessToken\":null" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new InvalidAccessTokenException(null);
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			AccountPlayerServlet servlet = new AccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link AccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с некорректным токеном доступа.
		 */
		@Test
		public void doPostWithInvalidAccessToken() throws IOException {
			String inputData = "{" +
					"\"accessToken\":\"\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new InvalidAccessTokenException("");
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			AccountPlayerServlet servlet = new AccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link AccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с истёкшим токеном доступа.
		 */
		@Test
		public void doPostWithNeedAuth() throws IOException {
			String inputData = "{" +
					"\"accessToken\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
					"eyJpc3MiOiJXYWxsZXRBdXRoIiwicGxheWVySWQiOjEyLCJleHAiOjE2OTgxMzU3NjB9." +
					"B461wQdaWWB0Lp7Fx4Gg8cu92r-uyQe4fLGHkQflFAQ\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new NeedAuthPlayerException();
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			AccountPlayerServlet servlet = new AccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}
	}
}