/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NotFoundOperationException;
import dev.kalenchukov.wallet.in.service.OperationService;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.repository.impl.OperationRepositoryImpl;
import dev.kalenchukov.wallet.type.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class OperationServiceImplTest {
	private OperationRepository operationRepository;

	@BeforeEach
	public void beforeEach() {
		this.operationRepository = mock(OperationRepositoryImpl.class);
	}

	@Nested
	public class Add {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void addValid() {
			long playerId = 11L;
			long accountId = 13L;
			Operation operation = mock(Operation.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			when(operationRepository.save(any(Operation.class))).thenReturn(operation);
			OperationService operationService = new OperationServiceImpl(operationRepository);

			Operation actualOperation = operationService.add(playerId, accountId, operationType, amount);

			assertThat(actualOperation).isEqualTo(operation);
			verify(operationRepository, only()).save(any(Operation.class));
		}

		@DisplayName("Проверка с null в качестве типа операции.")
		@Test
		public void addWithNullOperationType() {
			long playerId = 11L;
			long accountId = 13L;
			BigDecimal amount = mock(BigDecimal.class);
			OperationService operationService = new OperationServiceImpl(operationRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				operationService.add(playerId, accountId, null, amount);
			});
		}

		@DisplayName("Проверка с null в качестве суммы.")
		@Test
		public void addWithNullAmount() {
			long playerId = 11L;
			long accountId = 13L;
			OperationType operationType = mock(OperationType.class);
			OperationService operationService = new OperationServiceImpl(operationRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				operationService.add(playerId, accountId, operationType, null);
			});
		}
	}

	@Nested
	public class FindById {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void findByIdValid() throws NotFoundOperationException {
			long operationId = 42L;
			long accountId = 13L;
			long playerId = 435L;
			Operation operation = mock(Operation.class);
			when(operationRepository.findById(playerId, accountId, operationId)).thenReturn(Optional.of(operation));
			OperationService operationService = new OperationServiceImpl(operationRepository);

			Operation actual = operationService.findById(playerId, accountId, operationId);

			verify(operationRepository, only()).findById(playerId, accountId, operationId);
			assertThat(actual).isEqualTo(operation);
		}

		@DisplayName("Проверка с отсутствующими операциями.")
		@Test
		public void findByIdWithNotFound() {
			long operationId = 42L;
			long accountId = 13L;
			long playerId = 435L;
			when(operationRepository.findById(playerId, accountId, operationId)).thenReturn(Optional.empty());
			OperationService operationService = new OperationServiceImpl(operationRepository);

			assertThatExceptionOfType(NotFoundOperationException.class).isThrownBy(() -> {
				operationService.findById(playerId, accountId, operationId);
			});

			verify(operationRepository, only()).findById(playerId, accountId, operationId);
		}
	}

	@Nested
	public class Find {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void findValid() {
			long accountId = 13L;
			long playerId = 435L;
			Operation operation = mock(Operation.class);
			when(operation.getAccountId()).thenReturn(accountId);
			when(operationRepository.find(playerId, accountId)).thenReturn(List.of(operation));
			OperationService operationService = new OperationServiceImpl(operationRepository);

			List<Operation> actual = operationService.find(playerId, accountId);
			List<Operation> expected = List.of(operation);

			verify(operationRepository, only()).find(playerId, accountId);
			assertThat(actual).containsExactlyElementsOf(expected);
		}

		@DisplayName("Проверка с отсутствующими операциями.")
		@Test
		public void findWithNotFound() {
			long accountId = 13L;
			long playerId = 435L;
			Operation operation = mock(Operation.class);
			when(operation.getAccountId()).thenReturn(accountId);
			when(operationRepository.find(playerId, accountId)).thenReturn(Collections.emptyList());
			OperationService operationService = new OperationServiceImpl(operationRepository);

			List<Operation> actual = operationService.find(playerId, accountId);

			verify(operationRepository, only()).find(playerId, accountId);
			assertThat(actual).isEmpty();
		}
	}
}