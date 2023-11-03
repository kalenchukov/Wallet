/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.in.controller.handlers.ControllerHandler;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.modules.AuthToken;
import dev.kalenchukov.wallet.type.ActionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Класс проверки методов класса {@link ActionPlayerController}.
 */
public class ActionPlayerControllerTest {
	private MockMvc mockMvc;

	private ActionService actionService;

	@BeforeEach
	public void beforeEach() {
		this.actionService = mock(ActionServiceImpl.class);
		this.mockMvc = MockMvcBuilders.standaloneSetup(new ActionPlayerController(actionService))
				.defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				.setControllerAdvice(ControllerHandler.class)
				.build();
	}

	/**
	 * Класс проверки метода {@link ActionPlayerController#find(long, String)}.
	 */
	@Nested
	public class Find {
		/**
		 * Проверка метода {@link ActionPlayerController#find(long, String)}.
		 */
		@Test
		public void find() throws Exception {
			long actionId = 1L;
			long playerId = 88L;
			ActionType actionType = ActionType.ACTIONS;
			ActionType.Status actionTypeStatus = ActionType.Status.SUCCESS;
			Action action = mock(Action.class);
			when(action.getActionId()).thenReturn(actionId);
			when(action.getPlayerId()).thenReturn(playerId);
			when(action.getActionType()).thenReturn(actionType);
			when(action.getActionTypeStatus()).thenReturn(actionTypeStatus);
			when(actionService.find(anyLong())).thenReturn(List.of(action));

			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(playerId);

				mockMvc.perform(get("/players/{playerId}/actions", playerId)
								.header("Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6"))
						.andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpectAll(
								jsonPath("$.length()").value(1),
								jsonPath("$[0].actionId").value(actionId),
								jsonPath("$[0].playerId").value(playerId),
								jsonPath("$[0].actionType").value(actionType.name()),
								jsonPath("$[0].actionTypeStatus").value(actionTypeStatus.name())
						);
			}
		}

		/**
		 * Проверка метода {@link ActionPlayerController#find(long, String)}
		 * с отсутствующим игроком.
		 */
		@Test
		public void findWithNotFound() throws Exception {
			long playerId = 88L;
			when(actionService.find(anyLong())).thenReturn(Collections.emptyList());

			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(playerId);

				mockMvc.perform(get("/players/{playerId}/actions", playerId)
								.header("Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6"))
						.andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpectAll(
								jsonPath("$.length()").value(0)
						);
			}
		}

		/**
		 * Проверка метода {@link ActionPlayerController#find(long, String)}
		 * с отсутствием доступа.
		 */
		@Test
		public void findWithNoAccess() throws Exception {
			long playerId = 88L;

			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(24L);

				mockMvc.perform(get("/players/{playerId}/actions", playerId)
								.header("Authorization", "srd7b634545f45tf45tg497T7R65rsd7f6"))
						.andExpect(status().isForbidden())
						.andExpectAll(
								jsonPath("$.message").isNotEmpty()
						);
			}
		}

		/**
		 * Проверка метода {@link ActionPlayerController#find(long, String)}
		 * с отсутствующим токеном доступа.
		 */
		@Test
		public void findWithNoAccessToken() throws Exception {
			long playerId = 88L;

			mockMvc.perform(get("/players/{playerId}/actions", playerId))
					.andExpect(status().isBadRequest());
		}

		/**
		 * Проверка метода {@link ActionPlayerController#find(long, String)}
		 * с пустым токеном доступа.
		 */
		@Test
		public void findWithEmptyAccessToken() throws Exception {
			long playerId = 88L;

			mockMvc.perform(get("/players/{playerId}/actions", playerId)
							.header("Authorization", ""))
					.andExpect(status().isBadRequest())
					.andExpectAll(
							jsonPath("$.message").isNotEmpty()
					);
		}

		/**
		 * Проверка метода {@link ActionPlayerController#find(long, String)}
		 * с некорректным токеном доступа.
		 */
		@Test
		public void findWithInvalidAccessToken() throws Exception {
			long playerId = 88L;

			mockMvc.perform(get("/players/{playerId}/actions", playerId)
							.header("Authorization", "7s5fvb65df7bs7dft8"))
					.andExpect(status().isUnauthorized())
					.andExpectAll(
							jsonPath("$.message").isNotEmpty()
					);
		}
	}
}