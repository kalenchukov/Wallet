/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.Config;
import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.repository.AccountRepository;
import dev.kalenchukov.wallet.repository.modules.Liquibase;
import org.junit.jupiter.api.*;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Класс проверки методов класса {@link AccountRepositoryImpl}.
 */
@Testcontainers
public class AccountRepositoryImplTest {
	private static final PostgreSQLContainer<?> POSTGRES =
			new PostgreSQLContainer<>(Config.get().getProperty("docker.image"));

	private static DataSource DATA_SOURCE;

	@BeforeAll
	public static void beforeAll() {
		POSTGRES.withDatabaseName(Config.get().getProperty("database"));
		POSTGRES.withUsername(Config.get().getProperty("username"));
		POSTGRES.withPassword(Config.get().getProperty("password"));
		POSTGRES.start();

		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setUrl(POSTGRES.getJdbcUrl());
		dataSource.setUser(POSTGRES.getUsername());
		dataSource.setPassword(POSTGRES.getPassword());
		dataSource.setCurrentSchema(Config.get().getProperty("application.schema"));
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
		 * Проверка метода {@link AccountRepositoryImpl#save(Account)} с {@code null} в качестве счёта.
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
	 * Класс проверки метода {@link AccountRepositoryImpl#updateAmount(long, BigDecimal)}.
	 */
	@Nested
	public class UpdateAmount {
		/**
		 * Проверка метода {@link AccountRepositoryImpl#updateAmount(long, BigDecimal)}.
		 */
		@Test
		public void updateAmount() {
			long accountId = 2L;
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			boolean actual = accountRepository.updateAmount(accountId, new BigDecimal("97.88"));

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#updateAmount(long, BigDecimal)} с отсутствующим счётом.
		 */
		@Test
		public void updateAmountNotFoundAccountId() {
			long accountId = 24565464L;
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			boolean actual = accountRepository.updateAmount(accountId, new BigDecimal("97.88"));

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#updateAmount(long, BigDecimal)} с {@code null} в качестве суммы.
		 */
		@Test
		public void updateAmountWithNullAmount() {
			long accountId = 11L;
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountRepository.updateAmount(accountId, null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link AccountRepositoryImpl#findById(long)}.
	 */
	@Nested
	public class FindById {
		/**
		 * Проверка метода {@link AccountRepositoryImpl#findById(long)}.
		 */
		@Test
		public void findById() {
			long accountId = 4L;
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			Optional<Account> actualAccount = accountRepository.findById(accountId);

			assertThat(actualAccount).isPresent();
			assertThat(actualAccount.get().getAccountId()).isEqualTo(accountId);
			assertThat(actualAccount.get().getPlayerId()).isEqualTo(2L);
			assertThat(actualAccount.get().getAmount()).isEqualTo(new BigDecimal("40.0"));
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#findById(long)} с отсутствующим счётом.
		 */
		@Test
		public void findByIdWithNotFound() {
			long accountId = 7897894L;
			AccountRepository accountRepository = new AccountRepositoryImpl(DATA_SOURCE);

			Optional<Account> actualAccount = accountRepository.findById(accountId);

			assertThat(actualAccount).isEmpty();
		}
	}
}