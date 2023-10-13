/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NotFoundAccountException;
import dev.kalenchukov.wallet.repository.*;
import dev.kalenchukov.wallet.resources.OperationType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link OperationServiceImpl}.
 */
public class OperationServiceImplTest {
	/**
	 * Класс проверки метода {@link OperationServiceImpl#add(long, OperationType, BigDecimal)}.
	 */
	@Nested
	public class Add {
		/**
		 * Проверка метода {@link OperationServiceImpl#add(long, OperationType, BigDecimal)}.
		 */
		@Test
		public void add() throws NotFoundAccountException {
			long accountId = 13L;
			Account account = mock(Account.class);
			Operation operation = mock(Operation.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);

			OperationRepository operationRepository = mock(OperationRepositoryImpl.class);
			when(operationRepository.save(any(Operation.class))).thenReturn(operation);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.getById(anyLong())).thenReturn(Optional.of(account));
			OperationService operationService = new OperationServiceImpl(operationRepository, accountRepository);

			Operation actualOperation = operationService.add(accountId, operationType, amount);

			assertThat(actualOperation).isEqualTo(operation);
			verify(operationRepository, only()).save(any(Operation.class));
			verify(accountRepository, only()).getById(anyLong());
		}

		/**
		 * Проверка метода {@link OperationServiceImpl#add(long, OperationType, BigDecimal)} с {@code null} в качестве типа операции.
		 */
		@Test
		public void addWithNullOperationType() {
			long accountId = 13L;
			BigDecimal amount = mock(BigDecimal.class);
			OperationRepository operationRepository = mock(OperationRepositoryImpl.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			OperationService operationService = new OperationServiceImpl(operationRepository, accountRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				operationService.add(accountId, null, amount);
			});
		}

		/**
		 * Проверка метода {@link OperationServiceImpl#add(long, OperationType, BigDecimal)} с {@code null} в качестве суммы.
		 */
		@Test
		public void addWithNullAmount() {
			long accountId = 13L;
			OperationType operationType = mock(OperationType.class);
			OperationRepository operationRepository = mock(OperationRepositoryImpl.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			OperationService operationService = new OperationServiceImpl(operationRepository, accountRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				operationService.add(accountId, operationType, null);
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
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			Operation operation = mock(Operation.class);
			when(operation.getAccount()).thenReturn(account);
			OperationRepository operationRepository = mock(OperationRepositoryImpl.class);
			when(operationRepository.find(accountId, playerId)).thenReturn(Set.of(operation));
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			OperationService operationService = new OperationServiceImpl(operationRepository, accountRepository);

			Set<Operation> actual = operationService.find(accountId, playerId);
			Set<Operation> expected = Set.of(operation);

			verify(operationRepository, only()).find(accountId, playerId);
			assertThat(actual).containsExactlyElementsOf(expected);
		}

		/**
		 * Проверка метода {@link OperationServiceImpl#find(long, long)}.
		 */
		@Test
		public void findWithNotFound() {
			long accountId = 13L;
			long playerId = 435L;
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			Operation operation = mock(Operation.class);
			when(operation.getAccount()).thenReturn(account);
			OperationRepository operationRepository = mock(OperationRepositoryImpl.class);
			when(operationRepository.find(accountId, playerId)).thenReturn(Set.of());
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			OperationService operationService = new OperationServiceImpl(operationRepository, accountRepository);

			Set<Operation> actual = operationService.find(accountId, playerId);

			verify(operationRepository, only()).find(accountId, playerId);
			assertThat(actual).isEmpty();
		}
	}
}