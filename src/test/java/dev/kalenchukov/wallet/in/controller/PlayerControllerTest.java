/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.wallet.auth.AuthToken;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.DuplicateNamePlayerException;
import dev.kalenchukov.wallet.exceptions.NotFoundPlayerException;
import dev.kalenchukov.wallet.in.controller.handlers.ControllerHandler;
import dev.kalenchukov.wallet.in.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private PlayerService playerService;

	@MockBean
	private AuthToken authToken;

	@BeforeEach
	public void beforeEach() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new PlayerController(this.playerService, this.authToken))
				.defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				.setControllerAdvice(ControllerHandler.class)
				.build();
	}

	@Nested
	public class Create {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void createValid() throws Exception {
			long playerId = 1L;
			String name = "qwe";
			String password = "qwe";
			Player player = mock(Player.class);
			when(player.getPlayerId()).thenReturn(playerId);
			when(player.getName()).thenReturn(name);
			when(player.getPassword()).thenReturn(password);
			String body = "{" + "\"name\":\"qwe\"," + "\"password\":\"qwe\"" + "}";
			when(playerService.add(anyString(), anyString())).thenReturn(player);

			mockMvc.perform(post("/players").content(body)
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8))
					.andExpect(status().isCreated())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpectAll(jsonPath("$.playerId").value(playerId), jsonPath("$.name").value(name));
		}

		@DisplayName("Проверка с дублирующимся именем.")
		@Test
		public void createWithDuplicateName() throws Exception {
			String body = "{" + "\"name\":\"qwe\"," + "\"password\":\"qwe\"" + "}";
			when(playerService.add(anyString(), anyString())).thenThrow(new DuplicateNamePlayerException("qwe"));

			mockMvc.perform(post("/players").content(body)
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8))
					.andExpect(status().isConflict())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с null в качестве имени.")
		@Test
		public void createWithNullName() throws Exception {
			String body = "{" + "\"name\":null," + "\"password\":\"qwe\"" + "}";

			mockMvc.perform(post("/players").content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpectAll(
					jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}

		@DisplayName("Проверка с null в качестве пароля.")
		@Test
		public void createWithNullPassword() throws Exception {
			String body = "{" + "\"name\":\"qwe\"," + "\"password\":null" + "}";

			mockMvc.perform(post("/players").content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpectAll(
					jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}

		@DisplayName("Проверка с пустым именем.")
		@Test
		public void createWithEmptyName() throws Exception {
			String body = "{" + "\"name\":\"\"," + "\"password\":\"qwe\"" + "}";

			mockMvc.perform(post("/players").content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpectAll(
					jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}

		@DisplayName("Проверка с пустым паролем.")
		@Test
		public void createWithEmptyPassword() throws Exception {
			String body = "{" + "\"name\":\"qwe\"," + "\"password\":\"\"" + "}";

			mockMvc.perform(post("/players").content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpectAll(
					jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}
	}

	@Nested
	public class Auth {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void authValid() throws Exception {
			long playerId = 1L;
			String name = "qwe";
			String password = "qwe";
			String accessToken = "eyJhbGciOCJ9.eyJpc3MiOiJXYWxsZXRBdXg2NDcyNH0.QamTCQSLW4nuXCQ";
			Player player = mock(Player.class);
			when(player.getPlayerId()).thenReturn(playerId);
			when(player.getName()).thenReturn(name);
			when(player.getPassword()).thenReturn(password);
			String body = "{" + "\"name\":\"qwe\"," + "\"password\":\"qwe\"" + "}";
			when(authToken.createToken(anyLong())).thenReturn(accessToken);
			when(playerService.find(anyString(), anyString())).thenReturn(player);

			mockMvc.perform(post("/players/auth").content(body)
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpectAll(jsonPath("$.accessToken").isNotEmpty());
		}

		@DisplayName("Проверка с отсутствующим игроком.")
		@Test
		public void authWithNotFound() throws Exception {
			String body = "{" + "\"name\":\"qwe\"," + "\"password\":\"qwe\"" + "}";
			when(playerService.find(anyString(), anyString())).thenThrow(new NotFoundPlayerException("qwe"));

			mockMvc.perform(post("/players/auth").content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpectAll(
					jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с null в качестве имени.")
		@Test
		public void authWithNullName() throws Exception {
			String body = "{" + "\"name\":null," + "\"password\":\"qwe\"" + "}";

			mockMvc.perform(post("/players/auth").content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpectAll(
					jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}

		@DisplayName("Проверка с null в качестве пароля.")
		@Test
		public void authWithNullPassword() throws Exception {
			String body = "{" + "\"name\":\"qwe\"," + "\"password\":null" + "}";

			mockMvc.perform(post("/players/auth").content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpectAll(
					jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}

		@DisplayName("Проверка с пустым именем.")
		@Test
		public void authWithEmptyName() throws Exception {
			String body = "{" + "\"name\":\"\"," + "\"password\":\"qwe\"" + "}";

			mockMvc.perform(post("/players/auth").content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpectAll(
					jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}

		@DisplayName("Проверка с пустым паролем.")
		@Test
		public void authWithEmptyPassword() throws Exception {
			String body = "{" + "\"name\":\"qwe\"," + "\"password\":\"\"" + "}";

			mockMvc.perform(post("/players/auth").content(body)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpectAll(
					jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}
	}
}