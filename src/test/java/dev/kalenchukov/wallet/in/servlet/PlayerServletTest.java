/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kalenchukov.wallet.dto.player.PlayerDto;
import dev.kalenchukov.wallet.dto.violation.ViolationDto;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.ApplicationException;
import dev.kalenchukov.wallet.exceptions.player.DuplicateNamePlayerException;
import dev.kalenchukov.wallet.exceptions.player.InvalidNamePlayerException;
import dev.kalenchukov.wallet.exceptions.player.InvalidPasswordPlayerException;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.in.servlet.type.MimeType;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServletTest {
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
		 * Проверка метода {@link PlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}.
		 */
		@Test
		public void doPost() throws IOException, DuplicateNamePlayerException {
			String inputData = "{" +
					"\"name\":\"qwe\"," +
					"\"password\":\"qwe\"" +
					"}";
			// Объект, который возвращает сервис
			Player player = mock(Player.class);
			when(player.getPlayerId()).thenReturn(1L);
			when(player.getName()).thenReturn("qwe");
			// Объект, который должен вернуть сервер
			PlayerDto playerDto = mock(PlayerDto.class);
			when(playerDto.getPlayerId()).thenReturn(1L);
			when(playerDto.getName()).thenReturn("qwe");
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(PLAYER_SERVICE.add(anyString(), anyString())).thenReturn(player);

			PlayerServlet servlet = new PlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(PLAYER_SERVICE, times(1)).add(anyString(), anyString());
			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_CREATED);
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(playerDto));
		}

		/**
		 * Проверка метода {@link PlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * с лишним значением во входящих данных.
		 */
		@Test
		public void doPostWithUnnecessaryParamInputData() throws IOException, DuplicateNamePlayerException {
			String inputData = "{" +
					"\"unnecessary\":\"unnecessary data\"," +
					"\"name\":\"qwe\"," +
					"\"password\":\"qwe\"" +
					"}";
			// Объект, который возвращает сервис
			Player player = mock(Player.class);
			when(player.getPlayerId()).thenReturn(1L);
			when(player.getName()).thenReturn("qwe");
			// Объект, который должен вернуть сервер
			PlayerDto playerDto = mock(PlayerDto.class);
			when(playerDto.getPlayerId()).thenReturn(1L);
			when(playerDto.getName()).thenReturn("qwe");
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(PLAYER_SERVICE.add(anyString(), anyString())).thenReturn(player);

			PlayerServlet servlet = new PlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(PLAYER_SERVICE, times(1)).add(anyString(), anyString());
			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_CREATED);
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(playerDto));
		}

		/**
		 * Проверка метода {@link PlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * при некорректных входящих данных.
		 */
		@Test
		public void doPostWithInvalidInputData() throws IOException {
			String inputData = "{";
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());

			PlayerServlet servlet = new PlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		/**
		 * Проверка метода {@link PlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
		 * при дублировании имени игрока.
		 */
		@Test
		public void doPostWithDuplicatePlayerName() throws IOException, DuplicateNamePlayerException {
			String inputData = "{" +
					"\"name\":\"qwe\"," +
					"\"password\":\"qwe\"" +
					"}";
			// Аналог исключения, которое будет вызвано проверкой входящих данных
			ApplicationException exception = new DuplicateNamePlayerException("qwe");
			// Объект, который должен вернуть сервер
			ViolationDto violationDto = mock(ViolationDto.class);
			when(violationDto.getMessage()).thenReturn(exception.getMessage());
			when(REQUEST.getInputStream()).thenReturn(INPUT_STREAM);
			when(INPUT_STREAM.readAllBytes()).thenReturn(inputData.getBytes());
			when(RESPONSE.getWriter()).thenReturn(PRINT_WRITER);
			when(PLAYER_SERVICE.add(anyString(), anyString()))
					.thenThrow(new DuplicateNamePlayerException("qwe"));

			PlayerServlet servlet = new PlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link PlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
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

			PlayerServlet servlet = new PlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link PlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
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

			PlayerServlet servlet = new PlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link PlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
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

			PlayerServlet servlet = new PlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}

		/**
		 * Проверка метода {@link PlayerServlet#doPost(HttpServletRequest, HttpServletResponse)}
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

			PlayerServlet servlet = new PlayerServlet(ACTION_SERVICE, PLAYER_SERVICE);
			servlet.doPost(REQUEST, RESPONSE);

			verify(RESPONSE, times(1)).setContentType(MimeType.JSON.getValue());
			verify(RESPONSE, times(1)).setStatus(exception.getHttpCode());
			verify(RESPONSE, times(1)).setCharacterEncoding("UTF-8");
			verify(RESPONSE, times(1)).setContentLength(anyInt());
			verify(PRINT_WRITER, times(1)).write(OBJECT_MAPPER.writeValueAsString(violationDto));
		}
	}
}