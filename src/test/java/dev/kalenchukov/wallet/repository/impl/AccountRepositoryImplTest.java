/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.modules.Liquibase;
import dev.kalenchukov.wallet.properties.Props;
import dev.kalenchukov.wallet.repository.AccountRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Класс проверки методов класса {@link AccountRepositoryImpl}.
 */
@Testcontainers
public class AccountRepositoryImplTest {
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
	 * Класс проверки метода {@link AccountRepositoryImpl#save(Account)}.
	 */
	@Nested
	public class Save {
		/**
		 * Проверка метода {@link AccountRepositoryImpl#save(Account)}.
		 */
		@Test
		public void save() {
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(1L);
			when(account.getAmount()).thenReturn(BigDecimal.TEN);
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			Account actualAccount = accountRepository.save(account);

			assertThat(actualAccount.getAccountId()).isPositive();
			assertThat(actualAccount.getPlayerId()).isEqualTo(account.getPlayerId());
			assertThat(actualAccount.getAmount()).isEqualTo(account.getAmount());
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#save(Account)}
		 * с {@code null} в качестве счёта.
		 */
		@Test
		public void saveWithNull() {
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountRepository.save(null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link AccountRepositoryImpl#updateAmount(long, long, BigDecimal)}.
	 */
	@Nested
	public class UpdateAmount {
		/**
		 * Проверка метода {@link AccountRepositoryImpl#updateAmount(long, long, BigDecimal)}.
		 */
		@Test
		public void updateAmount() {
			long playerId = 1L;
			long accountId = 2L;
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			boolean actual = accountRepository.updateAmount(playerId, accountId, new BigDecimal("97.88"));

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#updateAmount(long, long, BigDecimal)}
		 * с отсутствующим счётом.
		 */
		@Test
		public void updateAmountNotFoundAccountId() {
			long playerId = 32L;
			long accountId = 24565464L;
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			boolean actual = accountRepository.updateAmount(playerId, accountId, new BigDecimal("97.88"));

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#updateAmount(long, long, BigDecimal)}
		 * с {@code null} в качестве суммы.
		 */
		@Test
		public void updateAmountWithNullAmount() {
			long playerId = 32L;
			long accountId = 11L;
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountRepository.updateAmount(playerId, accountId, null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link AccountRepositoryImpl#findById(long, long)}.
	 */
	@Nested
	public class FindById {
		/**
		 * Проверка метода {@link AccountRepositoryImpl#findById(long, long)}.
		 */
		@Test
		public void findById() {
			long playerId = 2L;
			long accountId = 4L;
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			Optional<Account> actualAccount = accountRepository.findById(playerId, accountId);

			assertThat(actualAccount).isPresent();
			assertThat(actualAccount.get().getAccountId()).isEqualTo(accountId);
			assertThat(actualAccount.get().getPlayerId()).isEqualTo(2L);
			assertThat(actualAccount.get().getAmount()).isEqualTo(new BigDecimal("40.0"));
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#findById(long, long)}
		 * с отсутствующим счётом.
		 */
		@Test
		public void findByIdWithNotFound() {
			long playerId = 91L;
			long accountId = 7897894L;
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			Optional<Account> actualAccount = accountRepository.findById(playerId, accountId);

			assertThat(actualAccount).isEmpty();
		}
	}
}