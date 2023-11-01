/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.modules.Liquibase;
import dev.kalenchukov.wallet.properties.Props;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.type.OperationType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Класс проверки методов класса {@link OperationRepositoryImpl}.
 */
public class OperationRepositoryImplTest {
	private static final PostgreSQLContainer<?> POSTGRES =
			new PostgreSQLContainer<>(Props.get().getTest().getDockerImage());

	private static DataSource DATA_SOURCE;

	@BeforeAll
	public static void beforeAll() {
		POSTGRES.withDatabaseName(Props.get().getDatabase().getName());
		POSTGRES.withUsername(Props.get().getDatabase().getUsername());
		POSTGRES.withPassword(Props.get().getDatabase().getPassword());
		POSTGRES.start();

		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setUrl(POSTGRES.getJdbcUrl());
		dataSource.setUser(POSTGRES.getUsername());
		dataSource.setPassword(POSTGRES.getPassword());
		dataSource.setCurrentSchema(Props.get().getLiquibase().getSchemaApp());
		DATA_SOURCE = dataSource;

		Liquibase.init(
				POSTGRES.getJdbcUrl(),
				POSTGRES.getUsername(),
				POSTGRES.getPassword()
		);
	}

	@AfterAll
	public static void afterAll() {
		POSTGRES.stop();
	}

	/**
	 * Класс проверки метода {@link OperationRepositoryImpl#save(Operation)}.
	 */
	@Nested
	public class Save {
		/**
		 * Проверка метода {@link OperationRepositoryImpl#save(Operation)}.
		 */
		@Test
		public void save() {
			Operation operation = mock(Operation.class);
			when(operation.getPlayerId()).thenReturn(1L);
			when(operation.getAccountId()).thenReturn(1L);
			when(operation.getOperationType()).thenReturn(OperationType.CREDIT);
			when(operation.getAmount()).thenReturn(BigDecimal.ONE);
			OperationRepository operationRepository = new OperationRepositoryImpl(DATA_SOURCE);

			Operation actualOperation = operationRepository.save(operation);

			assertThat(actualOperation.getOperationId()).isPositive();
			assertThat(actualOperation.getAccountId()).isEqualTo(operation.getAccountId());
			assertThat(actualOperation.getOperationType()).isEqualTo(operation.getOperationType());
			assertThat(actualOperation.getAmount()).isEqualTo(operation.getAmount());
		}

		/**
		 * Проверка метода {@link OperationRepositoryImpl#save(Operation)}
		 * с {@code null} в качестве операции.
		 */
		@Test
		public void saveWithNull() {
			OperationRepository operationRepository = new OperationRepositoryImpl(DATA_SOURCE);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				operationRepository.save(null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link OperationRepositoryImpl#findById(long, long, long)}.
	 */
	@Nested
	public class FindById {
		/**
		 * Проверка метода {@link OperationRepositoryImpl#findById(long, long, long)}.
		 */
		@Test
		public void findById() {
			long operationId = 2L;
			long accountId = 1L;
			long playerId = 1L;
			OperationRepository operationRepository = new OperationRepositoryImpl(DATA_SOURCE);

			Optional<Operation> actualOperation = operationRepository.findById(playerId, accountId, operationId);

			assertThat(actualOperation).isPresent();
			assertThat(actualOperation.get().getOperationId()).isEqualTo(operationId);
			assertThat(actualOperation.get().getPlayerId()).isEqualTo(playerId);
			assertThat(actualOperation.get().getAccountId()).isEqualTo(accountId);
			assertThat(actualOperation.get().getOperationType()).isEqualTo(OperationType.CREDIT);
			assertThat(actualOperation.get().getAmount()).isEqualTo(new BigDecimal("5.0"));
		}

		/**
		 * Проверка метода {@link OperationRepositoryImpl#findById(long, long, long)}
		 * с отсутствующей операцией
		 * по идентификатору операции.
		 */
		@Test
		public void findByIdNotFoundByOperationId() {
			long operationId = 24546456L;
			long accountId = 1L;
			long playerId = 1L;
			OperationRepository operationRepository = new OperationRepositoryImpl(DATA_SOURCE);

			Optional<Operation> actualOperation = operationRepository.findById(playerId, accountId, operationId);

			assertThat(actualOperation).isEmpty();
		}

		/**
		 * Проверка метода {@link OperationRepositoryImpl#findById(long, long, long)}
		 * с отсутствующей операцией
		 * по игроку.
		 */
		@Test
		public void findByIdNotFoundByPlayer() {
			long operationId = 2L;
			long accountId = 1L;
			long playerId = 34546572L;
			OperationRepository operationRepository = new OperationRepositoryImpl(DATA_SOURCE);

			Optional<Operation> actualOperation = operationRepository.findById(playerId, accountId, operationId);

			assertThat(actualOperation).isEmpty();
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
		public void find() {
			long playerId = 1L;
			long accountId = 2L;
			OperationRepository operationRepository = new OperationRepositoryImpl(DATA_SOURCE);

			List<Operation> actualSet = operationRepository.find(playerId, accountId);

			assertThat(actualSet).hasSize(1);
		}

		/**
		 * Проверка метода {@link OperationRepositoryImpl#find(long, long)}
		 * с отсутствующей операцией.
		 */
		@Test
		public void findWithNotFound() {
			long playerId = 1L;
			long accountId = 465166L;
			OperationRepository operationRepository = new OperationRepositoryImpl(DATA_SOURCE);

			List<Operation> actualSet = operationRepository.find(playerId, accountId);

			assertThat(actualSet).isEmpty();
		}
	}
}