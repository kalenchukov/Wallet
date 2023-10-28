/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kalenchukov.wallet.dto.token.AccessTokenDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.player.InvalidNamePlayerException;
import dev.kalenchukov.wallet.exceptions.player.InvalidPasswordPlayerException;
import dev.kalenchukov.wallet.exceptions.player.NotFoundPlayerException;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.PlayerService;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServletTest {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	@Mock
	private static ActionService ACTION_SERVICE;
	@Mock
	private static PlayerService PLAYER_SERVICE;
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
		 * Проверка метода {@link AuthPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}.
		 */
		@Test
		public void doPost() throws IOException, NotFoundPlayerException {
			String inputData = "{" +
					"\"name\":\"qwe\"," +
					"\"password\":\"qwe\"" +
					"}";
			String accessToken = "eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ";
			// Объект, который возвращает сервис
			Player player = mock(Player.class);
			when(player.getPlayerId()).thenReturn(1L);
			// Объект, который должен вернуть сервер
			AccessTokenDto accessTokenDto = mock(AccessTokenDto.class);
			when(accessTokenDto.getAccessToken()).thenReturn(accessToken);
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(PLAYER_SERVICE.find(anyString(), anyString())).thenReturn(player);

			AuthPlayerServlet servlet = new AuthPlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.createToken(anyLong())).thenReturn(accessToken);

				servlet.doPost(REQUEST, RESPONSE);
			}

			verify(PLAYER_SERVICE, times(1)).find(anyString(), anyString());
			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_OK);
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(accessTokenDto));
		}

		/**
		 * Проверка метода {@link AuthPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с лишним значением во входящих данных.
		 */
		@Test
		public void doPostWithUnnecessaryParamInputData() throws IOException, NotFoundPlayerException {
			String inputData = "{" +
					"\"unnecessary\":\"unnecessary data\"," +
					"\"name\":\"qwe\"," +
					"\"password\":\"qwe\"" +
					"}";
			String accessToken = "eyJhbGciOiJI6IkpX9.eyJpc3MiOixMzU3NjB9.B461wQdfLGHkQflFAQ";
			// Объект, который возвращает сервис
			Player player = mock(Player.class);
			when(player.getPlayerId()).thenReturn(1L);
			// Объект, который должен вернуть сервер
			AccessTokenDto accessTokenDto = mock(AccessTokenDto.class);
			when(accessTokenDto.getAccessToken()).thenReturn(accessToken);
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(PLAYER_SERVICE.find(anyString(), anyString())).thenReturn(player);

			AuthPlayerServlet servlet = new AuthPlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.createToken(anyLong())).thenReturn(accessToken);

				servlet.doPost(REQUEST, RESPONSE);
			}

			verify(PLAYER_SERVICE, times(1)).find(anyString(), anyString());
			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_OK);
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(accessTokenDto));
		}

		/**
		 * Проверка метода {@link AuthPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * при некорректных входящих данных.
		 */
		@Test
		public void doPostWithInvalidInputData() throws IOException {
			String inputData = "{";
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());

			AuthPlayerServlet servlet = new AuthPlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		/**
		 * Проверка метода {@link AuthPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с {@code null} в качестве имени игрока.
		 */
		@Test
		public void doPostWithNullPlayerName() throws IOException {
			String inputData = "{" +
					"\"name\":null," +
					"\"password\":\"qwe\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new InvalidNamePlayerException(null);
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			AuthPlayerServlet servlet = new AuthPlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link AuthPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с {@code null} в качестве пароля игрока.
		 */
		@Test
		public void doPostWithNullPlayerPassword() throws IOException {
			String inputData = "{" +
					"\"name\":\"qwe\"," +
					"\"password\":null}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new InvalidPasswordPlayerException(null);
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			AuthPlayerServlet servlet = new AuthPlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link AuthPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с некорректным именем игрока.
		 */
		@Test
		public void doPostWithInvalidPlayerName() throws IOException {
			String inputData = "{" +
					"\"name\":\"23423\"," +
					"\"password\":\"qwe\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new InvalidNamePlayerException("23423");
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			AuthPlayerServlet servlet = new AuthPlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link AuthPlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с некорректным паролем игрока.
		 */
		@Test
		public void doPostWithInvalidPlayerPassword() throws IOException {
			String inputData = "{" +
					"\"name\":\"qwe\"," +
					"\"password\":\"\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new InvalidPasswordPlayerException("");
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);

			AuthPlayerServlet servlet = new AuthPlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}
	}
}