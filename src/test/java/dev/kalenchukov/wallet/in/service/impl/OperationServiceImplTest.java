/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.in.service.OperationService;
import dev.kalenchukov.wallet.repository.*;
import dev.kalenchukov.wallet.repository.impl.OperationRepositoryImpl;
import dev.kalenchukov.wallet.type.OperationType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link OperationServiceImpl}.
 */
public class OperationServiceImplTest {
	/**
	 * Класс проверки метода {@link OperationServiceImpl#add(long, long, OperationType, BigDecimal)}.
	 */
	@Nested
	public class Add {
		/**
		 * Проверка метода {@link OperationServiceImpl#add(long, long, OperationType, BigDecimal)}.
		 */
		@Test
		public void add() {
			long playerId = 11L;
			long accountId = 13L;
			Operation operation = mock(Operation.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);

			OperationRepository operationRepository = mock(OperationRepositoryImpl.class);
			when(operationRepository.save(any(Operation.class))).thenReturn(operation);
			OperationService operationService = new OperationServiceImpl(operationRepository);

			Operation actualOperation = operationService.add(playerId, accountId, operationType, amount);

			assertThat(actualOperation).isEqualTo(operation);
			verify(operationRepository, only()).save(any(Operation.class));
		}

		/**
		 * Проверка метода {@link OperationServiceImpl#add(long, long, OperationType, BigDecimal)} с {@code null} в качестве типа операции.
		 */
		@Test
		public void addWithNullOperationType() {
			long playerId = 11L;
			long accountId = 13L;
			BigDecimal amount = mock(BigDecimal.class);
			OperationRepository operationRepository = mock(OperationRepositoryImpl.class);
			OperationService operationService = new OperationServiceImpl(operationRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				operationService.add(playerId, accountId, null, amount);
			});
		}

		/**
		 * Проверка метода {@link OperationServiceImpl#add(long, long, OperationType, BigDecimal)} с {@code null} в качестве суммы.
		 */
		@Test
		public void addWithNullAmount() {
			long playerId = 11L;
			long accountId = 13L;
			OperationType operationType = mock(OperationType.class);
			OperationRepository operationRepository = mock(OperationRepositoryImpl.class);
			OperationService operationService = new OperationServiceImpl(operationRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				operationService.add(playerId, accountId, operationType, null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link OperationServiceImpl#find(long, long)}.
	 */
	@Nested
	public class Find {
		/**
		 * Проверка метода {@link OperationServiceImpl#find(long, long)}.
		 */
		@Test
		public void find() {
			long accountId = 13L;
			long playerId = 435L;
			Operation operation = mock(Operation.class);
			when(operation.getAccountId()).thenReturn(accountId);
			OperationRepository operationRepository = mock(OperationRepositoryImpl.class);
			when(operationRepository.find(accountId, playerId)).thenReturn(Set.of(operation));
			OperationService operationService = new OperationServiceImpl(operationRepository);

			Set<Operation> actual = operationService.find(accountId, playerId);
			Set<Operation> expected = Set.of(operation);

			verify(operationRepository, only()).find(accountId, playerId);
			assertThat(actual).containsExactlyElementsOf(expected);
		}

		/**
		 * Проверка метода {@link OperationServiceImpl#find(long, long)} с отсутствующими операциями.
		 */
		@Test
		public void findWithNotFound() {
			long accountId = 13L;
			long playerId = 435L;
			Operation operation = mock(Operation.class);
			when(operation.getAccountId()).thenReturn(accountId);
			OperationRepository operationRepository = mock(OperationRepositoryImpl.class);
			when(operationRepository.find(accountId, playerId)).thenReturn(Set.of());
			OperationService operationService = new OperationServiceImpl(operationRepository);

			Set<Operation> actual = operationService.find(accountId, playerId);

			verify(operationRepository, only()).find(accountId, playerId);
			assertThat(actual).isEmpty();
		}
	}
}