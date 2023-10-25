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
import dev.kalenchukov.wallet.exceptions.account.NoAccessAccountException;
import dev.kalenchukov.wallet.exceptions.account.NotFoundAccountException;
import dev.kalenchukov.wallet.exceptions.account.OutOfAmountAccountException;
import dev.kalenchukov.wallet.exceptions.operation.InvalidAmountOperationException;
import dev.kalenchukov.wallet.exceptions.operation.NegativeAmountOperationException;
import dev.kalenchukov.wallet.exceptions.player.NeedAuthPlayerException;
import dev.kalenchukov.wallet.exceptions.token.InvalidAccessTokenException;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.in.service.ActionService;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DebitAccountPlayerServletTest {
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
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}.
		 */
		@Test
		public void doPost() throws IOException, NotFoundAccountException, NoAccessAccountException, OutOfAmountAccountException, NegativeAmountOperationException {
			String inputData = "{" +
					"\"accountId\":1," +
					"\"amount\":99.9," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Объект, который возвращает сервис
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(1L);
			when(operation.getAccountId()).thenReturn(1L);
			when(operation.getOperationType()).thenReturn(OperationType.DEBIT);
			when(operation.getAmount()).thenReturn(new BigDecimal("99.9"));
			// Объект, который должен вернуть сервер
			OperationDto operationDto = mock(OperationDto.class);
			when(operationDto.getOperationId()).thenReturn(1L);
			when(operationDto.getAccountId()).thenReturn(1L);
			when(operationDto.getOperationType()).thenReturn(OperationType.DEBIT);
			when(operationDto.getAmount()).thenReturn(new BigDecimal("99.9"));
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(ACCOUNT_SERVICE.debit(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(operation);

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(1L);

				servlet.doPost(REQUEST, RESPONSE);
			}

			verify(ACCOUNT_SERVICE, times(1)).debit(anyLong(), anyLong(), any(BigDecimal.class));
			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_OK);
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(operationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с лишним значением во входящих данных.
		 */
		@Test
		public void doPostWithUnnecessaryParamInputData() throws IOException, NotFoundAccountException, NoAccessAccountException, OutOfAmountAccountException, NegativeAmountOperationException {
			String inputData = "{" +
					"\"unnecessary\":\"unnecessary data\"," +
					"\"accountId\":1," +
					"\"amount\":99.9," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Объект, который возвращает сервис
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(1L);
			when(operation.getAccountId()).thenReturn(1L);
			when(operation.getOperationType()).thenReturn(OperationType.DEBIT);
			when(operation.getAmount()).thenReturn(new BigDecimal("99.9"));
			// Объект, который должен вернуть сервер
			OperationDto operationDto = mock(OperationDto.class);
			when(operationDto.getOperationId()).thenReturn(1L);
			when(operationDto.getAccountId()).thenReturn(1L);
			when(operationDto.getOperationType()).thenReturn(OperationType.DEBIT);
			when(operationDto.getAmount()).thenReturn(new BigDecimal("99.9"));
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(ACCOUNT_SERVICE.debit(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(operation);

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(1L);

				servlet.doPost(REQUEST, RESPONSE);
			}

			verify(ACCOUNT_SERVICE, times(1)).debit(anyLong(), anyLong(), any(BigDecimal.class));
			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_OK);
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(operationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * при некорректных входящих данных.
		 */
		@Test
		public void doPostWithInvalidInputData() throws IOException {
			String inputData = "{";
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с отсутствием аккаунта.
		 */
		@Test
		public void doPostWithNotFoundAccount() throws IOException, NotFoundAccountException, NoAccessAccountException, OutOfAmountAccountException, NegativeAmountOperationException {
			String inputData = "{" +
					"\"accountId\":164689," +
					"\"amount\":99.9," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new NotFoundAccountException(164689L);
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(ACCOUNT_SERVICE.debit(anyLong(), anyLong(), any(BigDecimal.class)))
					.thenThrow(new NotFoundAccountException(164689L));

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(1L);

				servlet.doPost(REQUEST, RESPONSE);
			}

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с отсутствием доступа аккаунту.
		 */
		@Test
		public void doPostWithNoAccessAccount() throws IOException, NotFoundAccountException, NoAccessAccountException, OutOfAmountAccountException, NegativeAmountOperationException {
			String inputData = "{" +
					"\"accountId\":164689," +
					"\"amount\":99.9," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new NoAccessAccountException(164689L);
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(ACCOUNT_SERVICE.debit(anyLong(), anyLong(), any(BigDecimal.class)))
					.thenThrow(new NoAccessAccountException(164689L));

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(1L);

				servlet.doPost(REQUEST, RESPONSE);
			}

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с отрицательной суммой операции.
		 */
		@Test
		public void doPostWithNegativeAmount() throws IOException, NotFoundAccountException, NoAccessAccountException, OutOfAmountAccountException, NegativeAmountOperationException {
			String inputData = "{" +
					"\"accountId\":1," +
					"\"amount\":-88.8," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new NegativeAmountOperationException(BigDecimal.valueOf(-88.8));
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(ACCOUNT_SERVICE.debit(anyLong(), anyLong(), any(BigDecimal.class)))
					.thenThrow(new NegativeAmountOperationException(BigDecimal.valueOf(-88.8)));

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(1L);

				servlet.doPost(REQUEST, RESPONSE);
			}

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с превышением суммы счёта.
		 */
		@Test
		public void doPostWithOutOfAmountAccount() throws IOException, NotFoundAccountException, NoAccessAccountException, OutOfAmountAccountException, NegativeAmountOperationException {
			String inputData = "{" +
					"\"accountId\":1," +
					"\"amount\":1000000," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new OutOfAmountAccountException(BigDecimal.valueOf(1000000));
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(ACCOUNT_SERVICE.debit(anyLong(), anyLong(), any(BigDecimal.class)))
					.thenThrow(new OutOfAmountAccountException(BigDecimal.valueOf(1000000)));

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(1L);

				servlet.doPost(REQUEST, RESPONSE);
			}

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с {@code null} в качестве токена доступа.
		 */
		@Test
		public void doPostWithNullAccessToken() throws IOException {
			String inputData = "{" +
					"\"accountId\":1," +
					"\"amount\":99.9," +
					"\"accessToken\":null}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new InvalidAccessTokenException(null);
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с некорректным токеном доступа.
		 */
		@Test
		public void doPostWithInvalidAccessToken() throws IOException {
			String inputData = "{" +
					"\"accountId\":1," +
					"\"amount\":99.9," +
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

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с истёкшим токеном доступа.
		 */
		@Test
		public void doPostWithNeedAuth() throws IOException {
			String inputData = "{" +
					"\"accountId\":1," +
					"\"amount\":99.9," +
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

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с {@code null} в качестве идентификатора аккаунта.
		 */
		@Test
		public void doPostWithNullAccountId() throws IOException {
			String inputData = "{" +
					"\"accountId\":null," +
					"\"amount\":99.9," +
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

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с некорректным идентификатором аккаунта.
		 */
		@Test
		public void doPostWithInvalidAccountId() throws IOException {
			String inputData = "{" +
					"\"accountId\":\"W\"," +
					"\"amount\":99.9," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с {@code null} в качестве суммы.
		 */
		@Test
		public void doPostWithNullAmount() throws IOException {
			String inputData = "{" +
					"\"accountId\":1," +
					"\"amount\":null," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new InvalidAmountOperationException(null);
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link DebitAccountPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с некорректной суммой.
		 */
		@Test
		public void doPostWithInvalidAmount() throws IOException {
			String inputData = "{" +
					"\"accountId\":1," +
					"\"amount\":\"W\"," +
					"\"accessToken\":\"eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ\"" +
					"}";
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());

			DebitAccountPlayerServlet servlet = new DebitAccountPlayerServlet(ACTION_SERVICE, ACCOUNT_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}