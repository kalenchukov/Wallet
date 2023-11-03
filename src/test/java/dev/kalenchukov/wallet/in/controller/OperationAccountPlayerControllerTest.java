/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NotFoundOperationException;
import dev.kalenchukov.wallet.in.controller.handlers.ControllerHandler;
import dev.kalenchukov.wallet.in.service.OperationService;
import dev.kalenchukov.wallet.in.service.impl.OperationServiceImpl;
import dev.kalenchukov.wallet.modules.AuthToken;
import dev.kalenchukov.wallet.type.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Класс проверки методов класса {@link OperationAccountPlayerController}.
 */
public class OperationAccountPlayerControllerTest {
	private MockMvc mockMvc;

	private OperationService operationService;

	@BeforeEach
	public void beforeEach() {
		this.operationService = mock(OperationServiceImpl.class);
		this.mockMvc = MockMvcBuilders.standaloneSetup(new OperationAccountPlayerController(operationService))
				.defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				.setControllerAdvice(ControllerHandler.class)
				.build();
	}

	/**
	 * Класс проверки метода {@link OperationAccountPlayerController#get(long, long, long, String)}.
	 */
	@Nested
	public class Get {
		/**
		 * Проверка метода {@link OperationAccountPlayerController#get(long, long, long, String)}.
		 */
		@Test
		public void testGet() throws Exception {
			long operationId = 1L;
			long playerId = 88L;
			long accountId = 14L;
			OperationType operationType = OperationType.DEBIT;
			BigDecimal amount = BigDecimal.ONE;
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(operationId);
			when(operation.getPlayerId()).thenReturn(playerId);
			when(operation.getAccountId()).thenReturn(accountId);
			when(operation.getOperationType()).thenReturn(operationType);
			when(operation.getAmount()).thenReturn(amount);
			when(operationService.findById(anyLong(), anyLong(), anyLong())).thenReturn(operation);

			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(playerId);

				mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations/{operationId}", playerId, accountId, operationId)
								.header("Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6"))
						.andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpectAll(
								jsonPath("$.operationId").value(operationId),
								jsonPath("$.playerId").value(playerId),
								jsonPath("$.accountId").value(accountId),
								jsonPath("$.operationType").value(operationType.name())
						);
			}
		}

		/**
		 * Проверка метода {@link OperationAccountPlayerController#get(long, long, long, String)}
		 * с отсутствующей операцией.
		 */
		@Test
		public void testGetWithNotFound() throws Exception {
			long operationId = 1L;
			long playerId = 88L;
			long accountId = 14L;
			when(operationService.findById(anyLong(), anyLong(), anyLong()))
					.thenThrow(new NotFoundOperationException(operationId));

			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(playerId);

				mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations/{operationId}", playerId, accountId, operationId)
								.header("Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6"))
						.andExpect(status().isNotFound())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpectAll(
								jsonPath("$.message").isNotEmpty()
						);
			}
		}

		/**
		 * Проверка метода {@link OperationAccountPlayerController#get(long, long, long, String)}
		 * с отсутствием доступа.
		 */
		@Test
		public void testGetWithNoAccess() throws Exception {
			long operationId = 1L;
			long playerId = 88L;
			long accountId = 14L;

			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(24L);

				mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations/{operationId}", playerId, accountId, operationId)
								.header("Authorization", "srd7b634545f45tf45tg497T7R65rsd7f6"))
						.andExpect(status().isForbidden())
						.andExpectAll(
								jsonPath("$.message").isNotEmpty()
						);
			}
		}

		/**
		 * Проверка метода {@link OperationAccountPlayerController#get(long, long, long, String)}
		 * с отсутствующим токеном доступа.
		 */
		@Test
		public void testGetWithNoAccessToken() throws Exception {
			long operationId = 1L;
			long playerId = 88L;
			long accountId = 14L;

			mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations/{operationId}", playerId, accountId, operationId))
					.andExpect(status().isBadRequest());
		}

		/**
		 * Проверка метода {@link OperationAccountPlayerController#get(long, long, long, String)}
		 * с пустым токеном доступа.
		 */
		@Test
		public void testGetWithEmptyAccessToken() throws Exception {
			long operationId = 1L;
			long playerId = 88L;
			long accountId = 14L;

			mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations/{operationId}", playerId, accountId, operationId)
							.header("Authorization", ""))
					.andExpect(status().isBadRequest())
					.andExpectAll(
							jsonPath("$.message").isNotEmpty()
					);
		}

		/**
		 * Проверка метода {@link OperationAccountPlayerController#get(long, long, long, String)}
		 * с некорректным токеном доступа.
		 */
		@Test
		public void testGetWithInvalidAccessToken() throws Exception {
			long operationId = 1L;
			long playerId = 88L;
			long accountId = 14L;

			mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations/{operationId}", playerId, accountId, operationId)
							.header("Authorization", "7s5fvb65df7bs7dft8"))
					.andExpect(status().isUnauthorized())
					.andExpectAll(
							jsonPath("$.message").isNotEmpty()
					);
		}
	}

	/**
	 * Класс проверки метода {@link OperationAccountPlayerController#find(long, long, String)}.
	 */
	@Nested
	public class Find {
		/**
		 * Проверка метода {@link OperationAccountPlayerController#find(long, long, String)}.
		 */
		@Test
		public void find() throws Exception {
			long operationId = 1L;
			long playerId = 88L;
			long accountId = 14L;
			OperationType operationType = OperationType.CREDIT;
			BigDecimal amount = BigDecimal.TEN;
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(operationId);
			when(operation.getPlayerId()).thenReturn(playerId);
			when(operation.getAccountId()).thenReturn(accountId);
			when(operation.getOperationType()).thenReturn(operationType);
			when(operation.getAmount()).thenReturn(amount);
			when(operationService.find(anyLong(), anyLong())).thenReturn(List.of(operation));

			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(playerId);

				mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations", playerId, accountId)
								.header("Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6"))
						.andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpectAll(
								jsonPath("$.length()").value(1),
								jsonPath("$[0].operationId").value(operationId),
								jsonPath("$[0].playerId").value(playerId),
								jsonPath("$[0].accountId").value(accountId),
								jsonPath("$[0].operationType").value(operationType.name())
						);
			}
		}

		/**
		 * Проверка метода {@link OperationAccountPlayerController#find(long, long, String)}
		 * с отсутствующими операциями.
		 */
		@Test
		public void findWithNotFound() throws Exception {
			long playerId = 88L;
			long accountId = 14L;
			when(operationService.find(anyLong(), anyLong())).thenReturn(Collections.emptyList());

			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(playerId);

				mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations", playerId, accountId)
								.header("Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6"))
						.andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpectAll(
								jsonPath("$.length()").value(0)
						);
			}
		}

		/**
		 * Проверка метода {@link OperationAccountPlayerController#find(long, long, String)}
		 * с отсутствием доступа.
		 */
		@Test
		public void findWithNoAccess() throws Exception {
			long playerId = 88L;
			long accountId = 14L;

			try (MockedStatic<AuthToken> mockedStatic = mockStatic(AuthToken.class)) {
				mockedStatic.when(() -> AuthToken.verifyToken(anyString())).thenReturn(24L);

				mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations", playerId, accountId)
								.header("Authorization", "srd7b634545f45tf45tg497T7R65rsd7f6"))
						.andExpect(status().isForbidden())
						.andExpectAll(
								jsonPath("$.message").isNotEmpty()
						);
			}
		}

		/**
		 * Проверка метода {@link OperationAccountPlayerController#find(long, long, String)}
		 * с отсутствующим токеном доступа.
		 */
		@Test
		public void findWithNoAccessToken() throws Exception {
			long playerId = 88L;
			long accountId = 14L;

			mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations", playerId, accountId))
					.andExpect(status().isBadRequest());
		}

		/**
		 * Проверка метода {@link OperationAccountPlayerController#find(long, long, String)}
		 * с пустым токеном доступа.
		 */
		@Test
		public void findWithEmptyAccessToken() throws Exception {
			long playerId = 88L;
			long accountId = 14L;

			mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations", playerId, accountId)
							.header("Authorization", ""))
					.andExpect(status().isBadRequest())
					.andExpectAll(
							jsonPath("$.message").isNotEmpty()
					);
		}

		/**
		 * Проверка метода {@link OperationAccountPlayerController#find(long, long, String)}
		 * с некорректным токеном доступа.
		 */
		@Test
		public void findWithInvalidAccessToken() throws Exception {
			long playerId = 88L;
			long accountId = 14L;

			mockMvc.perform(get("/players/{playerId}/accounts/{accountId}/operations", playerId, accountId)
							.header("Authorization", "7s5fvb65df7bs7dft8"))
					.andExpect(status().isUnauthorized())
					.andExpectAll(
							jsonPath("$.message").isNotEmpty()
					);
		}
	}
}