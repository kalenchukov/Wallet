/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kalenchukov.wallet.dto.operation.OperationDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.account.InvalidIdAccountException;
import dev.kalenchukov.wallet.exceptions.player.NeedAuthPlayerException;
import dev.kalenchukov.wallet.exceptions.token.InvalidAccessTokenException;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.OperationService;
import dev.kalenchukov.wallet.in.servlet.type.MimeType;
import dev.kalenchukov.wallet.modules.AuthToken;
import dev.kalenchukov.wallet.type.OperationType;
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
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListOperationAccountPlayerServletTest {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	@Mock
	private static ActionService ACTION_SERVICE;
	@Mock
	private static OperationService OPERATION_SERVICE;
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
	public class DoGet {
		/**
		 * Проверка метода {@link ListOperationAccountPlayerServlet#doGet(HttpServletRequest, HttpServletResponse)}.
		 */
		@Test
		public void doGet() throws IOException {
			String inputData = "{" +
					"\"accountId\":1," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Объект, который возвращает сервис
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(1L);
			when(operation.getAccountId()).thenReturn(1L);
			when(operation.getOperationType()).thenReturn(OperationType.CREDIT);
			when(operation.getAmount()).thenReturn(BigDecimal.TEN);
			// Объект, который должен вернуть сервер
			OperationDto operationDto = mock(OperationDto.class);
			when(operationDto.getOperationId()).thenReturn(1L);
			when(operationDto.getAccountId()).thenReturn(1L);
			when(operationDto.getOperationType()).thenReturn(OperationType.CREDIT);
			when(operationDto.getAmount()).thenReturn(BigDecimal.TEN);
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(OPERATION_SERVICE.find(anyLong(), anyLong())).thenReturn(List.of(operation));

			ListOperationAccountPlayerServlet servlet = new ListOperationAccountPlayerServlet(ACTION_SERVICE, OPERATION_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(1L);

				servlet.doGet(REQUEST, RESPONSE);
			}

			verify(OPERATION_SERVICE, times(1)).find(anyLong(), anyLong());
			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_OK);
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(List.of(operationDto)));
		}

		/**
		 * Проверка метода {@link ListOperationAccountPlayerServlet#doGet(HttpServletRequest, HttpServletResponse)}
		 * с лишним значением во входящих данных.
		 */
		@Test
		public void doGetWithUnnecessaryParamInputData() throws IOException {
			String inputData = "{" +
					"\"unnecessary\":\"unnecessary data\"," +
					"\"accountId\":1," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Объект, который возвращает сервис
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(1L);
			when(operation.getAccountId()).thenReturn(1L);
			when(operation.getOperationType()).thenReturn(OperationType.CREDIT);
			when(operation.getAmount()).thenReturn(BigDecimal.TEN);
			// Объект, который должен вернуть сервер
			OperationDto operationDto = mock(OperationDto.class);
			when(operationDto.getOperationId()).thenReturn(1L);
			when(operationDto.getAccountId()).thenReturn(1L);
			when(operationDto.getOperationType()).thenReturn(OperationType.CREDIT);
			when(operationDto.getAmount()).thenReturn(BigDecimal.TEN);
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(OPERATION_SERVICE.find(anyLong(), anyLong())).thenReturn(List.of(operation));

			ListOperationAccountPlayerServlet servlet = new ListOperationAccountPlayerServlet(ACTION_SERVICE, OPERATION_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(1L);

				servlet.doGet(REQUEST, RESPONSE);
			}

			verify(OPERATION_SERVICE, times(1)).find(anyLong(), anyLong());
			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_OK);
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(List.of(operationDto)));
		}

		/**
		 * Проверка метода {@link ListOperationAccountPlayerServlet#doGet(HttpServletRequest, HttpServletResponse)}
		 * при некорректных входящих данных.
		 */
		@Test
		public void doGetWithInvalidInputData() throws IOException {
			String inputData = "{";
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());

			ListOperationAccountPlayerServlet servlet = new ListOperationAccountPlayerServlet(ACTION_SERVICE, OPERATION_SERVICE);
			servlet.doGet(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		/**
		 * Проверка метода {@link ListOperationAccountPlayerServlet#doGet(HttpServletRequest, HttpServletResponse)}
		 * с {@code null} в качестве токена доступа.
		 */
		@Test
		public void doPostWithNullAccessToken() throws IOException {
			String inputData = "{" +
					"\"accountId\":1," +
					"\"accessToken\":null}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new InvalidAccessTokenException(null);
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			ListOperationAccountPlayerServlet servlet = new ListOperationAccountPlayerServlet(ACTION_SERVICE, OPERATION_SERVICE);
			servlet.doGet(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link ListOperationAccountPlayerServlet#doGet(HttpServletRequest, HttpServletResponse)}
		 * с некорректным токеном доступа.
		 */
		@Test
		public void doPostWithInvalidAccessToken() throws IOException {
			String inputData = "{" +
					"\"accountId\":1," +
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

			ListOperationAccountPlayerServlet servlet = new ListOperationAccountPlayerServlet(ACTION_SERVICE, OPERATION_SERVICE);
			servlet.doGet(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link ListOperationAccountPlayerServlet#doGet(HttpServletRequest, HttpServletResponse)}
		 * с истёкшим токеном доступа.
		 */
		@Test
		public void doPostWithNeedAuth() throws IOException {
			String inputData = "{" +
					"\"accountId\":1," +
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

			ListOperationAccountPlayerServlet servlet = new ListOperationAccountPlayerServlet(ACTION_SERVICE, OPERATION_SERVICE);
			servlet.doGet(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link ListOperationAccountPlayerServlet#doGet(HttpServletRequest, HttpServletResponse)}
		 * с {@code null} в качестве идентификатора аккаунта.
		 */
		@Test
		public void doPostWithNullAccountId() throws IOException {
			String inputData = "{" +
					"\"accountId\":null," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new InvalidIdAccountException(null);
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			ListOperationAccountPlayerServlet servlet = new ListOperationAccountPlayerServlet(ACTION_SERVICE, OPERATION_SERVICE);
			servlet.doGet(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link ListOperationAccountPlayerServlet#doGet(HttpServletRequest, HttpServletResponse)}
		 * с некорректным идентификатором аккаунта.
		 */
		@Test
		public void doPostWithInvalidAccountId() throws IOException {
			String inputData = "{" +
					"\"accountId\":\"W\"," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());

			ListOperationAccountPlayerServlet servlet = new ListOperationAccountPlayerServlet(ACTION_SERVICE, OPERATION_SERVICE);
			servlet.doGet(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}