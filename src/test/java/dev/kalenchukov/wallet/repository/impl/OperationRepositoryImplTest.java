/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.WalletApplicationTest;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.type.OperationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = WalletApplicationTest.class)
public class OperationRepositoryImplTest {
	@Autowired
	private OperationRepository operationRepository;

	@Nested
	public class Save {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void saveValid() {
			Operation operation = mock(Operation.class);
			when(operation.getPlayerId()).thenReturn(1L);
			when(operation.getAccountId()).thenReturn(1L);
			when(operation.getOperationType()).thenReturn(OperationType.CREDIT);
			when(operation.getAmount()).thenReturn(BigDecimal.ONE);

			Operation actualOperation = operationRepository.save(operation);

			assertThat(actualOperation.getOperationId()).isPositive();
			assertThat(actualOperation.getAccountId()).isEqualTo(operation.getAccountId());
			assertThat(actualOperation.getOperationType()).isEqualTo(operation.getOperationType());
			assertThat(actualOperation.getAmount()).isEqualTo(operation.getAmount());
		}

		@DisplayName("Проверка с null в качестве операции.")
		@Test
		public void saveWithNull() {
			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				operationRepository.save(null);
			});
		}
	}

	@Nested
	public class FindById {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void findByIdValid() {
			long operationId = 2L;
			long accountId = 1L;
			long playerId = 1L;

			Optional<Operation> actualOperation = operationRepository.findById(playerId, accountId, operationId);

			assertThat(actualOperation).isPresent();
			assertThat(actualOperation.get().getOperationId()).isEqualTo(operationId);
			assertThat(actualOperation.get().getPlayerId()).isEqualTo(playerId);
			assertThat(actualOperation.get().getAccountId()).isEqualTo(accountId);
			assertThat(actualOperation.get().getOperationType()).isEqualTo(OperationType.CREDIT);
			assertThat(actualOperation.get().getAmount()).isEqualTo(new BigDecimal("5.0"));
		}

		@DisplayName("Проверка с отсутствующей операцией по идентификатору операции.")
		@Test
		public void findByIdNotFoundByOperationId() {
			long operationId = 24546456L;
			long accountId = 1L;
			long playerId = 1L;

			Optional<Operation> actualOperation = operationRepository.findById(playerId, accountId, operationId);

			assertThat(actualOperation).isEmpty();
		}

		@DisplayName("Проверка с отсутствующей операцией по игроку.")
		@Test
		public void findByIdNotFoundByPlayer() {
			long operationId = 2L;
			long accountId = 1L;
			long playerId = 34546572L;

			Optional<Operation> actualOperation = operationRepository.findById(playerId, accountId, operationId);

			assertThat(actualOperation).isEmpty();
		}
	}

	@Nested
	public class Find {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void findValid() {
			long playerId = 1L;
			long accountId = 2L;

			List<Operation> actualSet = operationRepository.find(playerId, accountId);

			assertThat(actualSet).hasSize(1);
		}

		@DisplayName("Проверка с отсутствующей операцией.")
		@Test
		public void findWithNotFound() {
			long playerId = 1L;
			long accountId = 465166L;

			List<Operation> actualSet = operationRepository.find(playerId, accountId);

			assertThat(actualSet).isEmpty();
		}
	}
}