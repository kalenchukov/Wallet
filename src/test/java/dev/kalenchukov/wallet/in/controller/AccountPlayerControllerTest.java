/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.controller;

import dev.kalenchukov.wallet.auth.AuthToken;
import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.OutOfAmountAccountException;
import dev.kalenchukov.wallet.in.controller.handlers.ControllerHandler;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.type.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountPlayerController.class)
public class AccountPlayerControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	@MockBean
	private AuthToken authToken;

	@BeforeEach
	public void beforeEach() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new AccountPlayerController(this.accountService, this.authToken))
				.defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				.setControllerAdvice(ControllerHandler.class)
				.build();
	}

	@Nested
	public class Create {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void createValid() throws Exception {
			long accountId = 18L;
			long playerId = 96L;
			BigDecimal amount = BigDecimal.ZERO;
			Account account = mock(Account.class);
			when(account.getAccountId()).thenReturn(accountId);
			when(account.getPlayerId()).thenReturn(playerId);
			when(account.getAmount()).thenReturn(amount);
			when(accountService.add(anyLong())).thenReturn(account);
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts", playerId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8))
					.andExpect(status().isCreated())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpectAll(jsonPath("$.accountId").value(accountId), jsonPath("$.playerId").value(playerId),
							jsonPath("$.amount").value(amount)
					);
		}

		@DisplayName("Проверка с отсутствием доступа.")
		@Test
		public void createWithNoAccess() throws Exception {
			long playerId = 96L;
			String body = "{" + "\"amount\":11.59" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(24L);

			mockMvc.perform(post("/players/{playerId}/accounts", playerId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8))
					.andExpect(status().isForbidden())
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с некорректным токеном доступа.")
		@Test
		public void createWithInvalidAccessToken() throws Exception {
			long playerId = 96L;

			mockMvc.perform(post("/players/{playerId}/accounts", playerId).header("Authorization", "7s5fvb65df7bs7dft8")
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8))
					.andExpect(status().isForbidden())
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с пустым токеном доступа.")
		@Test
		public void createWithEmptyAccessToken() throws Exception {
			long playerId = 96L;

			mockMvc.perform(post("/players/{playerId}/accounts", playerId).header("Authorization", "")
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8))
					.andExpect(status().isForbidden())
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с отсутствующим токеном доступа.")
		@Test
		public void createWithNoAccessToken() throws Exception {
			long playerId = 96L;

			mockMvc.perform(post("/players/{playerId}/accounts", playerId).contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	public class Credit {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void creditValid() throws Exception {
			long operationId = 33L;
			long accountId = 65L;
			long playerId = 15L;
			BigDecimal amount = BigDecimal.ONE;
			OperationType operationType = OperationType.CREDIT;
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(operationId);
			when(operation.getPlayerId()).thenReturn(playerId);
			when(operation.getAccountId()).thenReturn(accountId);
			when(operation.getOperationType()).thenReturn(operationType);
			when(operation.getAmount()).thenReturn(amount);
			String body = "{" + "\"amount\":1" + "}";
			when(accountService.credit(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(operation);
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/credit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isOk())
					.andExpectAll(jsonPath("$.operationId").value(operationId), jsonPath("$.accountId").value(accountId),
							jsonPath("$.playerId").value(playerId),
							jsonPath("$.operationType").value(operationType.name()),
							jsonPath("$.amount").value(amount)
					);
		}

		@DisplayName("Проверка с null в качестве суммы.")
		@Test
		public void creditWithNullAmount() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":null" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/credit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isBadRequest())
					.andExpectAll(jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}

		@DisplayName("Проверка с некорректной суммой.")
		@Test
		public void creditWithInvalidAmount() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":\"W\"" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/credit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isBadRequest());
		}

		@DisplayName("Проверка с пустой суммой.")
		@Test
		public void creditWithEmptyAmount() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/credit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isBadRequest());
		}

		@DisplayName("Проверка с отрицательной суммой.")
		@Test
		public void creditWithNegativeAmount() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":-17.9" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/credit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isBadRequest())
					.andExpectAll(jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}

		@DisplayName("Проверка с отсутствием доступа.")
		@Test
		public void creditWithNoAccess() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":11.59" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(24L);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/credit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isForbidden())
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с некорректным токеном доступа.")
		@Test
		public void creditWithInvalidAccessToken() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":11.59" + "}";

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/credit", playerId, accountId).header(
									"Authorization", "7s5fvb65df7bs7dft8")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isForbidden())
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с пустым токеном доступа.")
		@Test
		public void creditWithEmptyAccessToken() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":11.59" + "}";

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/credit", playerId, accountId).header(
									"Authorization", "")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isForbidden())
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с отсутствующим токеном доступа.")
		@Test
		public void creditWithNoAccessToken() throws Exception {
			long accountId = 65L;
			long playerId = 15L;

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/credit", playerId, accountId).contentType(
					MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isBadRequest());
		}
	}

	@Nested
	public class Debit {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void debitValid() throws Exception {
			long operationId = 33L;
			long accountId = 65L;
			long playerId = 15L;
			BigDecimal amount = BigDecimal.TEN;
			OperationType operationType = OperationType.DEBIT;
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(operationId);
			when(operation.getPlayerId()).thenReturn(playerId);
			when(operation.getAccountId()).thenReturn(accountId);
			when(operation.getOperationType()).thenReturn(operationType);
			when(operation.getAmount()).thenReturn(amount);
			String body = "{" + "\"amount\":10" + "}";
			when(accountService.debit(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(operation);
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/debit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isOk())
					.andExpectAll(jsonPath("$.operationId").value(operationId), jsonPath("$.accountId").value(accountId),
							jsonPath("$.playerId").value(playerId),
							jsonPath("$.operationType").value(operationType.name()),
							jsonPath("$.amount").value(amount)
					);
		}

		@DisplayName("Проверка с суммой превышающей баланс счёта.")
		@Test
		public void debitWithOutOfAmount() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			BigDecimal amount = BigDecimal.TEN;
			String body = "{" + "\"amount\":10" + "}";
			when(accountService.debit(anyLong(), anyLong(), any(BigDecimal.class))).thenThrow(
					new OutOfAmountAccountException(amount));
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/debit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isBadRequest())
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с null в качестве суммы.")
		@Test
		public void debitWithNullAmount() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":null" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/debit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isBadRequest())
					.andExpectAll(jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}

		@DisplayName("Проверка с некорректной суммой.")
		@Test
		public void debitWithInvalidAmount() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":\"W\"" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/debit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isBadRequest());
		}

		@DisplayName("Проверка с пустой суммой.")
		@Test
		public void debitWithEmptyAmount() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/debit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isBadRequest());
		}

		@DisplayName("Проверка с отрицательной суммой.")
		@Test
		public void debitWithNegativeAmount() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":-190" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(playerId);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/debit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isBadRequest())
					.andExpectAll(jsonPath("$").isArray(), jsonPath("$[0].message").isNotEmpty());
		}

		@DisplayName("Проверка с отсутствием доступа.")
		@Test
		public void debitWithNoAccess() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":11.59" + "}";
			when(authToken.verifyToken(anyString())).thenReturn(24L);

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/debit", playerId, accountId).header(
									"Authorization", "srd7b6s65Vr65E6WX4W65RV7878n897T7R65rsd7f6")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isForbidden())
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с некорректным токеном доступа.")
		@Test
		public void debitWithInvalidAccessToken() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":11.59" + "}";

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/debit", playerId, accountId).header(
									"Authorization", "7s5fvb65df7bs7dft8")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isForbidden())
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с пустым токеном доступа.")
		@Test
		public void debitWithEmptyAccessToken() throws Exception {
			long accountId = 65L;
			long playerId = 15L;
			String body = "{" + "\"amount\":11.59" + "}";

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/debit", playerId, accountId).header(
									"Authorization", "")
							.content(body)
							.contentType(
									MediaType.APPLICATION_JSON)
							.characterEncoding(
									StandardCharsets.UTF_8))
					.andExpect(status().isForbidden())
					.andExpectAll(jsonPath("$.message").isNotEmpty());
		}

		@DisplayName("Проверка с отсутствующим токеном доступа.")
		@Test
		public void debitWithNoAccessToken() throws Exception {
			long accountId = 65L;
			long playerId = 15L;

			mockMvc.perform(post("/players/{playerId}/accounts/{accountId}/debit", playerId, accountId).contentType(
					MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)).andExpect(
					status().isBadRequest());
		}
	}
}