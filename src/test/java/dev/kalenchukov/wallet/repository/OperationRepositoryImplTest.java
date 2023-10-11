/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.entity.Player;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link OperationRepositoryImpl}.
 */
public class OperationRepositoryImplTest {
	/**
	 * Класс проверки метода {@link OperationRepositoryImpl#save(Operation)}.
	 */
	@Nested
	public class Save {
		/**
		 * Проверка метода {@link OperationRepositoryImpl#save(Operation)}.
		 */
		@Test
		public void save() throws NoSuchFieldException, IllegalAccessException {
			Set<Operation> data = spy(new HashSet<>());
			Operation operation = mock(Operation.class);
			OperationRepository operationRepository = new OperationRepositoryImpl();
			when(data.add(operation)).thenReturn(true);

			Field field = operationRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(operationRepository, data);

			Operation actual = operationRepository.save(operation);

			assertThat(actual).isEqualTo(operation);
			verify(data, only()).add(operation);
		}

		/**
		 * Проверка метода {@link OperationRepositoryImpl#save(Operation)} с {@code null} в качестве операции.
		 */
		@Test
		public void saveWithNullOperation() {
			OperationRepository operationRepository = new OperationRepositoryImpl();

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				operationRepository.save(null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link OperationRepositoryImpl#getById(long, Player)}.
	 */
	@Nested
	public class GetById {
		/**
		 * Проверка метода {@link OperationRepositoryImpl#getById(long, Player)}.
		 */
		@Test
		public void getById() throws NoSuchFieldException, IllegalAccessException {
			long operationId = 17L;
			long playerId = 435L;
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(operationId);
			when(operation.getAccount()).thenReturn(account);
			Set<Operation> data = spy(new HashSet<>());
			data.add(operation);
			OperationRepository operationRepository = new OperationRepositoryImpl();

			Field field = operationRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(operationRepository, data);

			Optional<Operation> actual = operationRepository.getById(operationId, playerId);

			assertThat(actual.isPresent()).isTrue();
			assertThat(actual.get()).isEqualTo(operation);
		}

		/**
		 * Проверка метода {@link OperationRepositoryImpl#getById(long, Player)} с отсутствующей операцией
		 * по идентификатору операции.
		 */
		@Test
		public void getByIdNotExistsByOperationId() throws NoSuchFieldException, IllegalAccessException {
			long operationId = 17L;
			long playerId = 435L;
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(operationId);
			Set<Operation> data = spy(new HashSet<>());
			data.add(operation);
			OperationRepository operationRepository = new OperationRepositoryImpl();

			Field field = operationRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(operationRepository, data);

			Optional<Operation> actual = operationRepository.getById(200L, playerId);

			assertThat(actual).isEmpty();
		}

		/**
		 * Проверка метода {@link OperationRepositoryImpl#getById(long, Player)} с отсутствующей операцией
		 * по игроку.
		 */
		@Test
		public void getByIdNotExistsByPlayer() throws NoSuchFieldException, IllegalAccessException {
			long operationId = 17L;
			long playerId1 = 435L;
			long playerId2 = 2000L;
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId1);
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(operationId);
			when(operation.getAccount()).thenReturn(account);
			Set<Operation> data = spy(new HashSet<>());
			data.add(operation);
			OperationRepository operationRepository = new OperationRepositoryImpl();

			Field field = operationRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(operationRepository, data);

			Optional<Operation> actual = operationRepository.getById(operationId, playerId2);

			assertThat(actual).isEmpty();
		}
	}

	/**
	 * Класс проверки метода {@link OperationRepositoryImpl#find(long, long)}.
	 */
	@Nested
	public class Find {
		/**
		 * Проверка метода {@link OperationRepositoryImpl#find(long, long)}.
		 */
		@Test
		public void find() throws NoSuchFieldException, IllegalAccessException {
			long accountId = 17L;
			long playerId = 435L;
			Account account = mock(Account.class);
			when(account.getAccountId()).thenReturn(accountId);
			when(account.getPlayerId()).thenReturn(playerId);
			Operation operation = mock(Operation.class);
			when(operation.getAccount()).thenReturn(account);
			Set<Operation> data = spy(new HashSet<>());
			data.add(operation);
			OperationRepository operationRepository = new OperationRepositoryImpl();

			Field field = operationRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(operationRepository, data);

			Set<Operation> actual = operationRepository.find(accountId, playerId);

			assertThat(actual).hasSize(1);
		}

		/**
		 * Проверка метода {@link OperationRepositoryImpl#find(long, long)} с отсутствующей операцией
		 * по идентификатору счёта.
		 */
		@Test
		public void findWithNotFoundByAccountId() throws NoSuchFieldException, IllegalAccessException {
			long accountId = 17L;
			long playerId = 435L;
			Account account = mock(Account.class);
			when(account.getAccountId()).thenReturn(accountId);
			when(account.getPlayerId()).thenReturn(playerId);
			Operation operation = mock(Operation.class);
			when(operation.getAccount()).thenReturn(account);
			Set<Operation> data = spy(new HashSet<>());
			data.add(operation);
			OperationRepository operationRepository = new OperationRepositoryImpl();

			Field field = operationRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(operationRepository, data);

			Set<Operation> actual = operationRepository.find(200L, playerId);

			assertThat(actual).isEmpty();
		}

		/**
		 * Проверка метода {@link OperationRepositoryImpl#find(long, long)} с отсутствующей операцией
		 * по игроку.
		 */
		@Test
		public void findWithNotFoundByPlayer() throws NoSuchFieldException, IllegalAccessException {
			long accountId = 17L;
			long playerId1 = 435L;
			long playerId2 = 2000L;
			Account account = mock(Account.class);
			when(account.getAccountId()).thenReturn(accountId);
			when(account.getPlayerId()).thenReturn(playerId1);
			Operation operation = mock(Operation.class);
			when(operation.getAccount()).thenReturn(account);
			Set<Operation> data = spy(new HashSet<>());
			data.add(operation);
			OperationRepository operationRepository = new OperationRepositoryImpl();

			Field field = operationRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(operationRepository, data);

			Set<Operation> actual = operationRepository.find(accountId, playerId2);

			assertThat(actual).isEmpty();
		}
	}
}